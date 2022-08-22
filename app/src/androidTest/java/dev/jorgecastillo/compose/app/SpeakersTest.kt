//package dev.jorgecastillo.compose.app
//
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.test.assertTextEquals
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onRoot
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performScrollToIndex
//import androidx.compose.ui.test.printToLog
//import androidx.lifecycle.AbstractSavedStateViewModelFactory
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.savedstate.SavedStateRegistryOwner
//import dev.jorgecastillo.compose.app.speakers.SpeakersRepository
//import dev.jorgecastillo.compose.app.speakers.SpeakersScreen
//import dev.jorgecastillo.compose.app.speakers.SpeakersViewModel
//import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme
//import org.junit.Rule
//import org.junit.Test
//
///**
// * ### Exercise 👩🏾‍💻
// *
// * This is a UI test. It runs the [SpeakersScreen] composable within an empty Activity, and asserts
// * over the list of [Speaker]s displayed. The speakers are loaded using a repository from a
// * [SpeakersViewModel] that is already provided from the test. This ViewModel is hooked to the
// * host Activity lifecycle via the [viewModel] function.
// *
// * The test asserts over all the speaker names that expects to be visible on screen.
// *
// * To complete this exercise:
// *
// * 1. Go to [SpeakersViewModel] and use the "injected" [SavedStateHandle] to save the speakers
// * loaded from the [SpeakersRepository].
// * 2. Use [SavedStateHandle.getStateFlow] to read the speakers from the [SavedStateHandle] as a
// * StateFlow. The StateFlow will emit every new list of speakers written to the handle. Note that
// * you will use the same name to write and read the list of speakers from the handle.
// * 3. Collect the ViewModel's uiState from the Composable using [collectAsState].
// * 4. Use rememberSaveable to save the already hoisted scrolling state for the list.
// *
// *
// * Validate your implementation by running the provided test.
// */
//class SpeakersTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
//
//    @Test
//    fun text_always_reflects_the_most_fresh_state() {
//        // Start the app
//        composeTestRule.setContent {
//            // Create ViewModel scoped to the host activity (test ComponentActivity here).
//            val viewModel: SpeakersViewModel = viewModel(
//                factory = viewModelFactory(
//                    composeTestRule.activity,
//                    SpeakersRepository()
//                )
//            )
//
//            ComposeAndInternalsTheme {
//                SpeakersScreen(viewModel)
//            }
//        }
//
//        // Assert for all the items in the list.
//        composeTestRule.onNodeWithTag("SpeakersList").
//        composeTestRule.onNodeWithTag("SpeakersList").performScrollToIndex(1)
//
//        composeTestRule.onNodeWithTag("name").assertTextEquals("Jane Smith")
//        composeTestRule.onRoot().printToLog("TEST")
//        composeTestRule.onNodeWithText("Generate").performClick()
//    }
//}
//
///**
// * Factory to create a [SpeakersViewModel]. Can be used along with [viewModel] in order to create
// * the ViewModel scoped to the host Activity or Fragment.
// */
//fun viewModelFactory(owner: SavedStateRegistryOwner, repository: SpeakersRepository) =
//    object : AbstractSavedStateViewModelFactory(owner, null) {
//        override fun <T : ViewModel> create(
//            key: String,
//            modelClass: Class<T>,
//            handle: SavedStateHandle
//        ): T {
//            return SpeakersViewModel(repository, handle) as T
//        }
//    }