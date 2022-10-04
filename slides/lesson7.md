## **7. State within an architecture**

---

#### **Where** to put state? 🤷

---

#### 1️⃣ **In the UI layer**

* For extremely simple apps

```kotlin
@Composable
fun Counter() {
    // The UI state is managed by the UI itself
    var count by remember { mutableStateOf(0) }
    Row {
        Button(onClick = { ++count }) {
            Text(text = "Increment")
        }
        Button(onClick = { --count }) {
            Text(text = "Decrement")
        }
    }
}
```

---

#### **State + UI logic**

* Starts coupling UI and logic 🤔

```kotlin
@Composable
fun ContactsList(contacts: List<Contact>) {
    val listState = rememberLazyListState()
    val isAtTopOfList by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex < 3
        }
    }
    LazyColumn(state = listState, ...) {...}

    AnimatedVisibility(visible = !isAtTopOfList) {
        ScrollToTopButton()
    }
}
```

---

#### **State + business logic** 🙈

* UI and business logic <span class="error">coupled</span> (testability)

```kotlin
@Composable
fun SpeakersScreen(eventId: String, service: Service) {
  var speakers by remember { mutableStateOf(emptyList()) }

  LaunchedEffect(eventId) { // suspend
    speakers = service.loadSpeakers(eventId)
  }

  LazyColumn {
    items(speakers) { speaker -> SpeakerCard(speaker) }
  }
}
```

---

<img src="slides/images/state_holders.png" width="1000">

---

#### **State holders**

* Store and produce UI state
* Decouple UI from business/data
* Abstract sources of data
* Make UI simpler
* UI behavior / interactions testable
* **`ViewModel`:** Special type of state holder

---

#### **`ViewModel`**

* **Scope to host** (Activity/Fragment)
* **Scope to backstack entry** (compose navigation)

```kotlin
@Composable
fun SpeakersScreen(
  viewModel: SpeakersViewModel = viewModel()
  // For destination scoped use hiltViewModel()
) {
    val uiState = viewModel.uiState
    /* ... */

    SpeakersList(
      speakers = uiState.speakers,
      onSpeakerClick = { id -> viewModel.onSpeakerClick(id) }
    )
}
```

---

#### **`ViewModel`**

* Inject `ViewModel` at the root level
* Pass state down the tree ⏬  **(hoisting)**

---

<img src="slides/images/stateful_vs_stateless.png" width="1000">

---

#### **Config changes & process death**

---

#### **`rememberSaveable`** ✨

Same than `remember`, but survives:

* Config changes
* System initiated process death

```kotlin
@Composable
fun HelloScreen() {
  var name by rememberSaveable { mutableStateOf("") }

  HelloContent(name = name, onNameChange = { name = it })
}
```

---

#### **`rememberSaveable`**

Rec. for simple **UI element state** only

* Scroll position
* Text input
* Selected items on a list
* Checked/unchecked state
* ...

---

#### **`rememberSaveable`**

* Bundle data only
* Or **parcelable** / **serializable**

```kotlin
@Parcelize
data class City(val name: String, val country: String) : Parcelable

@Composable
fun CityScreen() {
    var selectedCity = rememberSaveable {
        mutableStateOf(City("Madrid", "Spain"))
    }
}
```

* But sometimes can't use those 🤔

---

#### **Custom savers**

* `mapSaver`

```kotlin
var speaker by rememberSaveable(SpeakerMapSaver) {
  mutableStateOf(Speaker("1", "John Doe"))
}

val SpeakerMapSaver = run {
  val idKey = "id"
  val nameKey = "name"

  mapSaver(
    save = { mapOf(idKey to it.id, nameKey to it.name) },
    restore = {
      Speaker(it[idKey] as String, it[nameKey] as String)
    })
}
```

---

#### **Custom savers**

* `listSaver`

```kotlin
var speaker by rememberSaveable(SpeakerListSaver) {
  mutableStateOf(Speaker("1", "John Doe"))
}

val SpeakerListSaver = run {
  listSaver<Speaker, Any>(
    save = { listOf(it.id, it.name) },
    restore = { Speaker(it[0] as String, it[1] as String) }
  )
}
```

---

#### **Combine state holders**

* **`rememberSaveable`** 👉 UI element state

```kotlin
// Example: LazyColumn / LazyRow
// scroll position likely not relevant in your ViewModel
@Composable
fun rememberLazyListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    return rememberSaveable(saver = LazyListState.Saver) {
        LazyListState(
            initialFirstVisibleItemIndex,
            initialFirstVisibleItemScrollOffset
        )
    }
}
```

* **`ViewModel`** 👉 screen state

---
<!-- .slide: data-scene="Slides" -->

#### **`ViewModel`**

* Survives **config changes**
* Survives system init. **process death** 👉 (Inject `SavedStateHandle`)

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ExampleRepository
) : ViewModel() { /* ... */ }

@Composable
fun MyScreen(
    viewModel: MyViewModel = viewModel()
) { /* ... */ }
```

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 11: `rememberSaveable` + `ViewModel`

---
<!-- .slide: data-scene="Slides" -->

#### **Derived State**

* Derive `State` from other `State` objects
* **Avoid useless recompositions** (optimization)

```kotlin
// scrollState: Int updates with high frequency. We want to
// update our counter every 100 pixels scrolled only, not
// on every single pixel change.
val counter = remember {
    derivedStateOf {
        // Recompose only when this changes!
        (scrollState / 100).roundToInt()
    }
}

// Recomposed only on counter change, so it will "ignore"
// scrollState in 99% of cases
Text(counter.toString())
```

---

#### **Derived State**

* Example 2: Combining state for 2 counters

```kotlin
var firstCount by remember { mutableStateOf(0) }
var secondCount by remember { mutableStateOf(0) }
```

```kotlin
val totalIsOver10 by remember {
    derivedStateOf {
        val total = firstCount + secondCount
        total > 10 // recompose only when this changes!
    }
}

Text(if (totalIsOver10) "Yay!" else "Nay!")
```

---

#### **Adapters for 3rd party libs**

```kotlin
val state: String? by liveData.observeAsState()
Text("State is $state")

val state by viewModel.stateFlow.collectAsState()
Text("State is $state")

// All RxJava observable data types
val state by viewModel.observable.subscribeAsState()
Text("State is $state")
```

Dependencies

```gradle
"androidx.compose.runtime:runtime-livedata:$composeVersion"
"androidx.compose.runtime:runtime-rxjava2:$composeVersion"
"androidx.compose.runtime:runtime-rxjava3:$composeVersion"
```

---

#### **Collect + lifecycle**

```kotlin
@Composable
fun BookmarksRoute(
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState
      .collectAsStateWithLifecycle()
      // Lifecycle at least in a certain state (collection)
      // Default = Lifecycle.State.STARTED

    BookmarksScreen(
      feedState = feedState,
      onRemoveBookmark = viewModel::onBookmarkRemoved
    )
}
```

---

#### **`ViewModel`**

```kotlin
@HiltViewModel
class BookmarksViewModel @Inject constructor(
  newsRepo: NewsRepository
) : ViewModel() {
  val feedState: StateFlow<NewsFeedUiState> =
    newsRepo
      .getNewsResourcesStream() // Flow
      .map { it.toFeedState() }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading
      )
}
```