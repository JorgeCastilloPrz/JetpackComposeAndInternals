## **6. Snapshot State**

---

The **source of truth** of our Composables

<img src="slides/images/state_source_of_truth.png" width=700 />

<img src="slides/images/state_source_of_truth2.png" width=700 />

---

<img src="slides/images/update_ui.png" width=800 />

---

#### **Modeling state**

```kotlin
@Composable fun ProfileScreen() {
  val nameUiState = remember { mutableStateOf("John Doe") }
  NamePlate(nameUiState.value)
}

@Composable fun NamePlate(name: String) {
  Text(name)
}
```

* **mutable** state (so it can be updated)
* The state model **can still be immutable**
* Will trigger recomposition when it changes

---

#### **`remember`** 🧠

```kotlin
@Composable fun ProfileScreen() {
  val nameUiState = remember { mutableStateOf("John Doe") }
  NamePlate(nameUiState.value)
}
```

* Cache state **across recompositions**
* Calculated on 1st execution (composition)
* Reused cached value after recomposition/s
* Forgotten if Composable removed/replaced, or host `ComposeView` detached

---

#### **Triggering recomposition**

* Just write to the state 🤷‍♂️

```kotlin
@Composable fun ProfileScreen() {
  Column {
    val nameUiState = remember { mutableStateOf("John Doe") }
    NamePlate(nameUiState.value)

    Button(onClick = { nameUiState.value = "New name" }) {
      Text("Click to get")
    }
  }
}
```

---

<img src="slides/images/state_sample.gif" width="400">

---

#### **syntax**

```kotlin
// Default
val mutableState = remember { mutableStateOf("John") }
mutableState.value = "New name"

// Alternative 1: Delegation
var value by remember { mutableStateOf("John") }
value = "New name"

// Alternative 2: Destructuring (React style)
val (value, setValue) = remember { mutableStateOf("John") }
setValue("New name")
```

---

#### **State hoisting**

* ⏬ **State passed down** (function args)
* ⏫ **Events propagated up** (callbacks)

---

<img src="slides/images/speakerpanel.png" width="400">

```kotlin
@Composable
fun SpeakerPanel(speaker: Speaker, onFollow: (SpeakerId) -> Unit) {
    Card(/* modifiers */) {
        Row(/* modifiers */) {
            CircledImage(speaker.image) 👈
            SpeakerDetails(
                name = speaker.name, 👈
                company = speaker.company, 👈
                onFollow = { onFollow(speaker.id) }) 👈
        }
    }
}

@Composable
fun CircledImage(@DrawableRes imageRes: Int) {
  Image(
    painter = painterResource(imageRes), 👈
    contentScale = ContentScale.Crop,
    modifier = Modifier.size(102.dp).clip(CircleShape)
  )
}

@Composable
fun SpeakerDetails(name: String, company: String, onFollow: () -> Unit) {
    Column(/* modifiers */) {
        Text(text = name, /* style... */) 👈
        Text(text = company, /* style... */) 👈
        Button(onClick = onFollow) { 👈
            Text("Follow")
        }
    }
}
```

---

<img src="slides/images/state_hoisting.png" width="800">

---

#### **State hoisting**

Another example: `TextField`

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

Will **not display** the inserted characters 😲

---

<img src="slides/images/state_hoisting.gif" width="400">

---

#### **State hoisting**

* `TextField` hoists its state
* We create and pass the state to it

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

#### **State hoisting**

* Makes Composables **dummy**, **reusable**

* They simply display the state we pass to them

* Makes state **shareable, interceptable, decoupled**

---

<img src="slides/images/stateful_vs_stateless.png" width="1000">

---
<!-- .slide: data-scene="Slides" -->

* **Stateful** 🤓
  * Creates & manages its own state
  * When caller doesn't need to manage it
  * **Less reusable**
  * More frequent at the **root of the tree**

* **Stateless** 🤷‍♂️
  * Hoists its state
  * More reusable
  * **Shareable and interceptable state**

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 9: Mutable state exercise (`NameGenerator`)

---
<!-- .slide: data-scene="Slides" -->

#### **Smart recomposition**

* Compiler rewrites function IR
* Wraps restartable funcs into **restart groups**

