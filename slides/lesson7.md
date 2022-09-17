## **7. Saving & restoring State**

---

### **`rememberSaveable`** ✨

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

### **`rememberSaveable`**

Rec. for simple **UI element state** only

* Scroll position
* Selected items on a list
* Checked/unchecked state
* Text input
* ...

---

### **`rememberSaveable`**

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

### Custom **savers**

* `mapSaver`

```kotlin
var speaker by rememberSaveable(SpeakerMapSaver) {
  mutableStateOf(Speaker("1", "John Doe", "event1"))
}

val SpeakerMapSaver = run {
  val idKey = "id"
  val nameKey = "name"
  val eventIdKey = "eventId"

  mapSaver(
    save = { mapOf(idKey to it.id, nameKey to it.name, eventIdKey to it.eventId) },
    restore = { Speaker(it[idKey] as String, it[nameKey] as String, it[eventIdKey] as String) }
  )
}
```

---

### Custom **savers**

* `listSaver`

```kotlin
var speaker by rememberSaveable(SpeakerListSaver) {
  mutableStateOf(Speaker("1", "John Doe", "event1"))
}

val SpeakerListSaver = run {
  listSaver<Speaker, Any>(
    save = { listOf(it.id, it.name, it.eventId) },
    restore = { Speaker(it[0] as String, it[1] as String, it[2] as String) }
  )
}
```

---

#### Too many **responsibilities**?

```kotlin
@Composable
fun SpeakersScreen(eventId: String, service: SpeakerService) {
  var speakers by rememberSaveable {
    mutableStateOf(emptyList())
  }

  // Suspend effect to load the speakers
  LaunchedEffect(eventId) {
    speakers = service.loadSpeakers(eventId)
  }

  LazyColumn {
    items(speakers) { speaker -> SpeakerCard(speaker) }
  }
}
```

* UI and business logic are coupled
* Better add **`ViewModel`** 💡

---

### **`ViewModel`** ✨

```kotlin
@Composable
fun SpeakersScreen(
  viewModel: SpeakersViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    /* ... */

    SpeakersList(
      speakers = uiState.speakers,
      onSpeakerClick = { id -> viewModel.onSpeakerClick(id) }
    )
}
```

* **Scoped to host** (Activity/Fragment)
* **Scoped to backstack entry** (compose navigation)

---

### **`ViewModel`** ✨

* **Decouple** Composables **from business logic**
* Inject `ViewModel` at the root level
* Pass state down the tree ⏬  (**hoisting**)

---

#### Different state holders 🤔

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

### **`ViewModel`** ✨

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

## State integration with 3rd party libs

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

---

## **Saving & restoring** State

---

### **`rememberSaveable`** ✨

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

### **`rememberSaveable`**

Rec. for simple **UI element state** only

* Scroll position
* Selected items on a list
* Checked/unchecked state
* Text input
* ...

---

### **`rememberSaveable`**

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

### Custom **savers**

* `mapSaver`

```kotlin
var speaker by rememberSaveable(SpeakerMapSaver) {
  mutableStateOf(Speaker("1", "John Doe", "event1"))
}

val SpeakerMapSaver = run {
  val idKey = "id"
  val nameKey = "name"
  val eventIdKey = "eventId"

  mapSaver(
    save = { mapOf(idKey to it.id, nameKey to it.name, eventIdKey to it.eventId) },
    restore = { Speaker(it[idKey] as String, it[nameKey] as String, it[eventIdKey] as String) }
  )
}
```

---

### Custom **savers**

* `listSaver`

```kotlin
var speaker by rememberSaveable(SpeakerListSaver) {
  mutableStateOf(Speaker("1", "John Doe", "event1"))
}

val SpeakerListSaver = run {
  listSaver<Speaker, Any>(
    save = { listOf(it.id, it.name, it.eventId) },
    restore = { Speaker(it[0] as String, it[1] as String, it[2] as String) }
  )
}
```

---

#### Too many **responsibilities**?

```kotlin
@Composable
fun SpeakersScreen(eventId: String, service: SpeakerService) {
  var speakers by rememberSaveable {
    mutableStateOf(emptyList())
  }

  // Suspend effect to load the speakers
  LaunchedEffect(eventId) {
    speakers = service.loadSpeakers(eventId)
  }

  LazyColumn {
    items(speakers) { speaker -> SpeakerCard(speaker) }
  }
}
```

* UI and business logic are coupled
* Better add **`ViewModel`** 💡

---

### **`ViewModel`** ✨

```kotlin
@Composable
fun SpeakersScreen(
  viewModel: SpeakersViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    /* ... */

    SpeakersList(
      speakers = uiState.speakers,
      onSpeakerClick = { id -> viewModel.onSpeakerClick(id) }
    )
}
```

* **Scoped to host** (Activity/Fragment)
* **Scoped to backstack entry** (compose navigation)

---

### **`ViewModel`** ✨

* **Decouple** Composables **from business logic**
* Inject `ViewModel` at the root level
* Pass state down the tree ⏬  (**hoisting**)

---

#### Different state holders 🤔

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

### **`ViewModel`** ✨

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

## State integration with 3rd party libs

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
