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
    contentDescription = null
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

#### **Reading from Composable functions**

```kotlin
@Composable
fun buttonColors(
    bgColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(bgColor),
    disabledBgColor: Color = MaterialTheme.colors
        .onSurface.copy(alpha = 0.12f)
        .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = MaterialTheme.colors
        .onSurface.copy(alpha = ContentAlpha.disabled)
): ButtonColors = DefaultButtonColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    // ...
)
```

```kotlin
// primary -> onPrimary, secondary -> onSecondary...
// Public, you can also use it
@Composable
fun contentColorFor(backgroundColor: Color) =
    MaterialTheme.colors.contentColorFor(backgroundColor)
      .takeOrElse { LocalContentColor.current }
```

---

#### **Material color system**

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

* Do we need to define all of them? 🤔

---

#### **Color builders**

* Dark 🌙 / Light 🌞

```kotlin
private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xff91a4fc)

// We can only override some of them
private val DarkColors = darkColors(
    primary = Yellow200,
    secondary = Blue200,
)

private val LightColors = lightColors(
    primary = Yellow500,
    primaryVariant = Yellow400,
    secondary = Blue700,
)
```

```kotlin
MaterialTheme(
    colors = if (darkTheme) DarkColors else LightColors
) {
    // app content
}
```

---

#### **Defining Colors**

```kotlin
// RGBA+color space. Alpha and ColorSpace optional
val rgbaWhiteFloat = Color(
      red = 1f, green = 1f, blue = 1f,
      alpha = 1f, ColorSpace.get(ColorSpaces.Srgb)
)

// 32-bit SRGB color int
val fromIntWhite = Color(android.graphics.Color.WHITE)
val fromLongBlue = Color(0xFF0000FF)

// from SRGB integer component values. Alpha is optional
val rgbaWhiteInt = Color(
      red = 0xFF, green = 0xFF, blue = 0xFF, alpha = 0xFF)
```

---

#### **Where** to define them?

* Close to your theme!
* **Access them only by theme** to ensure dark mode / multi-theme support ✅

---

#### **Overriding theme values** ✍️

* Since they are `CompositionLocal`, you can **override them for a nested subtree**
* Example with content alpha 👇

```kotlin
// De-emphasize content by setting content alpha
// (default is ContentAlpha.high)
CompositionLocalProvider(
  LocalContentAlpha provides ContentAlpha.medium
) {
    Text(/*...*/)
}
CompositionLocalProvider(
  LocalContentAlpha provides ContentAlpha.disabled
) {
    Icon(/*...*/)
    Text(/*...*/)
}
```

---

#### **Dark mode** 🌙

```kotlin
@Composable
fun MyTheme(
                         // 👇 follow device theme setting
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        /*...*/
        content = content
    )
}
```

---

#### **Dark mode** 🌙

Checking from Composables down the tree

```kotlin
val isLightTheme = MaterialTheme.colors.isLight
Icon(
    painterResource(
        id = if (isLightTheme) {
          R.drawable.ic_sun_24dp
        } else {
          R.drawable.ic_moon_24dp
        }
    ),
    contentDescription = "Theme"
)
```

---

#### **Dark mode** 🌙

* **Elevation overlays** determined by elevation

```kotlin
Surface(
    elevation = 2.dp,
    // color is automatically adjusted for elevation
    color = MaterialTheme.colors.surface,
    // ...
) { /*...*/ }
```

---

<img src="slides/images/elevation_overlays.png" width=300 />

---

#### **Typography** 📝

```kotlin
class Typography internal constructor(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val h5: TextStyle,
    val h6: TextStyle,
    val subtitle1: TextStyle,
    val subtitle2: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val button: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle
) { ... }
```

---

#### **Typography defaults**

```kotlin
h1: TextStyle = TextStyle(
    fontWeight = FontWeight.Light,
    fontSize = 96.sp,
    letterSpacing = (-1.5).sp
),
h2: TextStyle = TextStyle(
    fontWeight = FontWeight.Light,
    fontSize = 60.sp,
    letterSpacing = (-0.5).sp
),
h3: TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 48.sp,
    letterSpacing = 0.sp
),
...
subtitle1: TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.15.sp
),
subtitle2: TextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
),
body1: TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.5.sp
),
body2: TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
),
button: TextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 1.25.sp
),
caption: TextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.4.sp
),
...
```

---

#### **Typography** 📝

* We can override only some of them

```kotlin
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    ... */
)
```

---

#### **Colors and Typography**

Example: An average theme for an app

```kotlin
@Composable
fun ComposeAndInternalsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette // defined via darkColors()
    } else {
        LightColorPalette // defined via lightColors()
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
```

---

#### **`MaterialTheme` shapes**

```kotlin
val Shapes = Shapes(
    small = RoundedCornerShape(percent = 50),
    medium = RoundedCornerShape(0f),
    large = CutCornerShape(
        topStart = 16.dp,
        topEnd = 0.dp,
        bottomEnd = 0.dp,
        bottomStart = 16.dp
    )
)

MaterialTheme(shapes = Shapes, /*...*/)
```

---

#### **Default shapes**

```kotlin
class Shapes(
  /**
   * small components like Button or Snackbar
   */
  val small: CornerBasedShape = RoundedCornerShape(4.dp),
  /**
   * medium comps like Card or AlertDialog
   */
  val medium: CornerBasedShape = RoundedCornerShape(4.dp),
  /**
   * large comps like ModalDrawer or ModalBottomSheetLayout
   */
  val large: CornerBasedShape = RoundedCornerShape(0.dp)
)
```

---

Custom themes

---

Theme adapters for MDC and AppCompat

---

Writing our first animation

---

Advanced animations

---

Drag and swipe gestures

---

resources
