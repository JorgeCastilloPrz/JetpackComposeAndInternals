## **10. Architecture, accessibility, testing.**

---

<img src="slides/images/mindshift.png" width=600 />

---

#### **Imperative 👉 Declarative**

<img src="slides/images/declarative.png" width=600 />

---

#### **`View` (imperative)**

```kotlin
fun updateCount(count: Int) {
  if (count > 0 && !hasBadge()) {
    addBadge()
  } else if (count == 0 && hasBadge()) {
    removeBadge()
  }
  if (count > 99 && !hasFire()) {
    addFire()
    setBadgeText("99+")
  } else if (count <= 99 && hasFire()) {
    removeFire()
  }
  if (count > 0 && !hasPaper()) {
   addPaper()
  } else if (count == 0 && hasPaper()) {
   removePaper()
  }
  if (count <= 99) {
    setBadgeText("$count")
  }
}
```

---

#### **Compose (declarative)**

* Describe what to show
* No thinking about previous state or how to transition from it

```kotlin
@Composable
fun BadgedEnvelope(count: Int) {
  Envelope(fire=count > 99, paper=count > 0) {
    if (count > 0) {
      Badge(text="$count")
    }
  }
}
```

---

#### **Data binding**

* Views had data binding. Expensive for UDF. No diffing supported (except RecyclerView)

* **Compose does diffing by design** 👉 rebind all state, only parts that change recompose 🔝

---

#### **Modeling UI State**

* Make it **exhaustive**
* Incorrect states are impossible
* Allows exhaustive evaluation from UI

```kotlin
sealed interface HeroesUiState {
  object Idle : HeroesUiState
  object Loading : HeroesUiState
  data class Error(val errorMsg: String) : HeroesUiState
  data class Content(val heroes: List<Hero>): HeroesUiState
}
```

---

#### **Modeling variants (2 options)**

Mutable / nullable props 🚨 <span class="error">(impossible states)</span>

```kotlin
sealed interface HeroesUiState {
  // ...
  data class Content(
    val heroes: List<Hero>,
    val loading: Boolean? = null // loading without content?
  ): HeroesUiState
}
```

Exhaustive (verbosity for correctness)

```kotlin
sealed interface HeroesUiState {
  object Idle : HeroesUiState
  object Loading : HeroesUiState
  data class Error(val errorMsg: String) : HeroesUiState
  data class Content(val heroes: List<Hero>): HeroesUiState
  data class LoadingAndContent(val heroes: List<Hero>): HeroesUiState
}
```

---

#### **Architecture (UDF)**

<img src="slides/images/state_holders.png" width="900">

---

#### **Structuring the app**

* 2 options ✌

  * Single `Activity`, multiple **`Fragments` with Composable content** (`ComposeView`), navigation component

  * Single `Activity`, **Composable screens**, Compose (or custom) navigation

---

#### **Compose Navigation**

* Navigation state is hoisted

```kotlin
@Composable
fun SuperHeroesApp() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "heroes_feed"
  ) {
    composable("heroes_feed") { SuperHeroes(/*...*/) }
    composable("hero_detail") { HeroDetail(/*...*/) }
  }
}
```

---
<!-- .slide: data-scene="Slides" -->

#### **Navigating to route**

```kotlin
navController.navigate("heroes")
```

* Passing arguments

```kotlin
NavHost(..., startDestination = "heroes") {
  composable("heroes") {
    SuperHeroes(...) { hero -> // onHeroClick
      navController.navigate("hero/${hero.id}") {
        popUpTo(navContr.graph.findStartDestination().id)
        // popUpTo("heroes") { inclusive = true }
        // launchSingleTop = true
        // restoreState = true
      }
    }
  }
  composable("hero/{id}") { backStackEntry ->
    HeroDetail(backStackEntry.arguments?.getString("id"))
  }
}
```

---

<!-- .slide: data-scene="Coding" -->

📝 Exercise 11: Adding Compose Navigation

---
<!-- .slide: data-scene="Slides" -->

#### **Dependency Injection** 💉

* 🚨 **Don't abuse `CompositionLocal`**
  * Harder to reason
  * Unclear source of truth for its value
  * Only for cross-cutting params **potentially needed by any descendant**
  * Would bloat Composable params otherwise

---

#### **Dependency Injection**

* Inject at root level, hoist state

* Hilt recommended

---

