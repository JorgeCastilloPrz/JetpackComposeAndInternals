## **5. Theming.** 🎨

---

#### **Material Design**

* A **design system**
* Color palette
* Typography
* Shapes
* Material components

```groovy
dependencies {
 implementation "androidx.compose.material:material:$version"
}
```

---

#### **Using material**

Applying material to a subtree 🌲

```kotlin
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        MaterialTheme {
          // Any composables here will be themed
          MyApp()
        }
    }
  }
}
```

---

#### **Nested themes**

```kotlin
// MainActivity.kt
setContent {
    MaterialTheme {
        SpeakersScreen(someSpeakers())
    }
}
```
```kotlin
@Composable fun SpeakersScreen(...) {
    LazyColumn {
        items(speakers) { speaker ->
            if (speaker.company == "Lyft") {
                PinkTheme { // 👈 different colors!
                    SpeakerCard(speaker)
                }
            } else {
                SpeakerCard(speaker)
            }
        }
    }
}
```

---

<img src="slides/images/nested_themes.png" width=317 />

---

#### **Material components**

* They read colors, typography and shapes from it **(`CompositionLocal`s)**

---

#### **Explicit config**

* Colors, typography & shapes

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

#### **implicit config (defaults)**

```kotlin
@Composable
fun MaterialTheme(/*colors, typography, shapes*/) {
  val rippleIndication = rememberRipple()
  val selectionColors = rememberTextSelectionColors(colors)

  CompositionLocalProvider(
      LocalColors provides colors,
      LocalContentAlpha provides ContentAlpha.high,
      LocalIndication provides rippleIndication,
      LocalRippleTheme provides MaterialRippleTheme,
      LocalShapes provides shapes,
      LocalTextSelectionColors provides selectionColors,
      LocalTypography provides typography
  ) {
      content()
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

#### **Reading the values**

```kotlin
@Composable
fun buttonColors(
    bgColor: Color = MaterialTheme.colors.primary,

    disabledBgColor: Color = MaterialTheme.colors.onSurface
        .copy(alpha = 0.12f)
        .compositeOver(MaterialTheme.colors.surface),

    disabledContentColor: Color = MaterialTheme.colors
        .onSurface.copy(alpha = ContentAlpha.disabled),
    // ...
): ButtonColors = ...
```

<img src="slides/images/button.png" width=350 />

---

#### **Material color system** 🖍

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

```kotlin
private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xff91a4fc)

// We can only override some of them
private val DarkColors = darkColors( // 🌙
    primary = Yellow200,
    secondary = Blue200,
)

