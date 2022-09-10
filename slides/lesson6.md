## **5. Advanced UI - part II**

---

#### Android **Resources** in Compose

Utility functions to load them

```kotlin
// Strings
stringResource(R.string.compose)
stringResource(R.string.congratulate, "New Year", 2021)
pluralStringResource(
  R.plurals.runtime_format, quantity, quantity)

// Dimens
dimensionResource(R.dimen.padding_small)

// Colors (prefer reading them from theme!)
colorResource(R.color.colorGrey)
```

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

<img src="slides/images/material_icons.png" width=900 />

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

#### **Vectors in Compose**

```kotlin
// Load the animated vector drawable
val image = AnimatedImageVector.
      animatedVectorResource(R.drawable.animated_vector)

val atEnd by remember { mutableStateOf(false) }
Icon(
    // paint it
    painter = rememberAnimatedVectorPainter(image, atEnd),
    contentDescription = null
)
```

<img src="slides/images/animatedvectordrawable.gif" width=200 />

---

Writing our first animation

---

Advanced animations

---

Drag and swipe gestures
