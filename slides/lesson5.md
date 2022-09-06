## **5. Advanced UI - part II**

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
    contentDescription = null // decorative element
)
```

<img src="slides/images/animatedvectordrawable.gif" width=200 />

---

#### **Theming** 🎨

---

#### **Material Design** 🎨

* A **design system**
* Theming and components

```groovy
dependencies {
    implementation "androidx.compose.material:material:$version"
}
```

---

#### **Material Design** 🎨

* Applying material to a subtree

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val speakersRepository = FakeSpeakerRepository()
            MaterialTheme {
              // Any composables here
              LazySpeakersScreen(speakers)
            }
        }
    }
}
```

---

#### **Material** 🎨

* Material components are built on top of it
* `TextField`, `TopAppBar`, `Card`, `Button`, `Scaffold`, `FloatingActionButton`...

---

```kotlin
MaterialTheme { // colors, typographies, shapes provided 👇
  Scaffold(
    topBar = { TopAppBar(title = { Text("My app") }) },
    content = { contentPadding ->
      Column(Modifier.padding(contentPadding)) {
        TextField(
          value = "",
          label = { Text("Insert some text") },
          onValueChange = {})
        Button(onClick = { /*TODO*/ }) {
          Text("Click me!")
        }
      }
    }
  )
}
```

---

<img src="slides/images/material2.png" width=300 />

---

#### **`MaterialTheme`**

Exposes colors, typography & shapes to the subtree

```kotlin
MaterialTheme(
    colors = …,
    typography = …,
    shapes = …
) {
    // app content (can read from those)
}
```

---

* Also provides default ripple indication, text selection colors, content alpha, and ripple theme (color and alpha).
* Everything **via `CompositionLocal`** ⏬ ⏬ ⏬

```kotlin
@Composable
fun MaterialTheme(
    colors: Colors = MaterialTheme.colors,
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }
      .apply { updateColorsFrom(colors) }

    val rippleIndication = rememberRipple()
    val selectionColors = rememberTextSelectionColors(rememberedColors)

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalContentAlpha provides ContentAlpha.high,
        LocalIndication provides rippleIndication,
        LocalRippleTheme provides MaterialRippleTheme,
        LocalShapes provides shapes,
        LocalTextSelectionColors provides selectionColors,
        LocalTypography provides typography
    ) {
        ProvideTextStyle(value = typography.body1) {
            PlatformMaterialTheme(content)
        }
    }
}
```

---

#### **`MaterialTheme`**

Shortcuts for colors, typography and shapes

```kotlin
object MaterialTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable // optimize for reading only
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalShapes.current
}
```

---

#### **Reading colors** from `Composable` functions

```kotlin
@Composable
fun buttonColors(
    bgColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    disabledBgColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = MaterialTheme.colors.onSurface
        .copy(alpha = ContentAlpha.disabled)
): ButtonColors = DefaultButtonColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor
)
```

```kotlin
// primary -> onPrimary, secondary -> onSecondary...
@Composable
fun contentColorFor(backgroundColor: Color) =
    MaterialTheme.colors.contentColorFor(backgroundColor)
      .takeOrElse { LocalContentColor.current }
```

---

#### Material **colors**

```kotlin
class Colors(
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onError: Color,
    isLight: Boolean
)
```

---

Custom themes. Making our app material

---

Writing our first animation

---

Advanced animations

---

Drag and swipe gestures

---

resources
