<!-- .slide: data-scene="Slides" -->
## **4. Runtime.**

---

#### **An agnostic runtime** 🤷🏾‍♀️

* Runtime works with **generic node type `N`**

* Supports any node type

* Node type picked by clients (e.g: Compose UI - `LayoutNode`, `VNode`)

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
@Composable fun Group(/*...*/) {
    ComposeNode<GroupComponent, VectorApplier>(
        factory = { GroupComponent() },
        update = {
            set(name) { this.name = it }
            // ...
        }
    ) {
        content()
    }
}

@Composable fun Path(/*...*/) {
    ComposeNode<PathComponent, VectorApplier>(
        factory = { PathComponent() },
        update = {
            set(name) { this.name = it }
            // ...
        }
    )
}
```

---

#### **List of changes**

* Nodes are not inserted right away
* Executing a composable 👇
* Produces a list of changes (lambdas)

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

* Once composition is done (or recomposition), the list of changes is sent to the `Applier`.
* The **`Applier`** applies (materializes) them.

---

<img src="slides/images/closing_the_circle_2.png" width=700 />

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

* Done when creating the Composition

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