## **4. Compose from the runtime**

---

#### An **agnostic runtime** 🤷🏾‍♀️

* Runtime works with **generic node type `N`**

* Supports any node type

* Node type picked by clients (e.g: Compose UI)

* Clients can feed multiple types (e.g: `LayoutNode`, `VNode`)

---

#### **Feeding the node type**

* Done when emitting the node
* Teaches the runtime **how to create & initialize** it

```kotlin
@Composable
fun Layout(modif: Modifier = Modifier, mp: MeasurePolicy) {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  val viewConfiguration = LocalViewConfiguration.current
  val materialized = currentComposer.materialize(modif)

  // LayoutNode is a child of ComposeUiNode
  ReusableComposeNode<ComposeUiNode, Applier<Any>>(
    factory = ComposeUiNode.Constructor,
    update = {
      set(mp, ComposeUiNode.SetMeasurePolicy)
      set(density, ComposeUiNode.SetDensity)
      set(layoutDirection, ComposeUiNode.SetLayoutDirection)
      set(viewConfiguration, ComposeUiNode.SetViewConfiguration)
      set(materialized, ComposeUiNode.SetModifier)
    },
  )
}
```

---

#### **`ComposeUiNode`** and **`LayoutNode`**

```kotlin
@PublishedApi
internal interface ComposeUiNode {
    var measurePolicy: MeasurePolicy
    var layoutDirection: LayoutDirection
    var density: Density
    var modifier: Modifier
    var viewConfiguration: ViewConfiguration
    // ...
}
```

Represents a Compose UI `Layout` for the runtime

```kotlin
internal class LayoutNode(...)) : ComposeUiNode, ...
```

---

<img src="slides/images/composition_trees.png" width=550 />

---

#### **VNode** (vectors)

Models a vector in Compose UI

```kotlin
sealed class VNode {
  internal class VectorComponent : VNode() { ... }
  internal class PathComponent : VNode() { ... }
  internal class GroupComponent : VNode() { ... }
}
```

<img src="slides/images/vnode.png" width=550 />

---

#### **Feeding the node type**

```kotlin
@Composable
fun Group(
    name: String = DefaultGroupName,
    rotation: Float = DefaultRotation,
    pivotX: Float = DefaultPivotX,
    pivotY: Float = DefaultPivotY,
    scaleX: Float = DefaultScaleX,
    scaleY: Float = DefaultScaleY,
    translationX: Float = DefaultTranslationX,
    translationY: Float = DefaultTranslationY,
    clipPathData: List<PathNode> = EmptyPath,
    content: @Composable @VectorComposable () -> Unit
) {
    ComposeNode<GroupComponent, VectorApplier>(
        factory = { GroupComponent() },
        update = {
            set(name) { this.name = it }
            set(rotation) { this.rotation = it }
            set(pivotX) { this.pivotX = it }
            set(pivotY) { this.pivotY = it }
            set(scaleX) { this.scaleX = it }
            set(scaleY) { this.scaleY = it }
            set(translationX) { this.translationX = it }
            set(translationY) { this.translationY = it }
            set(clipPathData) { this.clipPathData = it }
        }
    ) {
        content()
    }
}

@Composable
fun Path(
    pathData: List<PathNode>,
    pathFillType: PathFillType = DefaultFillType,
    name: String = DefaultPathName,
    fill: Brush? = null,
    fillAlpha: Float = 1.0f,
    stroke: Brush? = null,
    strokeAlpha: Float = 1.0f,
    strokeLineWidth: Float = DefaultStrokeLineWidth,
    strokeLineCap: StrokeCap = DefaultStrokeLineCap,
    strokeLineJoin: StrokeJoin = DefaultStrokeLineJoin,
    strokeLineMiter: Float = DefaultStrokeLineMiter,
    trimPathStart: Float = DefaultTrimPathStart,
    trimPathEnd: Float = DefaultTrimPathEnd,
    trimPathOffset: Float = DefaultTrimPathOffset
) {
    ComposeNode<PathComponent, VectorApplier>(
        factory = { PathComponent() },
        update = {
            set(name) { this.name = it }
            set(pathData) { this.pathData = it }
            set(pathFillType) { this.pathFillType = it }
            set(fill) { this.fill = it }
            set(fillAlpha) { this.fillAlpha = it }
            set(stroke) { this.stroke = it }
            set(strokeAlpha) { this.strokeAlpha = it }
            set(strokeLineWidth) { this.strokeLineWidth = it }
            set(strokeLineJoin) { this.strokeLineJoin = it }
            set(strokeLineCap) { this.strokeLineCap = it }
            set(strokeLineMiter) { this.strokeLineMiter = it }
            set(trimPathStart) { this.trimPathStart = it }
            set(trimPathEnd) { this.trimPathEnd = it }
            set(trimPathOffset) { this.trimPathOffset = it }
        }
    )
}
```

---

#### **List of changes**

* Nodes are not really inserted right away
* Emitting a node 👉 **records an operation to add it**
* The composition process creates a list of changes yet to be applied

<img src="slides/images/closing_the_circle_1.png" width=1000 />

---

#### **Types of changes**

* Insert nodes
* Remove nodes
* Move nodes (reorg children)
* Replace nodes
* Save values (`remember`)
...

---

#### **Applying the changes**

* Once composition is done (or recomposition), the list of changes is fed to the `Applier`.
* The **`Applier`** applies the changes 👉 materializes them

---

<img src="slides/images/closing_the_circle_2.png" width=1000 />

---

#### **Final step**

Invalidate owner to ensure measure / layout / draw

<img src="slides/images/closing_the_circle_3.png" width=1000 />

---

The different types of Appliers provided by client libraries

---

Appliers and how they are picked (when creating Composition)
