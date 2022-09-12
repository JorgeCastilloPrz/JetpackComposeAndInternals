package dev.jorgecastillo.compose.app.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jorgecastillo.compose.app.data.NameRepository
import dev.jorgecastillo.compose.app.ui.theme.ComposeAndInternalsTheme

@Composable
fun NameGenerator(repo: NameRepository) {
    val name = remember { mutableStateOf(repo.next()) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.testTag("name"),
            text = name.value,
            style = MaterialTheme.typography.h4
        )
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { name.value = repo.next() }
        ) {
            Text("Generate")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    ComposeAndInternalsTheme {
        NameGenerator(NameRepository())
    }
}
