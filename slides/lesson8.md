## **8. Effects and the Composable lifecycle**

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

---

#### **`rememberCoroutineScope`**

---

#### **`produceState`**
