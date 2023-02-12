<!-- .slide: data-scene="Slides" -->
## **3. Composition.**

---

#### **Conditional Composition**

```kotlin
if (maxWidth < 560.dp) {
    MyPhoneUi()
} else {
    MyTabletUi()
}
```

* What if condition value **not yet available**?
* E.g: Available space unknown until layout phase
* **Can't do with custom layout** 👉 too late 🤔

---

#### **A solution**

* **Delay Composition** until data is available
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
<!-- .slide: data-scene="Slides" -->

#### **`BoxWithConstraints`**

* Delays composition until measure/layout phase

* **Not as efficient** as normal Composition ⚠️

* Achieved via **Subcomposition** ✨

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 7: Use BoxWithConstraints to write our Composable

---
<!-- .slide: data-scene="Slides" -->

#### **Subcomposition**

* Creator of a subcomposition controls when initial composition process happens
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

#### **Composition trees** 🌲

* Compositions are connected as a tree

* Subcomposition is a **child composition**

* **Independent invalidation** for a subtree 🔄

* Allows to change node type (not only `LayoutNode`)

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
* Some of them built-in in Compose UI

```kotlin
@Composable
fun FruitText(fruitSize: Int) {
  // Access host context via LocalContext CompositionLocal
  // Use `current` to access its current value
  val resources = LocalContext.current.resources

  val fruitText = resources
    .getQuantityString(R.plurals.fruit_title, fruitSize)

  Text(text = fruitText)
}
```

---

<img src="slides/images/composition_locals.png" width=900 />

---

#### **Themes are `CompositionLocal`s** 😲

* `MaterialTheme` provides `LocalColors`, `LocalShapes`, `LocalTypography`

```kotlin
@Composable
fun MyApp() {
  MaterialTheme { // local values provided
    // Composables here
  }
}

// Somewhere deep in the hierarchy
@Composable
fun SomeTextLabel(labelText: String) {
  Text(
    text = labelText,
    // val colors: Colors get() = LocalColors.current
    color = MaterialTheme.colors.primary
  )
}
```

---

#### **Overriding**

```kotlin
@Composable fun CompositionLocalExample() {
  MaterialTheme { // Sets ContentAlpha.high as default
    Column {
      Text("Uses ContentAlpha.high")
      CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text("Uses ContentAlpha.medium")
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
  Text("Uses ContentAlpha.disabled")
}
```

---

<img src="slides/images/composition_locals2.png" width=500 />

---

#### **Custom `CompositionLocal`?**

* Not recommended 🙅🏼
* Composables harder to reason about
* Callers need to ensure values are provided
* Harder debugging 👉 (not a single source of truth)
* Do only for very good reasons
