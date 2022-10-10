## **1. Essentials**

---

#### Compose **architecture**

<img src="slides/images/compose_architecture.png" width=400 />

---

<img src="slides/images/title_compiler.png" width=200 style="margin-top:-100px;" />
<br/>

* 🤓 Analyzes the sources
* 🔍 Looks for **`@Composable`**
* ✅ Static checks
* ✍️ Generates code (replace IR)

---

<img src="slides/images/title_material.png" width=200 />
<br/>

* 🖍 Material **themes**
* 🧩 Material **components** (`Button`, `Text`, `FAB`, `TopAppBar`, `BottomNavigation`...)

<img src="slides/images/material.png" width=300 />

---

<img src="slides/images/title_foundation.png" width=200 style="margin-top:-100px;" />
<br/>

* 📦 **Generic** components (`Box`, `Row`, `Column`, `BasicText`, `BasicTextField`...)
* 🎨 Create **design systems** on top (material)

---

<img src="slides/images/title_ui.png" width=200 style="margin-top:-100px;" />
<br/>

* 📐 **`Layout`** Composable (measure, place)
* ✂️ Modifier system
* 👭 Multiplatform lib 👉 [Android & Desktop](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui/src/)
* 🤝 Integration with the platform (Android: `View` interop, accessibility, keyboard)

---

<img src="slides/images/title_runtime.png" width=200 style="margin-top:-100px;" />
<br/>

* 🔄 Composition / recomposition
* 🧠 Smart recomposition
* 🎞 The state snapshot system
* 🌻 Automatically reacting to state updates
---

#### **Composable** functions

<img src="slides/images/composable_function.png" width=400 />

---
<!-- .slide: data-scene="Slides" -->

<img src="slides/images/composable_function_input_output.png" width=800 />

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 1: Our first Composable function

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 1b: Showcase Compose Previews

---
<!-- .slide: data-scene="Slides" -->

#### **Properties** of Composable functions

* Expected by the runtime
* Unlock runtime optimizations

---

#### **1. Calling context**

```kotlin
// Compiler rewrites this
@Composable
fun NamePlate(name: String) {
  Text(text = name)
}
```
```kotlin
// Into this
@Composable
fun NamePlate(name: String, $composer: Composer) {
  $composer.start(123) // Unique key generated
  Text(text = name, $composer)
  $composer.end()
}
```

* `Composer` injected in all Composable calls
* Forwarded down the tree ⏬

---

<img src="slides/images/calling_context.png" width=900 />

---

#### The **composer**

* ⛓ Connects the code we write with the runtime
* 🗣 Informs the runtime about the shape of the tree by executing the Composable functions

---

#### **2. Idempotent**

* Executing a Composable multiple times should produce **same result** (if its inputs have not changed)
* The Composition should not vary as a result
* **Consistency**

---

**Why** would it execute multiple times?

---

**(Infix) recomposition**

<img src="slides/images/recomposition.png" width=900 />

---

<img src="slides/images/composition_recomposition_Composition.png" width=900 />

---

**(Infix) other reasons to re-execute**

* Always re-execute (idempotence):
  * Composables not returning `Unit` (not skippable)
  ```kotlin
  val a = remember { heavyCalculation() }
  ```
  * Unstable (unreliable) inputs

---

#### **3. Free of side effects**

* Avoid side effects in Composable functions
* ⚠️ Can execute multiple times
* ⚠️ Cannot control when it runs
* ⚠️ Cannot dispose / cancel it
* ⚠️ Risk of relation of order
* ⚠️ Risk of concurrency

```kotlin
@Composable
fun SpeakersFeed(networkService: SpeakersService) {
  val speakers = networkService.loadSpeakers() // side effect
  Column {
    speakers.forEach { SpeakerCard(it) }
  }
}
```

---

#### **3. Free of side effects**

Example of the risk of relation of order

```kotlin
@Composable
fun MainScreen() {
  Header() // sets some external state
  ProfileCard() // reads from it ⚠️
  Detail()
}
```

They can run in any order or in parallel 🤷‍

---

#### **3. Free of side effects**

* Example of the risk of concurrency
* Setting external var holding state
* Can run from different threads 👉 accessing var **not thread-safe**

```kotlin
@Composable
fun EventFeed(events: List<Event>) {
  var count = 0

  Column {
    Text(if (count == 0) "No events." else "$count")
    events.forEach { event ->
      Text("Item: ${event.name}")
      count++ // 🔥
    }
  }
}
```

---

#### **4. Restartable**

<img src="slides/images/restartable1.png" width=900 />

---

#### **4. Restartable**

<img src="slides/images/restartable2.png" width=900 />

---

#### **4. Restartable**

