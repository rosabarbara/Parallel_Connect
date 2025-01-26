package com.example.parallel_connect.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parallel_connect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ForumShoesScreen(navController: NavController) {
    // Firebase references
    val database = FirebaseDatabase.getInstance()
    val postsRef = database.getReference("forumShoesPosts")

    var userMessage by remember { mutableStateOf("") }
    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"

    // State for messages
    val messagesFlow = remember { MutableStateFlow<List<ForumPost>>(emptyList()) }
    val messages by messagesFlow.asStateFlow().collectAsState()

    // Fetch messages from Realtime Database
    LaunchedEffect(Unit) {
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = snapshot.children.mapNotNull { dataSnapshot ->
                    dataSnapshot.getValue(ForumPost::class.java)
                }.sortedBy { it.timestamp } // Sort by timestamp
                messagesFlow.value = posts
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database errors
            }
        })
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Show icon and forum title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Add space below the title section
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Removed because BUG
            /*// Placeholder for the show icon
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = R.drawable.shoes), // Replace with your actual drawable resource
                contentDescription = "Shoes Icon",
                modifier = Modifier
                    .size(100.dp) // Adjust size as needed
                    .padding(bottom = 8.dp)
            )*/
            // Title of the forum
            Text(
                text = "Missing shoes of Jeff",
                modifier = Modifier,
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }

        // Display forum posts
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { post ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "${post.userName}:")
                    Text(text = post.message, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        // Spacer to push the input row upwards
        Spacer(modifier = Modifier.height(16.dp))

        // Input and send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                placeholder = { Text("Type your message...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (userMessage.isNotBlank()) {
                        val newPost = ForumPost(
                            userName = userName,
                            message = userMessage,
                            timestamp = System.currentTimeMillis()
                        )
                        postsRef.push().setValue(newPost) // Push new post to the database
                        userMessage = "" // Clear the input
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}

data class ForumPost(
    val userName: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)

@Preview(showBackground = true)
@Composable
fun PreviewForumChatScreen() {
    // Create a mock NavController
    val navController = rememberNavController()

    // Preview the JeffChatScreen with the mock NavController
    ForumShoesScreen(navController = navController)
}