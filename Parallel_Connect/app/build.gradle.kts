plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.parallel_connect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.parallel_connect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation("androidx.compose.material3:material3:1.0.1") // Ou a versão mais recente
    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation ("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")
    // add the dependency for the Google AI client SDK for Android
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // Para chamadas de rede com OkHttp
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

// Para JSON (se necessário)
    implementation ("org.json:json:20230618")

// Para gerenciamento de estado no Jetpack Compose
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    implementation ("com.google.firebase:firebase-auth:21.3.0")
    implementation (libs.androidx.navigation.compose.v260)

    implementation("com.google.maps.android:android-maps-utils:2.2.6")

    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.play.services.location)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}