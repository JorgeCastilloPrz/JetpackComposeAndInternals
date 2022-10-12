<!-- .slide: data-scene="Slides" -->
## **2. Advanced UI**

---

#### **Modifiers** 🛠

Tweak how a Composable **looks and behaves**

```kotlin
Image(
  painter = painterResource(R.drawable.avatar_1),
  contentDescription = "user 1 avatar",
  contentScale = ContentScale.Crop,
  modifier = Modifier
    .padding(16.dp)
    .size(102.dp)
    .shadow(elevation = 8.dp, clip = true, shape = CircleShape)
    .clickable { onAvatarClick() }
)
```

<img src="slides/images/modifiers.gif" width=200 />

---

#### ⚠️ **Order of precedence**

```kotlin
// Move clickable modifier from
modifier = Modifier
    .padding(16.dp)
    .size(102.dp)
    .shadow(elevation = 8.dp, shape = CircleShape)
    .clickable { onAvatarClick() } // 👈

// To
modifier = Modifier
    .clickable { onAvatarClick() } // 👈
    .padding(16.dp)
    .size(102.dp)
    .shadow(elevation = 8.dp, shape = CircleShape)
```

<img src="slides/images/modifiers.gif" width=200 />
👉
<img src="slides/images/modifiers2.gif" width=200 />

---

#### **Multiple types**

* layout
* alignment
* draw (alpha, bg, clip, canvas, indication, shadows)
* focus
* graphics layer (efficient / granular redraw)
* border
* animations
* semantics (test / tooling / accessibility)
* interactions (click, scroll, drag, zoom, select, swipe)
* padding
* **...**

---

#### **internals** 🕵️‍♀️

```kotlin
interface Modifier {
  fun <R> foldIn(initial: R, operation: (R, Element) -> R): R
  fun <R> foldOut(initial: R, operation: (Element, R) -> R): R

  fun any(predicate: (Element) -> Boolean): Boolean
  fun all(predicate: (Element) -> Boolean): Boolean

  infix fun then(other: Modifier): Modifier =
    CombinedModifier(this, other) // chaining
}
```

☝ ️Extended by all `Modifier`s

---

#### **`CombinedModifier`**

* Linked list
* `outer` (head / current)
* `inner` (`CombinedModifier`)

```kotlin
class CombinedModifier(
  val outer: Modifier,
  val inner: Modifier
) : Modifier {
  override fun <R> foldIn(initial: R, operation: (R, Modifier.Element) -> R): R =
    inner.foldIn(outer.foldIn(initial, operation), operation)

  override fun <R> foldOut(initial: R, operation: (Modifier.Element, R) -> R): R =
    outer.foldOut(inner.foldOut(initial, operation), operation)

  override fun any(predicate: (Modifier.Element) -> Boolean): Boolean =
    outer.any(predicate) || inner.any(predicate)

  override fun all(predicate: (Modifier.Element) -> Boolean): Boolean =
    outer.all(predicate) && inner.all(predicate)
}
```

---

#### **Modifier chain**

<img src="slides/images/modifiers3.png" width=1000 />

---

#### **Reusing modifiers**

* When emitting a layout
* Creates a new chain of modifiers that **reuses as many as possible from the previous chain**

```kotlin
@Composable fun MyBox() {
    // these always stay the same
    val modifier = Modifier
        .padding(16.dp)
        .size(102.dp)
        .shadow(elevation = 8.dp, shape = CircleShape)

    // this one might not be there yet
    if (clickable) {
        modifier.clickable { onAvatarClick() }
    }

    Box(modifier) { ... }
}
```

---

#### **custom modifiers**

* Use `layout` modifier to modify how **a single element** is measured and laid out (placed)
* Let's create a custom `layout` modifier

```kotlin
fun Modifier.customLayoutModifier(...) =
  this.layout { measurable, constraints ->
    ...
  })
```

---

#### **custom modifiers**

<img src="slides/images/custom_modifiers.png" width=400 />

