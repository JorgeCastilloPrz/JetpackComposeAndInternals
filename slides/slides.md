# State

---

**Source of truth** of our Composables

---

### **map** UI state to UI

![Map state to UI](slides/images/map_state_to_ui.png)

---

### **Map** UI state to UI

```kotlin
@Composable fun NamePlate(name: String) {
    Text(name)
}
```

* `name` 👉 input UI state
* `Text(name)` 👉 emitted UI

---

### To **update** UI 🔄

Re-execute the function with **different args**

```kotlin
NamePlate("John Doe")
```

![Passing different input states](slides/images/input_state1.png)

```kotlin
NamePlate("Jane Smith")
```

![Passing different input states](slides/images/input_state2.png)

---

### Called **Recomposition**

* But how to trigger it? 🤔

---

### Modeling state

```kotlin
@Composable fun ProfileScreen() {
  val nameUiState = remember { mutableStateOf("John Doe") }
  NamePlate(nameUiState.value)
}

@Composable fun NamePlate(name: String) {
  Text(name)
}
```

* Use **mutable** state so it can be updated.
* The state model **can still be immutable**.
* `remember`?

---

### remember 🧠

* Cache state **across recompositions**.
* Calculated on first execution (composition).
* **Not calculated again** on every recomposition.
* Forgotten if the Composable is removed/replaced.

```kotlin
@Composable fun ProfileScreen() {
  // State holder will be created only the first time.
  val nameUiState = remember { mutableStateOf("John Doe") }
  NamePlate(nameUiState.value)
}
```

---

### Triggering recomposition

* Simply **update** the mutable `State` 🤷‍♂️
* Any Composable functions **reading from it** will automatically recompose (i.e: re-execute)

```kotlin
@Composable fun ProfileScreen() {
  Column {
    val nameUiState = remember { mutableStateOf("John Doe") }
    NamePlate(nameUiState.value)

    Button(onClick = { nameUiState.value = "New name" }) {
      Text("Click to get ")
    }
  }
}
```

---

![State sample](slides/images/state_sample.gif)

---

### State **syntax**

```kotlin
// Default
val mutableState = remember { mutableStateOf("John Doe") }
mutableState.value = "New name"

// Alternative 1: Delegation
var value by remember { mutableStateOf("John Doe") }
value = "New name"

// Alternative 2: Destructuring (React style)
val (value, setValue) = remember { mutableStateOf("John Doe") }
setValue("New name")
```

---

### State **hoisting**

* ⏬ **State passed down** the tree as function args
* ⏫ **Events propagated up** the tree via callbacks
* Example: `TextField`

---

### State **hoisting**

```kotlin
@Composable
fun TextBox() {
   OutlinedTextField(
      value = "",
      onValueChange = { },
      label = { Text("Name") }
  )
}
```

* Will **not display** the inserted characters.

---

<img src="slides/images/state_hoisting.gif" width="400">

---

### State **hoisting**

* `TextField` hoists its state.
* We must create and pass state to it.

```kotlin
@Composable
fun TextBox() {
    val inputText = remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.padding(16.dp),
        value = inputText.value, // read
        onValueChange = { inputText.value = it }, // write
        label = { Text("Name") }
    )
}
```

---

<img src="slides/images/state_hoisting2.gif" width="400">

---

### State **hoisting**

* Makes Composables **dummy**.
* Makes Composables **reusable**.
* They simply display the state we pass to them.

---

#### **Stateful** vs **Stateless** Composables

* Stateful 👉 Creates & manages its own state.
  * When caller doesn't need to manage it.
  * **Less reusable**.
  * More frequent at the **root of the tree**.
* Stateless 👉 Hoists its state.
  * More reusable.
  * **Shareable and interceptable state**.

---

### **Exercise 👩🏾‍💻**

 * 1. Create a mutable state to represent the name (String) in `NameGenerator`.
 * 2. Default the state value to the first generated name (`repo.next()`).
 * 2. Make the name text Composable read from the state just created.
 * 3. Update the name on button click. (`repo.next()` to generate a new name).

* **Validate your implementation by running the provided NameGeneratorTest.**

---

## **Smart** recomposition and Class **stability**

---

## Recomposition ⚙️

---

#### **Compiler** prepares the road

* **Rewrites** the function IR
* Wraps restartable funcs into **"restart groups"**
* Teaches runtime how to restart them by...
* Adding restart block at the end 👇