```kotlin
@HiltAndroidApp
class SpeakersApp : Application() { ... }

@AndroidEntryPoint // can also annotate fragments
class MainActivity : ComponentActivity() {
  // Inject deps here, pass them to your Composables
}

@HiltViewModel
class ForYouViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val repo: SpeakerRepository,
) : ViewModel() {
  // ...
}
```
```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindSpeakerRepo(
        repo: FakeSpeakerRepository
    ): SpeakerRepository
}
```

---

#### **`hiltViewModel`**

* Scoping `ViewModel` to destination
* (Single activity + Composable screens)

```gradle
"androidx.hilt:hilt-navigation-compose:$version"
```

```kotlin
@Composable
fun SpeakerFeed(
  viewModel: SpeakerFeedViewModel = hiltViewModel(),
  // viewModel() for scoping to host Activity/Fragment
) {
    val speakers by viewModel.speakers
      .collectAsStateWithLifecycle()

    LazyColumn {
        items(speakers) { speaker ->
            SpeakerCard(speaker)
        }
    }
}
```

---

#### **Headless screenshot tests**

```bash
⏺ > ./gradlew recordPaparazziDebug # record golden
✅ > ./gradlew verifyPaparazziDebug # compare/verify
```

* Relies on LayoutLib

```kotlin
class HeroesFeedTest {

  @get:Rule
  val paparazzi = Paparazzi(
    deviceConfig = PIXEL_5,
    theme = "android:Theme.Material.Light.NoActionBar",
    environment = detectEnvironment().copy(
      platformDir = "${androidHome()}/platforms/android-32",
      compileSdkVersion = 32
    )
  )

  @Test fun heroes_feed_looks_as_expected() {
    paparazzi.snapshot {
      HeroesTheme {
        HeroesFeed(someHeroes())
      }
    }
  }
}
```

---

#### **Automatic headless SS testing of previews**

* Integrate with airbnb/Showkase
* All **`@Preview`** methods automatically tested

---

#### **`TestParameterInjector`**

* Create multiple unit tests from each **`@Test`**
* E.g: dark/light mode and 1.5 font scale

<img src="slides/images/showkase.png" width=400 />

---

```kotlin
@RunWith(TestParameterInjector::class) // 👈
class ComposePaparazziTests {

  // Provides all @Preview methods to the test
  object PreviewProvider : TestParameterValuesProvider {
    override fun provideValues(): List<ComponentPreview> =
      Showkase.getMetadata()
              .componentList
              .map(::ComponentPreview)
  }

  enum class BaseDeviceConfig(
    val deviceConfig: DeviceConfig,
  ) {
    NEXUS_5(DeviceConfig.NEXUS_5),
    PIXEL_5(DeviceConfig.PIXEL_5),
    PIXEL_C(DeviceConfig.PIXEL_C),
  }

  @get:Rule
  val paparazzi = Paparazzi(maxPercentDifference = 0.0)

  @Test
  fun preview_tests(
    @TestParameter(valuesProvider = PreviewProvider::class) componentPreview: ComponentPreview,
    @TestParameter baseDeviceConfig: BaseDeviceConfig,
    @TestParameter(value = ["1.0", "1.5"]) fontScale: Float,
    @TestParameter(value = ["light", "dark"]) theme: String
  ) {
    paparazzi.unsafeUpdateConfig(
      baseDeviceConfig.deviceConfig.copy(
        softButtons = false,
      )
    )

    paparazzi.snapshot() {
      CompositionLocalProvider(
        LocalInspectionMode provides true,
        LocalDensity provides Density(
          density = LocalDensity.current.density,
          fontScale = fontScale
        )
      ) {
        ShowkaseTheme(darkTheme = (theme == "dark")) {
          componentPreview.content()
        }
      }
    }
  }
}
```

---

```kotlin
class ComponentPreview(
    private val browserComponent: ShowkaseBrowserComponent
) {
    val content: @Composable () -> Unit =
      browserComponent.component

    override fun toString(): String =
      browserComponent.componentKey
}
```

---

#### **Semantic trees** 🌲

* Describe UI in an alternative manner
* Accessibility, testing, other tooling

<img src="slides/images/semantic_trees.png" width=400 />

---

#### **`printToLog` (Tests)**

<img src="slides/images/semantics_test_ui.png" width=250 />

---

#### **`printToLog` (Tests)**

