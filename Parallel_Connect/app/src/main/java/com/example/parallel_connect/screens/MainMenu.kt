package com.example.parallel_connect.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.parallel_connect.R // Adjust based on your project structure
import com.example.parallel_connect.screens.Screens
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainMenu(
    navController: NavController
) {
    // Load custom font
    val customFont = FontFamily(Font(R.font.nightsecret)) // Replace with your font file

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Parallel Connect",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = customFont,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF515EC2)),
                actions = {
                    // Circle icon button in the top right corner to navigate to the profile screen
                    IconButton(
                        onClick = { navController.navigate(Screens.Profile.route) },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle, // Using built-in AccountCircle icon
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp) // Adjust the size of the icon
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF515EC2))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // "Parallel" text
                Text(
                    text = "Parallel",
                    fontSize = 80.sp,
                    fontFamily = customFont,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // "Connect" text
                Text(
                    text = "Connect",
                    fontSize = 80.sp,
                    fontFamily = customFont,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Map button
                Button(
                    onClick = { navController.navigate(Screens.Map.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Map", fontSize = 18.sp, color = Color(0xFF515EC2))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Messages button
                Button(
                    onClick = { navController.navigate(Screens.ConversationsList.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Messages", fontSize = 18.sp, color = Color(0xFF515EC2))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Test Chat Bot button
               /* Button(
                    onClick = { navController.navigate(Screens.Jeff.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Test Chat Bot", fontSize = 18.sp, color = Color(0xFF515EC2))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Test Forum button
                Button(
                    onClick = { navController.navigate(Screens.ForumShoes.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Test Forum Screen", fontSize = 18.sp, color = Color(0xFF515EC2))
                }*/
            }
        }
    )
}