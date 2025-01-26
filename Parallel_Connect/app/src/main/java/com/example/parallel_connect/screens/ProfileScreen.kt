package com.example.parallel_connect.screens


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@Composable
fun ProfileScreen(navController: NavController) {
    // Get the current user
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Extract user details
    val displayName = currentUser?.displayName ?: "Unknown User"

    // State to store the number of spirits caught
    var spiritsCaught by remember { mutableStateOf(0) }

    // Fetch spirits caught from Firebase
    LaunchedEffect(Unit) {
        val userId = currentUser?.uid ?: return@LaunchedEffect

        val caughtSpiritsRef = FirebaseDatabase.getInstance().reference
            .child("caughtSpirits")
            .child(userId)

        caughtSpiritsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Count the number of spirits caught by the user
                spiritsCaught = snapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors (optional)
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile Title
        Text(
            text = "Profile",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // User Name
        Text(
            text = "Name: $displayName",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Spirits Caught
        Text(
            text = "Spirits Caught: $spiritsCaught",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        // Button to Go Back to Main Menu
        Button(onClick = { navController.popBackStack() }) {
            Text("Back to Main Menu")
        }

        // Logout Button
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Screens.Login.route) // Navigate to the login screen
            }
        ) {
            Text("Log Out")
        }
    }
}
