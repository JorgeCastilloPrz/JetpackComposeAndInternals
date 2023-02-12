## **4. Runtime.**

---

#### **An agnostic runtime** 🤷🏾‍♀️

* Runtime works with **generic node type `N`**

* Supports any node type

* Node type picked by clients (e.g: Compose UI)

* Clients can feed multiple types (e.g: `LayoutNode`, `VNode`)

---

#### **Feeding the node type**

* Done when emitting the node
* Teaches runtime **how to create & initialize it**

```kotlin
fun Layout(modif: Modifier = Modifier, mp: MeasurePolicy) {
  // ...
  ReusableComposeNode<LayoutNode, /*...*/>(
    factory = { LayoutNode() },
    update = {
      set(mp, { this.measurePolicy = it })
      set(modif, { this.modifier = it }) 
      // ...
    },
  )
}
```

---

<img src="slides/images/composition_trees.png" width=550 />

---

#### **VNode (vectors)**

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

#### **`Applier<N>`**

* Abstraction to keep runtime agnostic of node type
* Impl provided by clients of the runtime

```kotlin
interface Applier<N> {
    val current: N // (visitor)
    fun onBeginChanges() {}
    fun onEndChanges() {}
    fun down(node: N)
    fun up()

    // Can build the tree top-down or bottom-up
    fun insertTopDown(index: Int, instance: N)
    fun insertBottomUp(index: Int, instance: N)

    fun remove(index: Int, count: Int)
    fun move(from: Int, to: Int, count: Int)
    fun clear()
}
```

---

#### **`UiApplier (LayoutNode)`**

* Inserts, removes, moves `LayoutNode`s

```kotlin
internal class UiApplier(root: LayoutNode) : AbstractApplier<LayoutNode>(root) {

  override fun insertTopDown(index: Int, instance: LayoutNode) {
    // Ignored. Insert is performed in [insertBottomUp].
  }

  override fun insertBottomUp(index: Int, instance: LayoutNode) {
    current.insertAt(index, instance)
  }

  override fun remove(index: Int, count: Int) {
    current.removeAt(index, count)
  }

  override fun move(from: Int, to: Int, count: Int) {
    current.move(from, to, count)
  }

  // ...
}
```

---

#### **`VectorApplier (VNode)`**

* Inserts, removes, moves `VNode`s

```kotlin
class VectorApplier(root: VNode) : AbstractApplier<VNode>(root) {
    override fun insertTopDown(index: Int, instance: VNode) {
        current.asGroup().insertAt(index, instance)
    }

    override fun insertBottomUp(index: Int, instance: VNode) {
        // Ignored as the tree is built top-down.
    }

    override fun remove(index: Int, count: Int) {
        current.asGroup().remove(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.asGroup().move(from, to, count)
    }
}
```

---

#### **Creating the `Applier`**

* Appliers are created when creating the Composition

```kotlin
// Integration point with the platform
private fun ComposeView.setContent(...): Composition {
    // We pick applier and the layout type here
    val original = Composition(UiApplier(owner.root), parent)
    val wrapped = WrappedComposition(original)
    wrapped.setContent(content)
    return wrapped
}
```
```kotlin
// Or when creating a Subcomposition
internal actual fun createSubcomposition(
    container: LayoutNode,
    parent: CompositionContext
): Composition = Composition(UiApplier(container), parent)
```