```
Node #1 at (l=0, t=171, r=1080, b=2148)px
 |-Node #2 at (l=0, t=171, r=1080, b=2148)px
    |-Node #19 at (l=0, t=325, r=1080, b=2148)px, Tag: 'SpeakersList'
    | VerticalScrollAxisRange = 'ScrollAxisRange(value=0, maxValue=8, reverseScrolling=false)'
    | CollectionInfo = 'androidx.compose.ui.semantics.CollectionInfo@29710cd'
    | Actions = [IndexForKey, ScrollBy, ScrollToIndex]
    |  |-Node #21 at (l=22, t=347, r=1058, b=716)px
    |  | Role = 'Button'
    |  | Focused = 'false'
    |  | ContentDescription = '[John Doe avatar]'
    |  | Text = '[John Doe, Uber]'
    |  | Actions = [OnClick, RequestFocus, GetTextLayoutResult]
    |  | MergeDescendants = 'true'
    |  |-Node #28 at (l=22, t=760, r=1058, b=1129)px
    |  | Role = 'Button'
    |  | Focused = 'false'
    |  | ContentDescription = '[Sylvia Lotte avatar]'
    |  | Text = '[Sylvia Lotte, Lyft]'
    |  | Actions = [OnClick, RequestFocus, GetTextLayoutResult]
    |  | MergeDescendants = 'true'
    |  |-Node #35 at (l=22, t=1173, r=1058, b=1542)px
    |  | Role = 'Button'
    |  | Focused = 'false'
    |  | ContentDescription = '[Apis Anoubis avatar]'
    |  | Text = '[Apis Anoubis, Twitter]'
    |  | Actions = [OnClick, RequestFocus, GetTextLayoutResult]
    |  | MergeDescendants = 'true'
    |  | ... (more list items)
    |-Node #5 at (l=0, t=171, r=1080, b=325)px
    |  |-Node #9 at (l=44, t=211, r=278, b=285)px
    |    Text = '[Speakers]'
    |    Actions = [GetTextLayoutResult]
    |-Node #14 at (l=882, t=1950, r=1036, b=2104)px
      Role = 'Button'
      Focused = 'false'
      ContentDescription = '[Button to add a new speaker]'
      Actions = [OnClick, RequestFocus]
      MergeDescendants = 'true'
```

---

#### **`Layout Inspector`**

<img src="slides/images/semantics_switch.png" width=200 />

<img src="slides/images/semantics_layout_inspector.png" width=800 />

---

#### **Providing semantics**

* `foundation` and `material` Components have built-in semantics
* Custom `Layout`s drawing to canvas directly 👉 we should add semantics

<img src="slides/images/calendar.png" width=250 />

---

#### **Matching from tests**

```kotlin
composeTestRule.onNode(hasText("Button"))
    .performClick()
    .assertIsDisplayed()
```

---

#### **Merged vs Unmerged** 🌲

* Only Composables with semantics are included
* Each node is a focusable element for accessibility

<img src="slides/images/talkback.png" width=250 />

---

#### **Merged vs Unmerged** 🌲

* Merging sub-trees can be more **meaningful**

```kotlin
Button(onClick = { /*TODO*/ }) {
  Icon(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null
  )
  Spacer(Modifier.size(ButtonDefaults.IconSpacing))
  Text("Like")
}
```

<img src="slides/images/semantics_merged.png" width=250 />

---

#### **Merged vs Unmerged** 🌲

<img src="slides/images/semantics_merged_vs_unmerged1.png" width=400 />

<img src="slides/images/semantics_merged_vs_unmerged2.png" width=800 />

---

```kotlin
@Composable
private fun PostMetadata(metadata: Metadata) {
  // Merge elements below for accessibility purposes
  Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
    Image(
      imageVector = Icons.Filled.AccountCircle,
      contentDescription = null // decorative
    )
    Column {
      Text(metadata.author.name)
      Text("${metadata.date} • ${metadata.readTimeMinutes} min read")
    }
  }
}
```

<img src="slides/images/semantics_merge1.png" width=427 />

<img src="slides/images/semantics_merge2.png" width=400 />

---

#### **Case Study** 🧐

* **Mosaic:** Writing a client library for the Compose compiler and runtime

---

## **Thank you!** 🙏🏿

[@JorgeCastilloPR](https://twitter.com/jorgecastillopr)

[effectiveandroid.substack.com](https://effectiveandroid.substack.com/)
