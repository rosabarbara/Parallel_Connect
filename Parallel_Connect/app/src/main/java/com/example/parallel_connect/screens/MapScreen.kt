package com.example.parallel_connect.screens

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.parallel_connect.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import java.io.File
import java.io.FileOutputStream
import android.net.Uri
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mapScreen(navController: NavController) {
    val context = LocalContext.current

    // State to track permission status
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    // Check and request permission
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parallel Connect") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Photo Gallery") },
                                onClick = {
                                    expanded = false
                                    navController.navigate("photo_gallery")
                                }
                            )
                            // Add more menu options here if needed
                        }
                        // Add more menu options here if needed
                    }
                }
            )
        }
    ) { innerPadding ->
        if (hasLocationPermission) {
            MapContent(navController, Modifier.padding(innerPadding))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Location permission is required to display the map.", color = Color.Red)
            }
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun MapContent(navController: NavController, modifier: Modifier = Modifier) { // Pass navController to handle navigation
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val caughtSpiritsRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("caughtSpirits")
        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "defaultUser")
    val databaseRef = FirebaseDatabase.getInstance().reference
        .child("user_photos")
        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "defaultUser")

    // CameraPositionState initialized with a fallback position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f) // Default to San Francisco
    }

    // State to manage photo actions
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        photoBitmap = bitmap
    }

    // Fetch current location using Fused Location Provider
    LaunchedEffect(Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val newLocation = LatLng(location.latitude, location.longitude)
                currentLocation = newLocation

                // Move the camera to the user's location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(newLocation, 15f)
            }
        }
    }

    // Load the custom PNG icon in LaunchedEffect to make sure resources are available
    var iconGhost by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var iconShoes by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(Unit) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ghost)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false) // Resize to 100x100 px
        iconGhost = BitmapDescriptorFactory.fromBitmap(scaledBitmap) // Set the custom icon
    }
    LaunchedEffect(Unit) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.shoes)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false) // Resize to 100x100 px
        iconShoes = BitmapDescriptorFactory.fromBitmap(scaledBitmap) // Set the custom icon
    }
    

    Box(modifier = modifier.fillMaxSize()) {
        // Google Map with test spirit marker
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true
            ),
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            currentLocation?.let { location ->
                // Add a test marker slightly offset from the user's current location
                val JeffLocation = LatLng(
                    location.latitude + 0.001,
                    location.longitude + 0.001
                ) // Offset by ~111 meters
                val MartinLocation = LatLng(location.latitude + 0.001, location.longitude)
                val SarahLocation = LatLng(location.latitude - 0.001, location.longitude - 0.001)
                val shoeslocation = LatLng(location.latitude - 0.001, location.longitude)


                iconGhost?.let {
                    Marker(
                        state = rememberMarkerState(position = JeffLocation),
                        title = "Spirit",
                        snippet = "Catch me!",
                        icon = it, // Use the custom icon
                        onClick = {
                            // Save the caught spirit to Firebase
                            caughtSpiritsRef.child("Jeff").setValue(true)
                            navController.navigate("spirit_found/Jeff")
                            true // Return true to consume the click event
                        }
                    )
                }
                iconGhost?.let {
                    Marker(
                        state = rememberMarkerState(position = MartinLocation),
                        title = "Spirit",
                        snippet = "Catch me!",
                        icon = it, // Use the custom icon
                        onClick = {
                            // Save the caught spirit to Firebase
                            caughtSpiritsRef.child("Martin").setValue(true)
                            navController.navigate("spirit_found/Martin")
                            true // Return true to consume the click event
                        }
                    )
                }
                iconGhost?.let {
                    Marker(
                        state = rememberMarkerState(position = SarahLocation),
                        title = "Spirit",
                        snippet = "Catch me!",
                        icon = it, // Use the custom icon
                        onClick = {
                            // Save the caught spirit to Firebase
                            caughtSpiritsRef.child("Sarah").setValue(true)
                            navController.navigate("spirit_found/Sarah")
                            true // Return true to consume the click event
                        }
                    )
                }


                iconShoes?.let {
                    Marker(
                        state = rememberMarkerState(position = shoeslocation),
                        title = "shoes",
                        snippet = "Catch me!",
                        icon = it, // Use the custom icon
                        onClick = {
                            navController.navigate(Screens.ForumShoes.route)
                            true // Return true to consume the click event
                        }
                    )
                }
            }
        }

        // Overlay for photo preview and options
        photoBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Photo Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            photoBitmap?.let { bitmap ->
                                // Save photo to local storage
                                val filename = "photo_${System.currentTimeMillis()}.jpg"
                                val file = File(context.filesDir, filename)

                                try {
                                    // Write the bitmap to the file
                                    val outputStream = FileOutputStream(file)
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                    outputStream.flush()
                                    outputStream.close()

                                    // Get the URI of the saved file
                                    val fileUri = Uri.fromFile(file)

                                    // Save the URI in the Firebase Realtime Database
                                    val photoRef = databaseRef.push()
                                    photoRef.setValue(fileUri.toString()) // Save URI as a string in the database

                                    // Show a success message
                                    Toast.makeText(context, "Photo saved successfully!", Toast.LENGTH_SHORT).show()

                                    // Clear the photoBitmap after saving
                                    photoBitmap = null
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "Error saving photo: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Text("Save")
                        }

                        Button(onClick = {
                            photoBitmap = null
                            cameraLauncher.launch(null)
                        }) {
                            Text("Retake")
                        }
                    }
                }
            }
        }

        // Bottom buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { cameraLauncher.launch(null) }, modifier = Modifier.weight(1f)) {
                Text("Take Photo")
            }
        }


    }
}