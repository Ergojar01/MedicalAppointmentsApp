// build.gradle.kts (app level)
// REVISADO Y CORREGIDO - Usa Data Binding y limpia dependencias
// ASUME que tienes un archivo libs.versions.toml configurado.

plugins {
    // Usar alias del Version Catalog
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt") // *** NECESARIO para Data Binding ***
    alias(libs.plugins.androidx.navigation.safeargs.kotlin) // Habilitado como en el original
    // Considera añadir kotlin-parcelize si realmente lo necesitas y está en libs.toml
    // alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.proyecto.medicalappointmentsapp"
    compileSdk = 35 // Usar SDK estable (como en el original)

    defaultConfig {
        applicationId = "com.proyecto.medicalappointmentsapp"
        minSdk = 24
        targetSdk = 35 // Usar SDK estable (como en el original)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Cambiar a true para producción
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Java 11 está bien, considera 17 si usas AGP muy reciente
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11" // Coincidir con compileOptions
    }
    buildFeatures {
        dataBinding = true // *** CORREGIDO: Habilitar Data Binding ***
    }
    // Habilitar Safe Args como en el original
    navigation {
        safeArgs = true
    }
}

dependencies {

    // --- Core y UI ---
    // Asegúrate de que estos alias (ej. libs.androidx.core.ktx) existan en tu libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx) // KTX para Activity
    implementation(libs.androidx.fragment.ktx) // KTX para Fragment
    // Si necesitas DrawerLayout específicamente y no viene con Material/AppCompat:
    implementation(libs.androidx.drawerlayout) // Asume alias 'androidx-drawerlayout'

    // --- Firebase ---
    // Importa el BoM (Bill of Materials) - Gestiona versiones de Firebase
    implementation(platform(libs.firebase.bom)) // Asume alias 'firebase-bom'

    // Dependencias de Firebase (sin versión explícita aquí)
    implementation(libs.firebase.auth) // Asume alias 'firebase-auth'
    implementation(libs.firebase.firestore) // Asume alias 'firebase-firestore'
    // implementation(libs.firebase.database) // Si usas Realtime DB, descomenta y asume alias 'firebase-database'

    // --- Componentes de Arquitectura (MVVM) ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Asume alias 'androidx-lifecycle-viewmodel-ktx'
    implementation(libs.androidx.lifecycle.livedata.ktx) // Asume alias 'androidx-lifecycle-livedata-ktx'
    implementation(libs.androidx.lifecycle.runtime.ktx) // Asume alias 'androidx-lifecycle-runtime-ktx' // Para lifecycleScope

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.core) // Asume alias 'kotlinx-coroutines-core'
    implementation(libs.kotlinx.coroutines.android) // Asume alias 'kotlinx-coroutines-android' // Para Dispatchers.Main
    // implementation(libs.kotlinx.coroutines.play.services) // Descomentar si usas Tasks.await() con Firebase/Play Services, asume alias

    // --- Navegación ---
    implementation(libs.androidx.navigation.fragment.ktx) // Asume alias 'androidx-navigation-fragment-ktx'
    implementation(libs.androidx.navigation.ui.ktx) // Asume alias 'androidx-navigation-ui-ktx'

    // --- Pruebas ---
    testImplementation(libs.junit) // Asume alias 'junit'
    androidTestImplementation(libs.androidx.junit) // Asume alias 'androidx-junit'
    androidTestImplementation(libs.androidx.espresso.core) // Asume alias 'androidx-espresso-core'
}