```kotlin
@Composable // Transforms this...
fun A(x: Int) {
  f(x)
}
```
```kotlin
@Composable // ...into this
fun A(x: Int, $composer: Composer, $changed: Int) {
  $composer.startRestartGroup()

  // Runs f(x) or skips depeding on $changed

  $composer.endRestartGroup()?.updateScope {
      $composer: Composer ->
        A(x, $composer, $changed or 0b0001)
  }
}
```

---

#### **Teaches runtime how to skip / restart**

* When body doesn't read `State`, `endRestartGroup() == null`
* No need to teach runtime how to recompose
* Only re-executes **when state that is read varies** 👍🏾

```kotlin
@Composable fun A(x: Int, ...) {
  // ...
  $composer.endRestartGroup()?.updateScope {
      $composer: Composer ->
        A(x, $composer, $changed or 0b0001)
  }
}
```

---

<img src="slides/images/comparison_propagation.png" width="800">

---

#### **Comparison propagation**

Saves computation time and space (in slot table)

```kotlin
@Composable
fun A(x: Int, $composer: Composer, $changed: Int) {
  // ...
  var $dirty = $changed
  if ($changed and 0b0110 === 0) {
    $dirty = $dirty or if ($composer.changed(x)) 0b0010 else 0b0100
  }
  if ($dirty and 0b1011 xor 0b1010 !== 0 || !$composer.skipping) {
    f(x) // executes body
  } else {
    $composer.skipToGroupEnd() // skips!
  }
  // ...
}
```

---

#### **Smart recomposition**

* Avoid recomposing the entire UI

* Only components that changed

* More efficient than binding UI state with Views

---

#### **Smart recomposition**

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

`RecompositionButton` (content lambda) and `RecompositionText` recompose. **`RecompositionBox` does not.**

<img src="slides/images/recomposition.gif" width="400">

---

#### **Recomposition scopes**

* Recompose the smallest possible scope

<img src="slides/images/recomposition_scopes.png" width="1000">

---

#### **Recomposition scopes**

* Button **`content` lambda** (not the Button itself)

<img src="slides/images/recomposition_scopes2.png" width="1000">

---

#### **"Donut-hole skipping"**

