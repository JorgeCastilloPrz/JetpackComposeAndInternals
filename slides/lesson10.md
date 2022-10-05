## **10. Architecture, accessibility, testing**

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
    var loading: Boolean // loading without content? 🤔
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

```kotlin
class ComponentPreview(
    private val browserComponent: ShowkaseBrowserComponent
) {
    val content: @Composable () -> Unit =
      browserComponent.component

    override fun toString(): String =
      browserComponent.group + ":" +
      browserComponent.componentName
}
```

---

* `TestParameterInjector` to create multiple unit tests from each **`@Test`**

```kotlin
@RunWith(TestParameterInjector::class)
class ComposePaparazziTests {

  object PreviewProvider : TestParameter.TestParameterValuesProvider {
    override fun provideValues(): List<ComponentPreview> =
      Showkase.getMetadata().componentList.map(::ComponentPreview)
  }

  @get:Rule
  val paparazzi = Paparazzi(
    maxPercentDifference = 0.0,
    deviceConfig = PIXEL_5.copy(softButtons = false),
  )

  @Test
  fun preview_tests(
        @TestParameter(valuesProvider = PreviewProvider::class) componentPreview: ComponentPreview,
        @TestParameter(value = ["1.0", "1.5"]) fontScale: Float,
        @TestParameter(value = ["light", "dark"]) theme: String
  ) {
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

* Semantic trees. Merged and unmerged
* Merging policies
* Adding semantics to our Composables
* How semantics are handled / wired in Android
* Tools leveraging the semantic trees
* UI testing our Composables