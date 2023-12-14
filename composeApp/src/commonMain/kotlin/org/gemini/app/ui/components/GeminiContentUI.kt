package org.gemini.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import io.ktor.websocket.Frame
import org.gemini.app.data.model.Gemini

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GeminiContentUI(onContentChange: (String) -> Unit, response: Gemini) {
    var keyboardController = LocalSoftwareKeyboardController.current

    // Separate variable to hold the current value of OutlinedTextField
    var currentContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = currentContent,
            onValueChange = {
                // Update both the currentContent and call the provided lambda
                currentContent = it
                onContentChange(it)
            },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(Icons.Default.TextFields, contentDescription = null)
            }
        )

        Button(
            onClick = {
                keyboardController?.hide()
                // You can choose to call onContentChange if needed, else it can be omitted.
                // onContentChange(currentContent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Generate Content")
        }

        Text(
            text = response.candidates.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = Color.Gray
        )
    }
}
