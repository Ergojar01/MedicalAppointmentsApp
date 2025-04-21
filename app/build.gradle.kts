plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.androidx.navigation.safeargs) // ✅ Safe Args plugin (sin .kotlin)
    id("kotlin-kapt") // ✅ Necesario para Data Binding
}

android {
    namespace = "com.proyecto.medicalappointmentsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.proyecto.medicalappointmentsapp"
        minSdk = 24
        targetSdk = 35
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
        dataBinding = true // ✅ Habilitar Data Binding
    }
}

dependencies {
    // --- Core y UI ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.drawerlayout) // ✅ DrawerLayout
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // --- Firebase ---
    implementation(platform(libs.firebase.bom)) // ✅ Firebase BoM
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // --- Arquitectura (ViewModel, LiveData, Scope) ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services) // ✅ útil si usás Tasks.await()

    // --- Navigation ---
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
