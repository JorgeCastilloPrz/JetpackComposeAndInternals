## **8. Composable lifecycle and side effects**

---

<img src="slides/images/composable_lifecycle.png" width="1000">

---

#### **Effects within the lifecycle**

* Effects run **after entering** and **before leaving**
* Effects must be bound to the Composable lifecycle
* Effects are affected by recompositions

---

#### **Side effect 🌀**

* Escapes the scope/control of the function
* No control on when it runs (recompositions)
* Unaware of lifecycle (never canceled)

```kotlin
@Composable
fun SpeakersScreen(eventId: String, service: Service) {
  var speakers by remember { mutableStateOf(emptyList()) }

  // network query as a side effect of the composition 🙈
  speakers = service.loadSpeakers(eventId)

  LazyColumn {
    items(speakers) { speaker -> SpeakerCard(speaker) }
  }
}
```

---

#### **We need that 🙏**

* 1️⃣ Effects run on correct lifecycle step

* 2️⃣ Effects dispose resources when leaving composition

* 3️⃣ Suspend effects canceled when leaving composition

* 4️⃣ Effects with an input are automatically disposed/cancelled & relaunched every time the input changes

---

#### **Effect Handlers**

---

#### **`DisposableEffect`**

* Effects that require cleanup before leaving
* E.g: attaching / detaching a callback

```kotlin
@Composable
fun BackButtonHandler(onBackPressed: () -> Unit) {
  val dispatcher = LocalOnBackPressedDispatcherOwner.current.onBackPressedDispatcher
  val backCallback = remember {
    object : OnBackPressedCallback {
      override fun handleOnBackPressed() {
        onBackPressed()
      }
    }
  }
  DisposableEffect(dispatcher) {
    dispatcher.addCallback(backCallback)
    onDispose {
      backCallback.remove()
    }
  }
}
```

---

#### **`DisposableEffect`**

* Fired when entering, disposed when leaving
* Disposed / re-triggered when keys change
* Supports one or multiple keys
* Pass constant key to span across recompositions

```kotlin
DisposableEffect(Unit) { ... }
```

---

#### **`SideEffect`**

* Posting updates to objs not managed by Compose
* "Fire on this composition or forget"
* Discarded if composition fails
* Non-disposable

```kotlin
@Composable
fun MyScreen(drawerTouchHandler: TouchHandler) {
  val drawerState = rememberDrawerState(Closed)

  // Runs on every successful composition/recomposition
  SideEffect {
    drawerTouchHandler.enabled = drawerState.isOpen
  }
}
```

---

#### **`LaunchedEffect`**

* Loading state when entering the composition
* For **`suspend`** effects

```kotlin
@Composable
fun SpeakersList(eventId: String, service: Service) {
  var speakers by remember { mutableStateOf(emptyList()) }

  LaunchedEffect(eventId) {
    speakers = service.loadSpeakers(eventId) // suspend
  }

  LazyColumn {
    items(speakers) { speaker -> SpeakerCard(speaker) }
  }
}
```

---

#### **`LaunchedEffect`**

* Runs when entering, canceled when leaving
* Canceled / re-launched when keys change
* Supports one or multiple keys
* Pass constant key to span across recompositions

```kotlin
LaunchedEffect(Unit) { ... }
```

---

#### **`rememberCoroutineScope`**

* Launch jobs in response to user interactions
* For **`suspend`** effects

```kotlin
@Composable
fun SearchScreen() {
  val scope = rememberCoroutineScope()
  var currentJob by remember { mutableStateOf(null) }
  var items by remember { mutableStateOf(emptyList()) }

  Column {
    Row {
      TextField("Start typing to search",
        onValueChange = { text ->
          currentJob?.cancel()
          currentJob = scope.async {
            delay(1000)
            items = viewModel.search(query = text)
          }
        }
      )
    }
  }
}
```

---

#### **`rememberCoroutineScope`**

* Scope canceled when leaving composition
* **Same scope across recompositions** 👉 all submitted jobs canceled when leaving

---

#### **`produceState`** 🍬

* Sugar for `LaunchedEffect` that feeds a state
* Supports **default value**

```kotlin
@Composable
fun SpeakersScreen(eventId: String, service: Service) {
  var uiState = produceState(
    initialValue = emptyList(),
    eventId // one or multiple keys
  ) {
    value = service.loadSpeakers(eventId) // suspend
  }

  LazyColumn {
    items(uiState.speakers) { speaker ->
      SpeakerCard(speaker)
    }
  }
}
```

---

#### **Effects in the Compose runtime**

* Triggered after all changes to the tree are applied
* Triggered in the same order they are stored

<img src="slides/images/closing_the_circle_1.png" width="900">

---

<img src="slides/images/closing_the_circle_4.png" width="800">