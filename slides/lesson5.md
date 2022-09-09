## **5. Theming**

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

📝 Exercise 8: Create a custom theme for an app

---

#### **Custom themes**

Our own design systems like `material`

---

#### **Extending material palettes**

* Extend `Colors`, `Typography`, or `Shapes` via **extensions properties**

* 🙅🏽‍♀️ Doesn't work for different themes (static)

```kotlin
// Use with MaterialTheme.colors.snackbarAction
val Colors.snackbarAction: Color
    get() = if (isLight) Red300 else Red700

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
@Immutable
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

#### **Replacing material systems**

* E.g: replacing typography, colors or shapes
* Same system than the above

```kotlin
@Immutable
data class ReplacementTypography(
    val body: TextStyle,
    val title: TextStyle
)

val LocalReplacementTypography = staticCompositionLocalOf {
    ReplacementTypography(
        body = TextStyle.Default,
        title = TextStyle.Default
    )
}
```

---

#### **Replacing material systems**

```kotlin
@Composable
fun ReplacementTheme(..., content: @Composable () -> Unit) {
    val replacementTypography = ReplacementTypography(
        body = TextStyle(fontSize = 16.sp),
        title = TextStyle(fontSize = 32.sp)
    )

    CompositionLocalProvider(
        LocalReplacementTypography provides replacementTypography
    ) {
        MaterialTheme(content = content)
    }
}
```
```kotlin
// Use with eg. ReplacementTheme.typography.body
object ReplacementTheme {
    val typography: ReplacementTypography
        @Composable
        get() = LocalReplacementTypography.current
}
```

---

#### **And from material components?** 🤔

* One more time, wrap them and replace defaults
* Wrap content lambdas with provider funcs for values not exposed as params

```kotlin
@Composable
fun ReplacementButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        shape = ReplacementTheme.shapes.component,
        onClick = onClick,
        modifier = modifier,
        content = {
            // When no utility functions available
            ProvideTextStyle(
                value = ReplacementTheme.typography.body
            ) {
                content()
            }
        }
    )
}
```

---

#### **Fully custom design systems** ✨

* Beware of material components (they expect the systems provided by material)
* E.g: a system that provides colors, typography and elevation
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

* 2nd, create the `CompositionLocal` to provide them down the tree

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

* 3rd, wrap the content with `CompositionLocalProvider`

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
    val customElevation = CustomElevation(
        default = 4.dp,
        pressed = 8.dp
    )
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

#### **And for material components?** 🤔

* Forward the values from your `CustomTheme` where possible
* Set values for the missing systems manually

```kotlin
@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = CustomTheme.colors.component,
            contentColor = CustomTheme.colors.content,
            disabledBackgroundColor = CustomTheme.colors.content
                .copy(alpha = 0.12f)
                .compositeOver(CustomTheme.colors.component),
            disabledContentColor = CustomTheme.colors.content
                .copy(alpha = ContentAlpha.disabled)
        ),
        shape = ButtonShape,
        elevation = ButtonDefaults.elevation(
            defaultElevation = CustomTheme.elevation.default,
            pressedElevation = CustomTheme.elevation.pressed
            /* disabledElevation = 0.dp */
        ),
        onClick = onClick,
        modifier = modifier,
        content = {
            ProvideTextStyle(
                value = CustomTheme.typography.body
            ) {
                content()
            }
        }
    )
}
```

---

#### **Bridging XML themes**

* Make `MaterialTheme` inherit the values from your MaterialComponents XML theme

```xml
<style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight">
    <!-- Material 2 color attributes -->
    <item name="colorPrimary">@color/purple_500</item>
    <item name="colorSecondary">@color/green_200</item>

    <!-- Material 2 type attributes-->
    <item name="textAppearanceBody1">@style/TextAppearance.MyApp.Body1</item>
    <item name="textAppearanceBody2">@style/TextAppearance.MyApp.Body2</item>

    <!-- Material 2 shape attributes-->
    <item name="shapeAppearanceSmallComponent">@style/ShapeAppearance.MyApp.SmallComponent</item>
</style>
```

```kotlin
MdcTheme {
    // MaterialTheme.colors, MaterialTheme.typography, MaterialTheme.shapes
    // will now contain copies of the Context's theme
}
```

---

#### **Bridging XML themes**

* For `AppCompat` XML themes, use Accompanist AppCompat Theme Adapter instead

```gradle
implementation "com.google.accompanist:accompanist-appcompat-theme:<version>"
```

```kotlin
AppCompatTheme {
    // MaterialTheme.colors, MaterialTheme.shapes, MaterialTheme.typography
    // will now contain copies of the context's theme
}
```

---

**Material Design 3 and Material You**

```gradle
implementation "androidx.compose.material3:material3:$version"
```

* Provides MD3 versions of all material components
* Its own `MaterialTheme` 👇

```kotlin
MaterialTheme(
    colorScheme = …,
    typography = …
    // Updates to shapes coming soon
) {
    // M3 app content (and M3 components)
}
```

---

**Material Design 3 and Material You**

```kotlin
// Dynamic color available on Android 12+
val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val colorScheme = when {
    dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
    dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}
```

<img src="slides/images/material_you.png" width=600 />
