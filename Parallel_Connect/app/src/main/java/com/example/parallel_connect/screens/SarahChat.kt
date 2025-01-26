package com.example.parallel_connect.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.parallel_connect.viewModels.SarahChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SarahChatScreen(
    viewModel: SarahChatViewModel = viewModel(),
    navController: NavController
) {
    var userMessage by remember { mutableStateOf("") }
    val chatMessages = viewModel.chatMessages

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF515EC2))
    ) {
        // Barra superior com botão de voltar
        TopAppBar(
            title = { Text(text = "Chat with Sarah", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF515EC2))
        )

        // Conteúdo principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                items(chatMessages) { message ->
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(Color(0xFFF5F5F5))
                            .padding(8.dp),
                        color = Color(0xFF515EC2)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = userMessage,
                    onValueChange = { userMessage = it },
                    placeholder = { Text(text = "Type your message...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF515EC2),
                        cursorColor = Color(0xFF515EC2)
                    )
                )
                Button(
                    onClick = {
                        if (userMessage.isNotBlank()) {
                            viewModel.sendMessage(userMessage)
                            userMessage = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF515EC2))
                ) {
                    Text(text = "Send", color = Color.White)
                }
            }
        }
    }
}