```kotlin
// Transforms this...
@Composable fun A(x: Int) {
  f(x)
}
// ...into this
@Composable fun A(x: Int, ...) {
  $composer.startRestartGroup()
  // ...
  f(x)
  $composer.endRestartGroup()?.updateScope { next ->
    A(x, next, $changed or 0b1)
  }
}
```

---

#### **Compiler** prepares the road

* `endRestartGroup` returns **`null`** when the body doesn't read any State that might vary
* No need to teach runtime how to recompose
* Only re-executes **when state that is read varies**

```kotlin
@Composable fun A(x: Int, ...) {
  $composer.startRestartGroup()
  // ...
  f(x)
  $composer.endRestartGroup()?.updateScope { next ->
    A(x, next, $changed or 0b1)
  }
}
```

---

### **Smart** recomposition

* Avoid recomposing the entire UI
* More efficient than binding UI state with Views
* Only recompose **components that changed**
* Save computation time ✅

---

### **Smart** recomposition

* The runtime automatically **tracks state reads** in Composable functions or lambdas
* Only re-executes those (if state varies)
* **Skips the rest**

---

### **Smart** recomposition

```kotlin
@Composable
fun Counter() {
    RecompositionBox {
        var counter by remember { mutableStateOf(0) }

        RecompositionButton(onClick = { counter++ }) {
            RecompositionText(text = "Counter: $counter")
        }
    }
}
```

* `counter` **is read from**:
  * `RecompositionButton` content lambda
  * `RecompositionText` (input)

---

`RecompositionButton` and `RecompositionText` recompose. **`RecompositionBox` does not**.

<img src="slides/images/recomposition.gif" width="400">

---