private val LightColors = lightColors( // 🌞
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

// Or load from resources
Text(
    text = "Ohi 🙋‍♂️",
    color = colorResource(R.color.purple_200)
)
```

---

#### **Where to define them?**

* Close to your theme! (private if possible)
* **Avoid direct access at all costs**
* **Access them only by theme** to ensure dark/light and dynamic theme support ✅

---

#### **Overriding theme values** ✍️

* `CompositionLocal`s allow this
* **Override for a nested subtree** 👇

```kotlin
MaterialTheme { // provides ContentAlpha.high
  // ...
  // Somewhere down the tree
  CompositionLocalProvider(
    LocalContentAlpha provides ContentAlpha.disabled
  ) {
      Icon(/*...*/)
      Text(/*...*/)
  }
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
<!-- .slide: data-scene="Slides" -->

#### **Our final theme**

An average theme for an app

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
<!-- .slide: data-scene="Coding" -->

📝 Exercise 8: Add PinkTheme to Lyft speakers

---
<!-- .slide: data-scene="Slides" -->

#### **Custom themes**

Our own design systems like `material`

---

#### **Extending material palettes**

* Extend `Colors`, `Typography`, or `Shapes` via **extensions properties**

* 🙅🏽‍♀️ Doesn't work for different themes (static)

```kotlin
// Use with MaterialTheme.colors.snackbarAction
val Colors.snackbarAction: Color
    get() = Red700

// Use with MaterialTheme.typography.textFieldInput
val Typography.textFieldInput: TextStyle
    get() = TextStyle(/* ... */)

// Use with MaterialTheme.shapes.card
val Shapes.card: Shape
    get() = RoundedCornerShape(size = 20.dp)
```

---

#### **Extending material palettes++**

* Wrapping `MaterialTheme`

* Supports multiple themes (via theme nesting)

```kotlin
// 1. Create a class to wrap the new colors
data class ExtendedColors(
  val tertiary: Color,
  val onTertiary: Color
)

// 2. Create a CompositionLocal to provide it down the tree
val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(
    tertiary = Color.Unspecified,
    onTertiary = Color.Unspecified
  )
}
```

---

```kotlin
// 3. Wrap the MaterialTheme with `CompositionLocalProvider`
@Composable
fun ExtendedTheme(..., content: @Composable () -> Unit) {
  val extendedColors = ExtendedColors(
    tertiary = Color(0xFFA8EFF0),
    onTertiary = Color(0xFF002021)
  )

  CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
    MaterialTheme(content = content)
  }
}
```

```kotlin
// 4. (Optional) Shortcut for better ergonomics
// Use like ExtendedTheme.colors.tertiary
object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
```

---

#### **Extending material palettes++**

* Wrap material components to use the new values

```kotlin
@Composable
fun ExtendedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ExtendedTheme.colors.tertiary,
            contentColor = ExtendedTheme.colors.onTertiary
            /* Other colors use values from MaterialTheme */
        ),
        onClick = onClick,
        modifier = modifier,
        content = content
    )
}
```

---

#### **Fully custom design systems** ✨

* Beware of material components (they expect the systems provided by material)
* E.g: A system that provides colors, typography and elevation

---

#### **Fully custom design systems** ✨

* 1st, create the classes to model the systems 👇

```kotlin
@Immutable
data class CustomColors(
    val content: Color,
    val component: Color,
    val background: List<Color> // gradient
)

@Immutable
data class CustomTypography(
    val body: TextStyle,
    val title: TextStyle
)

@Immutable
data class CustomElevation(
    val default: Dp,
    val pressed: Dp
)
```

---

#### **Fully custom design systems** ✨

* 2nd, create the `CompositionLocal`s

```kotlin
val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        content = Color.Unspecified,
        component = Color.Unspecified,
        background = emptyList()
    )
}
val LocalCustomTypography = staticCompositionLocalOf {
    CustomTypography(
        body = TextStyle.Default,
        title = TextStyle.Default
    )
}
val LocalCustomElevation = staticCompositionLocalOf {
    CustomElevation(
        default = Dp.Unspecified,
        pressed = Dp.Unspecified
    )
}
```

---

#### **Fully custom design systems** ✨

* 3rd, wrap with `CompositionLocalProvider`

```kotlin
@Composable
fun CustomTheme(..., content: @Composable () -> Unit) {
    val customColors = CustomColors(
        content = Color(0xFFDD0D3C),
        component = Color(0xFFC20029),
        background = listOf(Color.White, Color(0xFFF8BBD0))
    )
    val customTypography = CustomTypography(
        body = TextStyle(fontSize = 16.sp),
        title = TextStyle(fontSize = 32.sp)
    )
    val customElevation =
      CustomElevation(default = 4.dp, pressed = 8.dp)

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomTypography provides customTypography,
        LocalCustomElevation provides customElevation,
        content = content
    )
}
```

---

#### **Fully custom design systems** ✨

* 4th (optional), provide shortcuts

```kotlin
// Use with eg. CustomTheme.elevation.small
object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
    val typography: CustomTypography
        @Composable
        get() = LocalCustomTypography.current
    val elevation: CustomElevation
        @Composable
        get() = LocalCustomElevation.current
}
```

---

#### **Read from material components?** 🤔

* Read from your `CustomTheme` where possible
* Set values for missing systems manually

```kotlin
@Composable
fun CustomButton(/*...*/) {
  Button(
    colors = ButtonDefaults.buttonColors(
      backgroundColor = CustomTheme.colors.component,
      contentColor = CustomTheme.colors.content,
    ),
    shape = ButtonShape,
    elevation = ButtonDefaults.elevation(
      defaultElevation = CustomTheme.elevation.default,
      pressedElevation = CustomTheme.elevation.pressed
    ),
    onClick = onClick,
    modifier = modifier,
    content = content
  )
}
```

---

#### **Bridging XML themes** 🌉

* Inherit values from MaterialComponents XML theme - ♬ <a href="https://google.github.io/accompanist/">Accompanist</a> ♬

```xml
<style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight">
  <item name="colorPrimary">@color/purple_500</item>
  <item name="textAppearanceBody1">@style/TextAppearance.MyApp.Body1</item>
  <item name="shapeAppearanceSmallComponent">@style/ShapeAppearance.MyApp.SmallComponent</item>
</style>
```

```kotlin
// Colors, typography, shapes read from Context theme

MdcTheme {
  // For Theme.MaterialComponents.* XML themes
}
Mdc3Theme {
  // For Theme.Material3.* XML themes
}
AppCompatTheme {
  // For AppCompat XML themes
}
```

---

**Material Design 3 & Material You**

```gradle
implementation "androidx.compose.material3:material3:$version"
```

* MD3 versions of all material components
* Its own `MaterialTheme` 👇

```kotlin
MaterialTheme(
    colorScheme = …,
    typography = …,
    shapes = …
) {
    // M3 app content
}
```

---

**Dynamic color (Android 12+)**

```kotlin
val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val colorScheme = when {
    dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}
```

<img src="slides/images/material_you.png" width=350 />
