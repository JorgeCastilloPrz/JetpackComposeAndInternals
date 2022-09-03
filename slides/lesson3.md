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

#### Composition trees

* Subcomposition is a **child composition**

* Compositions are connected as a tree