* Compiler generates code to enable this

* Teaches the runtime how to restart (re-execute) the function

---

#### **5. Fast execution**

* Declarative style 👉 describe UI

* Designed to emit changes, not making them

* Exec only writes data to a structure (Composition)

* Materializing the changes is decoupled

---

<img src="slides/images/fastexecution.png" width=900 />

---

#### **6. Positional memoization**

* "Function memoization" 👉 ability of a function to **cache its result based on its inputs**

* Composable functions have knowledge about **their location in the sources**

* Compose assigns them unique ids based on that

```kotlin
@Composable
fun MyComposable() {
  Text("Hello") // id1
  Text("Hello") // id2
  Text("Hello") // id3
}
```

---
<!-- .slide: data-scene="Slides" -->
#### **6. Positional memoization**

<img src="slides/images/positionalmemoization1.png" width=800 />

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 2: Button and user interaction

---
<!-- .slide: data-scene="Slides" -->

#### The **Slot Table**

* In-memory structure
* Stores **the data of the Composition**
* A trace of everything that happened during composition
* Optimized for **linear access**
* Gap buffer

---

<img src="slides/images/slottable1.png" width=1000 />

---

<img src="slides/images/slottable2.png" width=1000 />

---

<img src="slides/images/slottable3.png" width=1000 />

---

<img src="slides/images/slottable4.png" width=1000 />

---

#### The **Slot Table**

* Ops done via injected `$composer`

* get, move, insert, delete 👉 **constant time**

* moving the gap 👉 **O(n)**

* 💡 Comp hierarchies **rarely change in structure**, mostly in terms of values

---

#### **Slot Table** with code

Compiler translates this

```kotlin
@Composable
fun Counter() {
 var count by remember { mutableStateOf(0) }
 Button(text="Count: $count", onPress={ count += 1 })
}
```

into

```kotlin
fun Counter($composer: Composer) {
 $composer.start(123)
 var count by remember($composer) { mutableStateOf(0) }
 Button(
   $composer,
   text="Count: $count",
   onPress={ count += 1 },
 )
 $composer.end()
}
```

---
<!-- .slide: data-scene="Slides" -->

<img src="slides/images/slottable5.png" width=1000 />

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 3

* Scaffold and TopAppBar

* Adding logic to our Composable (conditions, control flow)

* Creating a list of elements. Column and scrolling modifier

---
<!-- .slide: data-scene="Slides" -->

#### **Integration points**

`ComponentActivity.setContent`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAndInternalsTheme {
                // Composables
            }
        }
    }
}
```

**Adds a `ComposeView`** at the root of the `View` hierarchy (`android.R.id.content`)

---

#### **Integration points**

`ComposeView`

```kotlin
class ExampleFragment : Fragment() {
    override fun onCreateView(...): View {
        return ComposeView(requireContext()).apply {
            // Dispose Composition when view's
            // LifecycleOwner destroyed
            setViewCompositionStrategy(
              DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    Text("Hello Compose!")
                }
            }
        }
    }
}
```

`ViewGroup` (Composables into Views)

---

#### **Integration points**

ComposeView within layout

```xml
<...>
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/greeting"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
</...>
```

```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        val greeting = findViewById(R.id.greeting)
        greeting.setContent {
            MdcTheme { // or AppCompatTheme
                Greeting()
            }
        }
    }
}
```

A `ViewGroup`. Adding Composables to Views.

---

#### **Integration points**

ComposeView in RecyclerView

```kotlin
class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(...): MyViewHolder {
        return MyViewHolder(ComposeView(parent.context))
    }

    override fun onBindViewHolder(...) {
        holder.bind("$position")
    }
}

class MyViewHolder(
    val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {
    fun bind(input: String) {
        composeView.setContent {
            MdcTheme {
                Text(input)
            }
        }
    }
}
```

---

#### **ViewCompositionStrategy**

DisposeOnDetachedFromWindowOrReleasedFromPool
DisposeOnDetachedFromWindow
DisposeOnLifecycleDestroyed
DisposeOnViewTreeLifecycleDestroyed

---

<img src="slides/images/composeview_setcontent.png" width=800 />

---
<!-- .slide: data-scene="Slides" -->

#### Sharing UI between **Compose and Views**

```kotlin
class CallToActionViewButton @JvmOverloads constructor(
    ...
) : AbstractComposeView(context, attrs, defStyle) {

    var text by mutableStateOf<String>("")
    var onClick by mutableStateOf<() -> Unit>({})

    @Composable
    override fun Content() {
        YourAppTheme {
            CallToActionButton(text, onClick)
        }
    }
}
```

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 4: Making the list dynamic and lazy. LazyColumn and LazyRow
