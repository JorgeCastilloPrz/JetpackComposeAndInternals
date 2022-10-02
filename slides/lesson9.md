## **9. Advanced UI - II**

---

#### **Resources**

Use the utility functions

```kotlin
// Strings
Text(text = stringResource(R.string.compose))

Text(text = stringResource(
    R.string.congratulate, "New Year", 2021))

Text(text = pluralStringResource(
    R.plurals.numberOfSongsAvailable, quantity, quantity))
```
```kotlin
// Dimens
val smallPadding = dimensionResource(R.dimen.padding_small)
Text(Modifier.padding(smallPadding), text = "...")
```
```kotlin
// Colors (prefer reading them from theme!)
Divider(color = colorResource(R.color.colorGrey))
```

---

#### **Drawables?**

* We feed them to a `Painter`
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

#### **Animated Vectors**

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

#### **Animations** 💃🏼

---

#### **Gestures** 🤏🏽
