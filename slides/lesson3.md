<!-- .slide: data-scene="Slides" -->
## **3. Composition**

---

#### **Conditional Composition**

* Different Composables depending on some value that is **not yet available**

* E.g: phone/tablet UI depending on available space

```kotlin
if (maxWidth < 560.dp) { // not available in this phase
    MyPhoneUi()
} else {
    MyTabletUi()
}
```

* **Can't do with custom layout** 👉 we are still building up the Composition!

---

#### A **solution** 🤩

* **Delaying Composition** until data is available
* **`BoxWithConstraints`** 👇

```kotlin
BoxWithConstraints { // BoxWithConstraintsScope
    if (maxWidth < 560.dp) {
        MyPhoneUi()
    } else {
        MyTabletUi()
    }
}
```

```kotlin
@Stable
interface BoxWithConstraintsScope : BoxScope {
    val constraints: Constraints
    // Direct access in Dp
    val minWidth: Dp
    val maxWidth: Dp
    val minHeight: Dp
    val maxHeight: Dp
}
```

---

#### **BoxWithConstraints**

* Doesn't compose children during composition

* Delays it until measure/layout phase

* **It is not as efficient** ⚠️

* This is done via ✨ **Subcomposition** ✨

---

#### **Subcomposition**

* Creator of a subcomposition can control when the initial composition process happens
* Done via **`SubcomposeLayout`**

```kotlin
@Composable
fun BoxWithConstraints(...) {
  val measurePolicy = rememberBoxMeasurePolicy(...)

  // Delays subcomposition until layout phase
  SubcomposeLayout(modifier) { constraints ->
    val scope = BoxWithConstraintsScopeImpl(
      this,
      constraints
    )
    val measurables = subcompose(Unit) { scope.content() }
    with(measurePolicy) { measure(measurables, constraints) }
  }
}
```

---

#### **SubcomposeLayout**

* Analogue of `Layout` that creates and runs an independent composition during the layout phase

---

#### **Composition trees** 🌲

* Subcomposition is a **child composition**

* Created inline for **independent invalidation** 🔄

* Allows to change node type (not only `LayoutNode`)

* Compositions are connected as a tree

---

<img src="slides/images/composition_trees.png" width=550 />

---

#### **Why?**

* **State change propagation** must be notified to child compositions also (they might need to recompose)

* **Propagating CompositionLocals** down the tree, so they are accessible from child compositions also

---

#### **`CompositionLocal`**

* Make **implicit** params available to a subtree

* Avoids flooding function params transitively

* Implicit DI scoped to a subtree

* Some of them built-in in Compose UI

```kotlin
@Composable
fun FruitText(fruitSize: Int) {
  // Access the host context
  val resources = LocalContext.current.resources

  val fruitText = resources
    .getQuantityString(R.plurals.fruit_title, fruitSize)

  Text(text = fruitText)
}
```

---

<img src="slides/images/composition_locals.png" width=900 />

---

#### `CompositionLocal` **in themes**

* `MaterialTheme` provides `LocalColors`, `LocalShapes`, `LocalTypography`

```kotlin
@Composable
fun MyApp() {
  // Provides a Theme whose values are propagated down
  MaterialTheme {
    // Local values for colors, typography, shapes
  }
}

// Somewhere deep in the hierarchy
@Composable
fun SomeTextLabel(labelText: String) {
  Text(
    text = labelText,
    // primary obtained from LocalColors CompositionLocal
    color = MaterialTheme.colors.primary
  )
}
```

---

#### `CompositionLocal` **by Material**

* **material** provides some built-in ones

* `LocalAbsoluteElevation`
* `LocalContentAlpha`
* `LocalContentColor`
* `LocalElevationOverlay`
* `LocalMinimumTouchTargetEnforcement`
* `LocalTextStyle`

---

#### Overriding **`CompositionLocal`**

```kotlin
@Composable
fun CompositionLocalExample() {
  MaterialTheme { // Sets ContentAlpha.high as default
    Column {
      Text("Uses default provided alpha")
      CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text("Uses medium alpha")
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
          DescendantExample()
        }
      }
    }
  }
}
```
```kotlin
@Composable
fun DescendantExample() {
  // Also works across composable functions
  Text("This Text uses the disabled alpha now")
}
```

---

<img src="slides/images/composition_locals2.png" width=500 />

---

#### **Custom** `CompositionLocal`

* Not recommended 👉 Composables harder to reason about

* Callers need to ensure the values are provided

* Harder debugging 👉 Not a single source of truth for its value (can be overriden)

---