[🍩 donut-hole skipping (Vinay Gaba)](https://www.jetpackcompose.app/articles/donut-hole-skipping-in-jetpack-compose)

<img src="slides/images/recomposition_scopes3.png" width="1000">

---

#### **Debugging recomposition**

[📝 ⚙ How to debug recomposition (Vinay Gaba)](https://www.jetpackcompose.app/articles/how-can-I-debug-recompositions-in-jetpack-compose)

[📝 ⚙ Composable metrics (Chris Banes)](https://chris.banes.dev/composable-metrics/)

[📝 ⚙ Debugging recomposition Ben Trengrove](https://medium.com/androiddevelopers/jetpack-compose-debugging-recomposition-bfcf4a6f8d37)

---

#### **Class stability**

* Input state **must be reliable (stable)**...
* ...so Compose knows whether it changed...
* ...and recompose or skip accordingly.

---

#### A **stable** class

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
* Compose can be certain when it changes
* `PersonView` calls **can be skipped** if it didn't

---

#### An <span class="error">unstable</span> class

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
* Always recompose 👉 performance 📉🙈

---

#### **Use immutability** 🙏🏿

* Especially for UI state
* <span class="error">If not:</span>
  * Removes any chance of runtime optimization
  * Opens the door to bugs and race conditions

---

#### Another <span class="error">unstable</span> class

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

* 🚨️ Collections can be <span class="error">mutable</span> (impl)
* Compose defaults to <span class="error">unstable</span> for safety

---

#### **Class stability**

* Compose compiler **can infer** class stability 🧠
* Flags classes (& properties) as stable/unstable
* **Not all the cases can be inferred**

---

#### **Aiding the compiler**

* Compiler can't infer **how** our code is used
  * Mutable data structure w/ immutable public api
  * Only using immutable collection impls
  * ...
* We can let it know explicitly 👇
  * Use **`@Stable`** or **`@Immutable`**

---

#### **`@Stable`**

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

#### **`@Stable`**

This test passes ✅

```kotlin
@Test
fun `mutation does not affect equals comparison`() {
    val state1 = MyScreenState("Screen 1")
    val state2 = MyScreenState("Screen 1")
    state2.isLoading = true // defaults to false

    assertThat(state1, `is`(state2))
}
```

* Only `screenName` is compared
* Compose is notified of any change 👍
* `String` and `MutableState` are **`@Stable`**

---

#### **`@Immutable`**

* All public props will not change after construction
* Compose can detect changes easily
* **`@Immutable` implies `@Stable`**
* **`@Stable` does not imply `@Immutable`** 👉 stable class can hold mutable data but still notify changes

```kotlin
@Immutable
data class Conference(val talks: List<Talk>)

@Stable // cannot be @Immutable
data class MyScreenState(val screenName: String) {
    var isLoading: Boolean by mutableStateOf(false)
    var content: User? by mutableStateOf(null)
    var error: String by mutableStateOf("")
}
```

---
<!-- .slide: data-scene="Slides" -->

#### **How to know if I need them?**

* [📝 Measure Compose Compiler metrics](https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md)

* Look for funcs **restartable but not skippable** 🤔

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 10: Debug recomposition when using list as input (unstable). Fix it by replacing the `List` with an `@Immutable` wrapper

---
<!-- .slide: data-scene="Slides" -->

#### **Snapshots** 📸

---

#### **Snapshot state**

* **Isolated** state that can be remembered and observed for changes
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

#### **Snapshot** State 📸

* Obtained from apis like 👇
  * `mutableStateOf`
  * `mutableStateListOf`
  * `mutableStateMapOf`
  * `derivedStateOf`
  * `produceState`
  * `collectAsState`
  * ...

---

#### **Why to isolate State?** 🤔

---

#### **Concurrency**

* Offloading composition to **different threads**
* **Parallel composition**
* **Reordering compositions**
* No guarantees that our Composable will execute on a specific thread 🤷‍♀️

---

Write to state **from a different thread** 😲

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

#### **2 approaches** ✌🏼

* **Immutability** 👉 safe for concurrency
* **Mutability + isolation** 👉  Each thread maintains its own copy of the state. Global coordination needed to keep **global program state coherent**

---

#### **In Compose**

* **Mutable state** 👉  **observe changes**
* Work with mutable state across threads
* Isolation + propagation needed

---

#### **The Snapshot State system**

* Models and coordinates **state changes** and **state propagation**
* Part of the Jetpack Compose runtime
* Decoupled 👉 Could be used by other libraries

---

#### **Taking a snapshot 📸**

* A "picture" of our app state at a given instant
* **A context for our state reads**

```kotlin
var name by mutableStateOf("")
name = "Aleesha Salgado"
val snapshot = Snapshot.takeSnapshot()
name = "Jessica Jones"

println(name) // Jessica Jones
snapshot.enter { println(name) } // Aleesha Salgado 👈
println(name) // Jessica Jones
```

---

#### **Modifying state in a snapshot**

* `Snapshot.apply()` 👉 **propagate changes to other snapshots** ✨

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

#### **Nested snapshots**

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

<img src="slides/images/snapshot_tree.png" width=1000 />

---

#### **Snapshots in Compose** 🤔

* `Composition` created 👉 `GlobalSnapshot` created to hold **global state**
* `Recomposer` hooks listener for changes propagated to it from child snapshots

<img src="slides/images/snapshot_state1.png" width=600 />

---

#### **Snapshots in Compose**

* For initial composition & every recomposition

<img src="slides/images/snapshot_state2.png" width=600 />

---

#### **Snapshots in Compose**

* Composition / recomposition **can occur in any thread** 🤷‍♂️
* Snapshot propagation deals with this 👍👍
* Inspired in MVCC [multiversion concurrency control](https://en.wikipedia.org/wiki/Multiversion_concurrency_control)

---

#### **`Recomposer`**

* Keeps track of changes propagated by children
* Runs **recomposition loop** 👉 listen for invalidation of compositions registered with it
* Awaits for a frame from **monotonic clock ⏰ to coalesce all the pending changes** and trigger recompositions
