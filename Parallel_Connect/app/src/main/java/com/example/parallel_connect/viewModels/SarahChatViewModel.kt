package com.example.parallel_connect.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class SarahChatViewModel : ViewModel() {
    // List of chat messages
    val chatMessages = mutableStateListOf<String>()

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var messagesRef: DatabaseReference? = null
    private var currentUserId: String? = null

    // Define the listener as a class-level property
    private val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // Clear the existing messages and populate with the updated data
            chatMessages.clear()
            snapshot.children.forEach { messageSnapshot ->
                val message = messageSnapshot.getValue(String::class.java)
                message?.let { chatMessages.add(it) }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            println("Real-time listener cancelled: ${error.message}")
        }
    }

    init {
        setupChatListener()
    }

    private fun setupChatListener() {
        val userId = auth.currentUser?.uid
        if (userId != currentUserId) {
            // Remove the listener for the old user
            messagesRef?.removeEventListener(listener)

            // Update the current user ID
            currentUserId = userId

            if (currentUserId != null) {
                // Reference to the current user's chat messages in Firebase
                messagesRef = database.getReference("chats/Sarah/$currentUserId")

                // Add the real-time listener
                messagesRef?.addValueEventListener(listener)
            }
        }
    }

    // Method to send a message and get a response from the GenerativeModel
    fun sendMessage(userMessage: String) {
        // Add the user's message to the chat history
        chatMessages.add("You: $userMessage")

        // Push the user's message to Firebase
        pushMessageToFirebase("You: $userMessage")

        // Use coroutine to perform the model interaction in the background
        viewModelScope.launch {
            val botResponse = generateBotResponse(userMessage)
            chatMessages.add("Sarah: $botResponse") // Add the bot's response

            // Push the bot's response to Firebase
            pushMessageToFirebase("Bot: $botResponse")
        }
    }

    // Method to interact with the Generative Model (like Gemini 1.5)
    private suspend fun generateBotResponse(userMessage: String): String? {
        // Initialize the GenerativeModel with the API key
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyAOcrbds273ISlkb3OmRX0WOzBZfMV5Kdc" // Replace with your actual API key
        )

        // Define the prompt
        val prompt = "You are an old spirit, called Sarah, who died sacrificing herself for your 2 children. Answer this: $userMessage"

        // Generate the response using the model
        val response = generativeModel.generateContent(prompt)

        return response.text
    }

    // Push a message to Firebase
    private fun pushMessageToFirebase(message: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val chatRef = database.getReference("chats/Sarah/$userId")
            chatRef.push().setValue(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the listener to prevent memory leaks
        messagesRef?.removeEventListener(listener)
    }
}
