The ultimate Jetpack Compose online course. Created and delivered by [Jorge Castillo](https://twitter.com/jorgecastillopr), author of Jetpack Compose internals. Take this 10 hours course to master Jetpack Compose and learn how to work efficiently with it by acquiring a good understanding of its internals.

## Can I take this course?

* 🟢 Compose Level: Beginner
* 🟡 Android Level: Medium
* 🟡 Kotlin Level: Medium (suspend + Coroutines)
* ⏱ Duration: 10 hours ⏱
* 🗓 Next edition: October 15-16 (5 hours a day, 5:00 PM - 10:00 PM CET)
* 💰 Price: 750€

## End goal

This course takes you further and deeper than any other existing Jetpack Compose course. It has been consciously crafted so previous knowledge of the library is not needed, only some experience in Android development. The course starts from the library essentials and guides you gently towards the most advanced and efficient use of the library. This makes it a perfect fit for teams or individuals wanting to learn and master Jetpack Compose.

## Internals 🧐

I want you to learn how to use Compose in professional Android projects and also understand its internals while you are at it, so you know what the library expects from you to use it well and grow the correct mindset. Each lesson comes along with deep dives into the internals of the library.

## Why to attend

Jetpack Compose is the new de facto standard of UI development in Android. Taking this course will make you achieve a big leap forward in what is related to Android UI and how to integrate it perfectly in any modern architecture.

This course is also a very good option for teams wanting to migrate their codebases from Views to Composables, or to work with Jetpack Compose in a greenfield project or feature.

## Author / instructor 👨‍💻

<img align="left" style="margin-right:20px" src=".slides/images/twitteravatar.png">

I'm [Jorge Castillo](https://twitter.com/jorgecastillopr) 👋 Android & Kotlin **Google Developer Expert**, currently working at Twitter. I am the author of [Jetpack Compose Internals 📖](https://leanpub.com/composeinternals/). I created this course and I will be your instructor.

I have extensive experience in online courses, having created and delivered courses about diverse topics like Kotlin, Functional Programming and Jetpack Compose to name a few. I am a very active member of the Android community.

During my career as an Android developer I have noticed that reading sources and understanding internals implies a huge leap forward in knowledgeability. For this reason I decided to write Jetpack Compose internals and create this course.

## Course Outline

(Still might change a bit)

#### 1. Essentials

Writing our first Composable function, understanding how it works from the point of view of the Compose compiler and runtime.

- The Compose architecture (ui, foundation, material / runtime / compiler)
- Our first Composable function, `Box` and `Text`
- Introducing Composable previews
- Composable functions in-depth. What they model, their properties
- `@Composable` from the compiler perspective. Ensuring a calling context
- Runtime optimizations enabled by the compiler
- Adding a `Button` and user interaction
- The initial composition process
- Emitting nodes into the Composition to build up the tree
- The Composition and the slot table
- `Scaffold` and `TopAppBar`
- Adding logic to our Composable (conditions, control flow)
- How the runtime "materializes" the UI tree
- Creating a list of elements. `Column` and `Row`. Scrolling modifiers
- Making the list dynamic and lazy. `LazyColumn` and `LazyRow`
- Adding some padding to our UI
- Integration points for Jetpack Compose in Android

#### 2. Advanced UI

Modifiers in-depth, layouts, measuring and drawing, intrinsics, Composition and Subcomposition.

- The Modifier system. Order of precedence. How it is modeled in the runtime
- Our first custom layout. The Layout Composable
- Measure/layout pass in-depth.
- Intrinsics
- Drawing in Jetpack Compose. Canvas, RenderNodes
- Conditional Composition based on the available space via `BoxWithConstraints`
- Delaying Composition via `SubcomposeLayout`. Subcomposition
- Composition trees. How Compositions are connected
- CompositionLocals. How they are propagated from parent to child Compositions
- Vectors in Compose
- Node types used by the runtime. `LayoutNode` and `VNode`.
- The `LayoutNode` tree and the concept of `Owner`
- How changes to add, move, replace, or remove nodes from tree are materialized
- The different types of `Applier`s provided by client libraries
- Theming. Material and custom themes. Making our app material
- Writing our first animation
- Advanced animations
- Drag and swipe gestures

#### 3. State management

Deep dive into the Compose state snapshot system.

- Composable functions as functions from input (data) to output (emitted UI)
- Adding mutable state to our app. Reading from and writing to state
- Automatically reaction to state changes to reflect the most up to date state on UI
- Snapshot state in Compose. How it works, the MVCC system
- State comparison strategies
- Data stability. Class stability inference. Aiding the compiler
- Recomposition. Updating the data stored in the Composition
- Smart recomposition. Skipping Composables whenever possible
- Aiding recomposition with additional metadata. The `key` Composable
- Positional memoization. Uniquely identifying Composables
- Internals of Recomposition. RecompositionScopes in the runtime
- State holders
- Integration with AAC ViewModel
- Stateful vs Stateless Composables. Root level stateful vs dumb highly reusable stateless Composables
- State hoisting. State down, events up
- Saving and restoring state
- Surviving config changes and process death

#### 4. 🌀 Effect handling and Composable lifecycle 🍂

The lifecycle of a Composable and how it fits within the Android lifecycle. Constraining effects by the Composable lifecycle.

- What is an effect, why it needs to stay under control
- The Composable lifecycle. Entering and leaving the Composition
- Constraining effects by the Composable lifecycle
- The Compose effect handlers
- How the lifecycle is modeled in the Compose runtime
- How effects are modeled and dispatched by the runtime. The order of dispatch
- Adding different types of side effects to our app
- Where to write effects. Composable function body vs StateHolder vs AAC ViewModel
- Keeping effects testable / isolated
- How the Composable lifecycle events map to the host `Activity` or `Fragment` lifecycle
- Optimizing the Composable lifecycle for Composables within a `RecyclerView`

#### 5. Architecture around Compose, accessibility, testing

Leveraging UDF (Unidirectional Data Flow). Integrating Compose with modern architectures. Keeping everything testable.

- A mind shift. From imperative to declarative UI
- Binding data. Modeling UI state. Making UI state exhaustive
- Unidirectional data flow, mapping / transforming flows of events from application to UI state
- Integrating `State` with 3rd party observable data types (`LiveData`, `StateFlow`, RxJava types)
- Interoperability with the View system in both directions. `ComposeView` / `AndroidView`
- Compose Navigation
- Adding navigation to our app
- Single Activity (all Compose) vs Fragments with Compose
- Dependency injection in Composable functions. Scoping
- Semantic trees. Merged and unmerged
- Merging policies
- Adding semantics to our Composables
- How semantics are handled / wired in Android
- Tools leveraging the semantic trees
- UI testing our Composables
- Screenshot testing our Composables. Shot, Paparazzi, Showkase
- Headless UI tests

#### 6. Advanced Compose use cases

- Case Study: Creating a client library for the Compose compiler and runtime
- Supporting new types of nodes / `Applier`s.
- Intro to Compose multiplatform
- Intro to Compose for Desktop
- Intro to Compose for Web


## 🏆 Rewards

At the end of the course every attendee will get:
* A course certificate (signed by me)
* Lifelong access to all formats and updates of the [Jetpack Compose Internals book 📖](https://leanpub.com/composeinternals/)
* The course slides, exercises, and sources
* Access to the Effective Android Slack community, where we can keep discussing the course content and any other relevant topics regarding modern Android development

## ☑️ Requirements

* Latest Android Studio stable version installed.
