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
* Selected items on a list
* Checked/unchecked state
* Text input
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

#### **Combine state holders** 🤔

* **`rememberSaveable`** 👉 UI element state

```kotlin
// Example: LazyColumn / LazyRow
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

#### **`ViewModel`** ✨

* Survives **config changes**
* Survives system init. **process death** 👉 (Inject `SavedStateHandle`)

```kotlin
class SpeakersViewModel(
  private val repo: SpeakersRepository,
  private val savedState: SavedStateHandle // process death
) : ViewModel() {

  val uiState: StateFlow<List<Speaker>> = /*...*/

  // ...
}
```

---

### **Exercise 👩🏾‍💻**

Instructions in `SpeakersTest.kt`

---

## **Derived** State

---

## **State integration with 3rd party libs**

* Compose only recomposes automatically from reading `State` objects
* Convert any observable data type to `State` (adapters)

---

### **`StateFlow`**

* Normally within a `ViewModel`

```kotlin
class SpeakersViewModel @Inject constructor(
  private val repo: SpeakersRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(
    SpeakersUiState.Content(emptyList())
  )
  val uiState: StateFlow<SpeakersUiState> =
    _uiState.asStateFlow()

  val uiState: StateFlow<SpeakersUiState> =
    repo.loadSpakers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SpeakersUiState.Loading
    )

  fun onSpeakerClick(speaker: Speaker) { /* ... */ }
}
```

---

### **`collectAsState`**

```kotlin
@Composable
fun SpeakersScreen(
  viewModel: SpeakersViewModel = viewModel()
) {
  val speakers by viewModel.uiState.collectAsState()
  SpeakersList(
    speakers,
    onSpeakerClick = { viewModel.onSpeakerClick(it) }
  )
}
```