```
fun Modifier.takeHalfParentWidthAndCenter(): Modifier =
  this.layout { measurable, constraints ->
    val maxWidthAllowedByParent = constraints.maxWidth
    val placeable = measurable.measure(
      constraints.copy(minWidth = maxWidthAllowedByParent / 2))

    layout(placeable.width, placeable.height) {
      placeable.placeRelative(
        maxWidthAllowedByParent / 2 - placeable.width / 2,
        0
      )
    }
  }
```

---
<!-- .slide: data-scene="Slides" -->

#### **custom modifiers**

```kotlin
Box(Modifier.fillMaxWidth().background(Color.Yellow)) {
    Button(
      modifier = Modifier.takeHalfParentWidthAndCenter(),
      onClick = {}
    ) {
        Text("Hello world!")
    }
}
```

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 5: Writing a custom layout modifier

---
<!-- .slide: data-scene="Slides" -->

#### **Custom layouts**

* Use `Layout` Composable to measure and layout **multiple Composables**
* All UI Composables are layouts

```kotlin
@Composable
fun MyCustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children
    }
}
```

---

```kotlin
@Composable
fun StairedBox(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Layout(modifier, content) { measurables, constraints ->
    // measure children, don't constraint them further
    val placeables = measurables.map { measurable ->
      measurable.measure(constraints)
    }

    layout(constraints.maxWidth, constraints.maxHeight) {
      // Track the x and y coord
      var xPosition = 0
      var yPosition = 0
      // place children
      placeables.forEach { placeable ->
        placeable.placeRelative(
          x = xPosition,
          y = yPosition
        )

        xPosition += placeable.width
        yPosition += placeable.height
      }
    }
  }
}
```

---

```kotlin
StairedBox {
    Text("Text 1")
    Text("Text 2")
    Text("Text 3")
    Text("Text 4")
    Text("Text 5")
    Text("Text 6")
}
```

<img src="slides/images/custom_layout.png" width="400">

---

<img src="slides/images/compose_phases.png" width="600">

---

#### **Measuring**

---

#### **The LayoutNode tree** 🌲

* `Layout` emits a node of type **`LayoutNode`**
* A representation of the node in memory

<img src="slides/images/layout_node_tree.png" width="600">

---

<img src="slides/images/request_remeasure1.png" width="700">

---

<img src="slides/images/request_remeasure2.png" width="900">

---

<img src="slides/images/request_remeasure3.png" width="900">

---

<img src="slides/images/request_remeasure4.png" width="600">

---

<img src="slides/images/request_remeasure5.png" width="800">

---

#### **Measure & layout delegates**

<img src="slides/images/layoutnodewrappers.png" width="800">

---

🚨 **Layout modifiers** also affect measure / layout 🚨

```kotlin
Box(
  modifier = Modifier
    .padding(16.dp) // layout modifier
    .size(72.dp) // layout modifier
) {
    Image(...)
}
```

* Measure the node + its layout modifiers

---

<img src="slides/images/layoutnodewrappers2.png" width="800">

---

#### **`MeasurePolicy`**

* `LayoutNodeWrapper` relies on the provided `MeasurePolicy` to measure a node
* How to measure and place the node 📐 📌

```kotlin
Modifier.layout { measurable, constraints ->
  // measure and place the modified node
}

Layout(
    modifier = modifier,
    content = content
) { measurables, constraints ->
    // measure and place children
}
```

Must return a **`MeasureResult`**

---

#### **MeasureResult**

```kotlin
interface MeasureResult {
    val width: Int
    val height: Int
    val alignmentLines: Map<AlignmentLine, Int>
    fun placeChildren()
}
```

* Created via **`layout()`** call

```kotlin
fun Modifier.customLayoutModifier(...) =
  this.layout { measurable, constraints ->
    // Measure and place node
    //...
    layout(placeable.width, placeable.height) { // 👈
      // ...place node
    }
  })
```

* Same for `Layout` Composable


---

#### **Constraints**

* Measuring goes **from top to bottom** ⏬
* Parent imposes constraints to children
* **Also `Modifier.layout`**

```kotlin
Layout(
    modifier = modifier,
    content = content
) { measurables, constraints ->
    // 1. measure children using constraints
    // 2. call layout() passing total width / height
    // 3. place children
}
```

---

#### **Constraints**

