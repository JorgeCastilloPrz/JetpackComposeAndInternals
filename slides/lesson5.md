## **Advanced UI - part II**

---

#### **Vectors in Compose**

* `painterResource`
  * `VectorDrawable`
  * `BitmapDrawable` (rasterized imgs)

```kotlin
Icon(
  painter = painterResource(R.drawable.ic_android),
  contentDescription = null,
  tint = Color.Red
)
```

<img src="slides/images/vector_drawable.png" width=100 />

---

#### **Vectors in Compose**

* `ImageVector`
  * `Icons` by **material**
  *  Visual styles: `Filled` (`Default`), `Outlined`, `Rounded`, `TwoTone`, and `Sharp`

```kotlin
Icon(
  painter = rememberVectorPainter(image = Icons.Default.Add),
  contentDescription = null,
  tint = Color.Red
)
```

<img src="slides/images/vector_drawable2.png" width=100 />

---

#### **Vectors in Compose**

* `ImageVector`s are created with a DSL

```kotlin
public val Icons.Filled.Add: ImageVector
  get() {
    if (_add != null) {
      return _add!!
    }
    _add = materialIcon(name = "Filled.Add") {
      materialPath {
          moveTo(19.0f, 13.0f)
          horizontalLineToRelative(-6.0f)
          verticalLineToRelative(6.0f)
          horizontalLineToRelative(-2.0f)
          verticalLineToRelative(-6.0f)
          horizontalLineTo(5.0f)
          verticalLineToRelative(-2.0f)
          horizontalLineToRelative(6.0f)
          verticalLineTo(5.0f)
          horizontalLineToRelative(2.0f)
          verticalLineToRelative(6.0f)
          horizontalLineToRelative(6.0f)
          verticalLineToRelative(2.0f)
          close()
      }
    }
    return _add!!
}
```

---

Theming. Material and custom themes. Making our app material

---

Writing our first animation

---

Advanced animations

---

Drag and swipe gestures

---

Vectors in Compose (maybe move?)
