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

<img src="slides/images/animations_cheat_sheet.png" width=350 />

[Download here](https://storage.googleapis.com/android-stories/compose/Compose_Animation_Cheat_Sheet.pdf)

---

#### **`AnimatedVisibility`**

```kotlin
var editable by remember { mutableStateOf(true) }
AnimatedVisibility(visible = editable) {
  SpeakerCard(speaker)
}
```

<img src="slides/images/anim_visibility.gif" width=320 />

---

#### **`AnimatedVisibility` (custom)**

```kotlin
var editable by remember { mutableStateOf(true) }

AnimatedVisibility(
 visible = editable,
 enter = slideInHorizontally {-40.dp.roundToPx()} + fadeIn(),
 exit = slideOutHorizontally() + fadeOut()
) {
 SpeakerCard(speaker)
}
```

<img src="slides/images/anim_visibility2.gif" width=320 />

---

#### **Without removing from composition**

```kotlin
var editable by remember { mutableStateOf(true) }
val animatedAlpha: Float by animateFloatAsState(
  if (editable) 1f else 0f,
  animationSpec = tween(1500) // like interpolators
)

Box(Modifier.graphicsLayer { alpha = animatedAlpha }) {
  SpeakerCard(speaker = speaker)
}
```

<img src="slides/images/anim_visibility3.gif" width=320 />

---

#### **`animateContentSize`**

```kotlin
var expanded by remember { mutableStateOf(true) }
Column(Modifier.animateContentSize()) { // also customizable
  SpeakerCard(speaker = speaker)
  if (expanded) {
    Text(stringResource(id = R.string.description))
  }
}
```

<img src="slides/images/anim_contentsize.gif" width=320 />

---

#### **other properties**

```kotlin
var enabled by remember { mutableStateOf(true) }
val animatedColor = animateColorAsState(
  targetValue = if (enabled) Color.Red else Color.Gray
)
// animateDpAsState()
// animateOffsetAsState()
// animateFloatAsState()
// animateSizeAsState()
// animateRectAsState()
// animateIntAsState()

Box(Modifier.drawBehind { drawRect(animatedColor.value) }) {
  SpeakerCard(speaker)
}
```

<img src="slides/images/anim_properties.gif" width=320 />

---

#### **Lazy list item changes**

```kotlin
val speakers = remember { speakers.toMutableStateList() }
LazyColumn {
  // item { Button(...) }
  for (speaker in speakers) {
    item(key = speaker.id) { // 👈 important!!
      Box(Modifier.animateItemPlacement()) {
        SpeakerCard(speaker)
      }
    }
  }
}
```

<img src="slides/images/anim_lazylist_item_changes.gif" width=320 />

---

#### **`AnimatedContent`**

```kotlin
var isLoading by remember { mutableStateOf(true) }
Column {
  // Button(...)
  AnimatedContent(isLoading) { targetState ->
    if (targetState) { // conditional composition
      CircularProgressIndicator()
    } else {
      SpeakerCard(speaker = speakers)
    }
  }
}
```

<img src="slides/images/anim_animatedcontent.gif" width=320 />

---

#### **Multiple properties at once**

```kotlin
var enabled by remember { mutableStateOf(false) }
val transition = updateTransition(enabled)

val borderWidth by transition.animateDp { isEnabled ->
  if (isEnabled) 24.dp else 8.dp
}

val color by transition.animateColor { isEnabled ->
  if (isEnabled) Purple else Teal
}

Box(Modifier.border(borderWidth, color, CircleShape))
```

<img src="slides/images/anim_multiple_props.gif" width=320 />

---

#### **Repeat an animation**

```kotlin
val infiniteTransition = rememberInfiniteTransition()
val color by infiniteTransition.animateColor(
  initialValue = MaterialTheme.colors.primary,
  targetValue = MaterialTheme.colors.secondary,

  // or finiteRepeatable to repeat N times
  animationSpec = infiniteRepeatable(
    animation = tween(1000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse
  )
)

Box(Modifier.border(24.dp, color, CircleShape))
```

<img src="slides/images/anim_infinite_transition.gif" width=320 />

---

#### **Gestures** 🤏🏽