* **minWidth** <= chosenWidth <= **maxWidth**
* **minHeight** <= chosenHeight <= **maxHeight**

```kotlin
value class Constraints(val value: Long) {
    val minWidth: Int
    val maxWidth: Int
    val minHeight: Int
    val maxHeight: Int
    val hasBoundedWidth: Boolean // if maxWidth != Infinity
    val hasBoundedHeight: Boolean // if maxHeight != Infinity
    val hasFixedWidth: Boolean // maxWidth == minWidth
    val hasFixedHeight: Boolean // maxHeight == minHeight

    fun fixed(width: Int, height: Int): Constraints
    fun fixedWidth(width: Int): Constraints
    fun fixedHeight(height: Int): Constraints
}
```

---
<!-- .slide: data-scene="Slides" -->

#### **Constraints**

* Example: `Spacer`

```kotlin
fun Spacer(modifier: Modifier) {
  // No measurables since Spacer has no children
  Layout({}, modifier) { _, constraints ->
    with(constraints) {
      val width = if (hasFixedWidth) maxWidth else 0
      val height = if (hasFixedHeight) maxHeight else 0

      layout(width, height) {}
    }
  }
}
```

* If no fixed dimensions, it takes no space (0)
* `Spacer` always needs constraints imposed, **via parent or layout modifier**

---
<!-- .slide: data-scene="Coding" -->

📝 Exercise 6: Go over `Box` measure policy together

---
<!-- .slide: data-scene="Slides" -->

#### **Intrinsics**

* Estimate child size **before it can be measured**
* E.g: Width of all children equal to widest one 👇

<video width="640" height="480" autoplay muted loop>
  <source src="slides/images/dropdown.mp4" type="video/mp4">
</video>

---

#### **Solution?**

* Measure twice? 💥 Compose throws `RuntimeException` (performance)

* Use intrinsics: Estimate size of layout **when constraints are not available** 👍🏾

---

#### **Intrinsics**

Intrinsic versions of `width` and `height` modifiers

```kotlin
// Part of the DropdownMenu Material Composable
@Composable
fun DropdownMenuContent(...) {
  Column(
    modifier = modifier
      .padding(vertical = DropdownMenuVerticalPadding)
      .width(IntrinsicSize.Max)
      .verticalScroll(rememberScrollState()),
    content = content
  )
  // ...
}
```

* Tells `Column` to measure children using the max intrinsic width among the children

---

<img src="slides/images/compose_phases2.png" width="600">

---

<img src="slides/images/drawing.png" width="800">

---

#### **Drawing** 👩‍🎨

* Each node:

  1. Offsets drawing to match layout definition

  2. Checks if **drawing layer** available, draws it

  3. Draws **`DrawModifiers`** in order (if available)

  4. Calls `draw()` on next wrapper

---

#### Drawing **layer** 👩‍🎨

* Used for **separate drawn content**

* Can be invalidated separately from parents

* Granularity, minimize invalidated content

* **`Modifier.graphicsLayer`**

---

#### Drawing **layer** 👩‍🎨

```kotlin
Modifier.graphicsLayer(
    alpha = alpha,
    translationX = translationX,
    translationY = translationY,
    shadowElevation = shadowElevation,
    scaleX = scaleX,
    scaleY = scaleY,
    rotationX = rotationX,
    rotationY = rotationY,
    rotationZ = rotationZ,
    cameraDistance = cameraDistance,
    transformOrigin = TransformOrigin(originX, originY),
    shape = roundedDegree,
    clip = true,
    renderEffect = BlurEffect(blur, blur)
)
```

---

#### Drawing **layer** 👩‍🎨

```kotlin
@Composable
fun AnimatedText() {
    val animatedAlpha = remember { Animatable(0f) }
    Text(
        "Hello World",
        Modifier.graphicsLayer {
            alpha = animatedAlpha.value
        }
    )
    // animations are suspend
    LaunchedEffect(animatedAlpha) {
        animatedAlpha.animateTo(1f)
    }
}
```

---

#### Drawing **layer** 👩‍🎨

