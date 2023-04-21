package dev.jorgecastillo.compose.app

/**
 * ### Exercise 8 👩🏾‍💻
 *
 * In this exercise we must create a Composable that uses BoxWithConstraints to display different
 * UIs for phone and tablet.
 *
 * Please use the exercise7_phone.png and exercise7_tablet.png images from the screenshots directory
 * at the root of this project as a reference for this exercise. That is how phone and tablet UIs
 * are expected to look correspondingly.
 *
 * Very recommended!! Use a Pixel C (tablet) emulator for running this test, since we need to
 * emulate both phone and tablet layouts on the same device, so we'll need enough space available
 * for both.
 *
 * To complete this exercise, we must use BoxWithConstraints to implement [AdaptativeScreen] so:
 *
 * 1. For tablets (maxWidth >= 600.dp) it shows a Row with the [ProfileScreen] with width 320.dp and
 *    max height on the left, and [FriendsScreen] taking the rest of the screen width to the right
 *    (you can use Modifier.weight(1f) for that).
 *
 * 2. For phones (maxWidth < 600.dp) it shows the [ProfileScreen] only, filling all the available
 *    screen space (Modifier.fillMaxSize()).
 *
 * 3. Run the test.
 */
class Exercise8 {
}