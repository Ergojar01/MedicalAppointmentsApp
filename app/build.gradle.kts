// app/build.gradle.kts

plugins {
    // El plugin Android ya declarado en el root
    id("com.android.application")
    // Kotlin Android
    id("org.jetbrains.kotlin.android")
    // Parcelize
    id("org.jetbrains.kotlin.plugin.parcelize")
    // Firebase / Google Services
    id("com.google.gms.google-services")
    // Navigation Safe Args
    id("androidx.navigation.safeargs.kotlin")
    // Hilt
    id("com.google.dagger.hilt.android")
    // KAPT para Hilt, Room, ...
    kotlin("kapt")
}

android {
    namespace   = "com.proyecto.medicalappointmentsapp"
    compileSdk  = 35
    defaultConfig {
        applicationId = "com.proyecto.medicalappointmentsapp"
        minSdk        = 24
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
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
    kotlinOptions { jvmTarget = "11" }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // — Core y UI —
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // — Firebase —
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)

    // — Lifecycle (ViewModel, LiveData) —
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // — Navigation —
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // — Coroutines —
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // — Room (opcional) —
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // — Hilt —
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // — Testing —
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