[📝 🍩 “donut-hole skipping” in Compose](https://www.jetpackcompose.app/articles/donut-hole-skipping-in-jetpack-compose)

[📝 ⚙️ How to debug recomposition](https://www.jetpackcompose.app/articles/how-can-I-debug-recompositions-in-jetpack-compose)

by [@vinaygaba](https://www.twitter.com/@vinaygaba)

---

## Class **stability**

* Input state **must be reliable (stable)**...
* ...so Compose knows when state didn't change...
* ...and can skip recomposition in that case.

---

## A **stable** class

```kotlin
data class Person(val name: String, val phone: String)

@Composable
fun PersonView(person: Person) {
  Text(person.name)
}
```

* Immutable class + immutable properties
* Once created, it will not vary 👍
* Comparing two instances **is safe**
* Compose knows when it changed
* `PersonView` calls **can be skipped** if it didn't

---

## An <span class="error">unstable</span> class

```kotlin
data class Person(var name: String, var phone: String)

@Composable
fun PersonView(person: Person) {
  Text(person.name)
}
```

* <span class="error">Mutable</span> class or <span class="error">mutable</span> properties
* Once created, it might vary 🚨
* Unsafe in concurrency scenarios
* Comparing two instances 🤷‍♀️
* Compose defaults to <span class="error">never skip</span> 🚫
* Always recompose 👉 performance ⏬

---

## Use **immutability**

(Esp. for UI state)

* If not:
  * Removes any chance of runtime optimization
  * Opens the door to bugs and race conditions (modifying data before comparing)

---

## Another <span class="error">unstable</span> class

```kotlin
data class Conference(val talks: List<Talk>)

data class Talk(val title: String, val duration: Int)

@Composable
fun Conference(talks: List<Talk>) {
  LazyColumn {
   items(talks) { talk ->
     TalkCard(talk)
   }
  }
}
```

* ⚠️ Collections can be mutable (impl)
* Compose **flags the param as unstable** for safety

---

## Class **stability**

* Compose compiler **can infer** class stability 🧠
* Flags classes (& properties) as stable/unstable
* **Not all the cases can be inferred**

---

### When **inference** fails

* Compiler can't infer **how** our code is used
  * Mutable data structure w/ immutable public api
  * Only using immutable collection impls
  * ...
* We can let it know explicitly 👇
  * Use **`@Stable`** or **`@Immutable`**

---

## **`@Stable`**

* **`a.equals(b)`** always returns the same value for the same instances
* Changes to public props are notified to Compose
* All public properties are also stable

```kotlin
// a.equals(b) doesn't vary even if the states are mutated.
@Stable
data class MyScreenState(val screenName: String) {
    var isLoading: Boolean by mutableStateOf(false)
    var content: User? by mutableStateOf(null)
    var error: String by mutableStateOf("")
}
```

---

## **`@Stable`**

* This test passes ✅

```kotlin
@Test
fun `mutation does not affect equals comparison`() {
    val state1 = MyScreenState("Screen 1")
    val state2 = MyScreenState("Screen 1")
    state2.isLoading = true

    assertThat(state1, `is`(state2))
}
```

* Only `screenName` is compared
* Compose is notified of any change 👍
* `String` and `MutableState` are `@Stable`

---

## **`@Immutable`**

`@Immutable` implies `@Stable`.

```kotlin
@Immutable
data class Conference(val talks: List<Talk>)
```

---

### How to know **if I need them?**

* [📝 Measure Compose Compiler metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md)
* Look for funcs **restartable but not skippable** 🤔
* [📝 Composable metrics](https://chris.banes.dev/composable-metrics/) by Chris Banes

---

## 📸 **Snapshot** State

---

* **Isolated** state that can be remembered and observed for changes.

---

### **Snapshot** State 📸

* Any implementation of `State`:

```kotlin
@Stable
interface State<out T>{
  val value: T
}
```

* `MutableState`
* `AnimationState`
* `DerivedState`
* ...

---

### **Snapshot** State 📸

* Obtained from apis like 👇
  * `mutableStateOf`
  * `mutableStateListOf`
  * `mutableStateMapOf`
  * `derivedStateOf`
  * `produceState`
  * `collectAsState`
  * ...

---

### Why to **isolate** State? 🤔

---

### **Concurrency**

* Offloading composition to **different threads**
* **Parallel composition**
* **Reordering compositions**
* No guarantees that our Composable will execute on a specific thread 🤷‍♀️

---

Write to state from a different thread ✏️

```kotlin
@Composable
fun MyComposable() {
    val uiState = remember { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        launch(Dispatchers.IO) {
            delay(2000)
            uiState.value = "COMPLETE!!"
        }
    }
    if (uiState.value.isEmpty()) {
        CircularProgressIndicator()
    } else {
        Text(uiState.value)
    }
}
```

---

<img src="slides/images/snapshot_state_1.gif" width="400">

---

### 2 **Strategies**

* **Immutability** 👉 safe for concurrency.
* **Mutability + isolation** 👉  Each thread maintains its own copy of the state. Global coordination needed to keep **global program state coherent**.

---

### In Compose

* **Mutable state** 👉  **observe changes**
* Work with mutable state across threads
* Isolation + propagation needed

---

### Snapshot State **system**

* Models and coordinates **state changes** and **state propagation**
* Part of the Jetpack Compose runtime
* Decoupled 👉 Could be used by other libraries

---

### Taking a snapshot 📸

* A "picture" of our app state **at a given instant**
* A context for our state reads

```kotlin
var name by mutableStateOf("")
name = "Aleesha Salgado"
val snapshot = Snapshot.takeSnapshot()
name = "Jessica Jones"

println(name) // Jessica Jones
snapshot.enter { println(name) } // Aleesha Salgado
println(name) // Jessica Jones
```

---

#### Modifying state in a snapshot

* `Snapshot.apply()` 👉 **propagate changes to other snapshots**.

```kotlin
var name by mutableStateOf("")
name = "Aleesha Salgado"
val snapshot = Snapshot.takeMutableSnapshot()

snapshot.enter { name = "Jessica Jones" }
println(name) // Aleesha Salgado

snapshot.apply() // propagate changes ✨

println(name) // Jessica Jones
```

---

### **Nested** snapshots

* taking a snapshot within the `enter` block

```kotlin
var name by mutableStateOf("")
name = "Aleesha Salgado"

val first = Snapshot.takeMutableSnapshot()
first.enter {
  name = "Jessica Jones"

  val second = Snapshot.takeMutableSnapshot()
  second.enter {
    name = "Cassandra Higgins"
  }
  println(name) // Jessica Jones
  second.apply()
  println(name) // Cassandra Higgins
}
println(name) // Aleesha Salgado
first.apply()
println(name) // Cassandra Higgins
```

---

## The Snapshot **tree** 🌲

![snapshot tree](slides/images/snapshottree.png)

---

### And within Compose? 🤔

* **Track reads and writes** automatically
* Compose passes read and write **observers** when taking the Snapshot 👇

```kotlin
Snapshot.takeMutableSnapshot(readObserver, writeObserver)
```

---

### **When** are snapshots created?

* One `GlobalSnapshot` (root)
* A new **one per thread** where `State` is read/written
* Created by the runtime (not manually)

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
