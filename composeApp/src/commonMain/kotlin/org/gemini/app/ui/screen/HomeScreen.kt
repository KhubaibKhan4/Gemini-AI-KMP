package org.gemini.app.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch
import org.gemini.app.domain.repository.Repository
import org.gemini.app.domain.usecases.GeminiState
import org.gemini.app.presentation.MainViewModel
import org.gemini.app.ui.components.LoadingBox

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val repository = remember { Repository() }
        val viewModel = remember { MainViewModel(repository) }
        var states by remember { mutableStateOf<GeminiState>(GeminiState.LOADING) }
        val coroutineScope = rememberCoroutineScope()
        var content by remember { mutableStateOf("hey") }
        var search by remember { mutableStateOf("") }
        var chatMessages by remember { mutableStateOf(listOf<String>()) }
        var isEnable by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (isEnable) {
                viewModel.generateContent(content)
            }
        }

        states = viewModel.content.collectAsState().value
        when (states) {
            is GeminiState.LOADING -> {
                LoadingBox()
                isEnable = true
            }

            is GeminiState.SUCCESS -> {
                isEnable = true
                val data = (states as GeminiState.SUCCESS).content
                LaunchedEffect(Unit) {
                    val role = data.candidates?.firstOrNull()?.content?.role.orEmpty()
                    val text =
                        data.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()

                    if (role.isNotEmpty() && text.isNotEmpty()) {
                        chatMessages = chatMessages + "$role: $text"
                    }
                }
                println("API Response $data")
            }

            is GeminiState.ERROR -> {
                isEnable = true
                val error = (states as GeminiState.ERROR).error
                // ErrorBox(error)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Display chat messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                state = rememberLazyListState()
            ) {
                items(chatMessages) { chatMessage ->
                    ChatMessage(message = chatMessage)
                }
            }

            // Input field and send button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(isEnable) {
                    // Use a Card with rounded corners
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = search,
                                onValueChange = {
                                    search = it
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                placeholder = { Text("Type a message...") },
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                viewModel.generateContent(search)
                                                content = ""
                                                search = ""
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        SelectionContainer {
            Text(message)
        }
    }
}
