package com.example.parallel_connect.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsListScreen(navController: NavController) {
    val caughtSpirits = remember { mutableStateListOf<String>() }
    val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("caughtSpirits")
        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "defaultUser")

    // Fetch "caught spirits" from Firebase
    LaunchedEffect(Unit) {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                caughtSpirits.clear()
                for (child in snapshot.children) {
                    val spiritName = child.key ?: continue
                    caughtSpirits.add(spiritName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching caught spirits: ${error.message}")
            }
        })
    }

    // Display conversation list
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF515EC2))
    ) {
        TopAppBar(
            title = { Text(text = "Active Conversations", color = Color.White) },
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

        // If no spirits are found
        if (caughtSpirits.isEmpty()) {
            Text(
                text = "No spirits found yet",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentWidth()
            )
        } else {
            // LazyColumn to show caught spirits if available
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(caughtSpirits) { spirit ->
                    Text(
                        text = spirit,
                        fontSize = 20.sp,
                        color = Color(0xFF515EC2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable { navController.navigate("$spirit/$spirit") }
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Divider(color = Color(0xFF515EC2), thickness = 1.dp)
                }
            }
        }
    }
}