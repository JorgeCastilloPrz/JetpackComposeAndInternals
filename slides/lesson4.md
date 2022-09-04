## **4. Compose from the runtime**

---

#### An **agnostic runtime** 🤷🏾‍♀️

* Runtime works with **generic node type `N`**

* Supports any node type

* Node type picked by clients (e.g: Compose UI)

* Clients can feed multiple types (e.g: `LayoutNode`, `VNode`)

---

#### **Picking the node type**

* Done when emitting the node

```kotlin
@Composable
fun Layout(modif: Modifier = Modifier, mp: MeasurePolicy) {
  val density = LocalDensity.current
  val layoutDirection = LocalLayoutDirection.current
  val viewConfiguration = LocalViewConfiguration.current
  val materialized = currentComposer.materialize(modif)
  
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

<img src="slides/images/composition_trees.png" width=550 />

---

Node types used by the runtime. LayoutNode and VNode.

---

Recap on the LayoutNode tree and the concept of Owner

---

Invalidation to reflect changes

---

How changes to add, move, replace, or remove nodes from tree are materialized

---

The different types of Appliers provided by client libraries