```kotlin
Box(
    // blur is built on top of graphicsLayer
    modifier = Modifier.blur(2.dp),
    contentAlignment = Alignment.Center
) {
    Image(
        painter = painterResource(R.drawable.avatar_1),
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(102.dp)
    )
}
```

<img src="slides/images/blur.png" width=150 />

---

#### Drawing **layer** 👩‍🎨

* Also supports Android `RenderEffect`

```kotlin
@Immutable
internal class AndroidRenderEffect(
    val androidRenderEffect: android.graphics.RenderEffect
) : RenderEffect() {
    override fun createRenderEffect() = androidRenderEffect
}
```

---

#### Drawing **layer** 👩‍🎨

* Modifiers like `alpha`, `rotate`, `clip`, `scale`, `clipToBounds` are built on top of `Modifier.graphicsLayer`

* Use to update Composable **properties** efficiently

---

#### **Drawing layer types**

* Both **hardware accelerated** ⏭

* Transparent for user (decided by Compose)

  * **RenderNodeLayer:** Most efficient. `RenderNode`: draw once, redraw cheap multiple times

  * **ViewLayer:** Fallback when direct access to RenderNodes unsupported. Uses Android Views as holders of RenderNodes (hack)

---

#### **Draw modifiers**

* `alpha`, `rotate`, `clip`, `scale`, `clipToBounds`, `drawBehind`, `drawWithContent`...

---

#### **When are actual nodes drawn? 🤔**

* Everything is a `graphicsLayer` and / or `DrawModifier`s.
* E.g: `BasicText` is graphicsLayer + drawBehind

```kotlin
private fun Modifier.drawTextAndSelectionBehind(): Modifier =
    this.graphicsLayer().drawBehind {
      // graphicsLayer is an optimization
      //
      // Text is heavy to draw. Layer allows to avoid
      // redrawing it when nothing changed. If parent
      // needs to redraw, only layer gets redrawn, not
      // the text. A cached drawing of the text is used
    }
```

---

#### **(infix) `graphicsLayer` optimization**

* `LazyColumn` draws all children with an empty layer to avoid real redrawing when we need to just offset the items (cached drawings used on scroll)

* Compose adds layers where it makes sense 👉 **where drawing is expensive**

---

#### **More examples 🤔**

Surface is a Box with a Modifier.surface

```kotlin
fun Modifier.surface(shape: Shape,
    bgColor: Color, border: BorderStroke?,
    elevation: Dp
) = this.shadow(...)
    .then(if (border != null) Modifier.border(border, ...) else Modifier)
    .background(color = bgColor, shape = shape)
    .clip(shape)
```

---

#### **Canvas** 🖌

* `Canvas` abstraction (MPP - Android, Desktop)

* Android **delegates to native `Canvas`**

* More ergonomic api than native 👉 its functions do not accept `Paint` anymore, create once and reuse

---

#### **`@Composable Canvas()`** 🖌

```kotlin
Canvas(modifier = Modifier.fillMaxSize()) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    drawLine(
        start = Offset(x = canvasWidth, y = 0f),
        end = Offset(x = 0f, y = canvasHeight),
        color = Color.Blue
    )
}
```

Equivalent apis than native canvas

---

#### **Canvas via modifiers**

* Canvas is actually `Spacer` + `Modifier.drawBehind` 🤯

```kotlin
@Composable
fun Canvas(modifier: Modifier, onDraw: DrawScope.() -> Unit) =
    Spacer(modifier.drawBehind(onDraw))
```

---

#### **DrawScope**

* Gives access to all the drawing apis

* Available for `Canvas` and draw modifiers

```kotlin
Box(
    modifier = Modifier
        .size(100.dp)
        .drawBehind { // draw behind the Box
            drawRect(
                color = Color.Blue,
                size = size
            )
        }
)
```

---

#### **DrawScope**

* `Modifier.drawWithContent` to draw **behind or over content** 👇

```kotlin
internal fun Modifier.drawIndicatorLine(...): Modifier {
    // ...
    return drawWithContent {
        drawContent()
        val strokeWidth = strokeWidthDp.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            indicatorBorder.brush,
            Offset(0f, y), // start
            Offset(size.width, y), // end
            strokeWidth
        )
    }
}
```