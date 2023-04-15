package dev.jorgecastillo.compose.app.tools

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    group = "light"
)
@Preview(
    name = "landscape",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    group = "light"
)
@Preview(
    name = "foldable",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    group = "light"
)
@Preview(
    name = "tablet",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    group = "light"
)
@Preview(
    name = "phone",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "dark"
)
@Preview(
    name = "landscape",
    device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "dark"
)
@Preview(
    name = "foldable",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "dark"
)
@Preview(
    name = "tablet",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    group = "dark"
)
annotation class DevicePreviews
