// build.gradle.kts (app level)

plugins {
    // Usar alias del Version Catalog para todos los plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.parcelize) // Añadido desde el segundo snippet
    // Opcional pero recomendado si usas Navigation Component con Safe Args:
    // alias(libs.plugins.androidx.navigation.safeargs.kotlin)
}

android {
    namespace = "com.proyecto.medicalappointmentsapp"
    // Usa la última SDK estable disponible (ej. 34)
    compileSdk = 35 // AJUSTAR según la última SDK estable

    defaultConfig {
        applicationId = "com.proyecto.medicalappointmentsapp"
        minSdk = 24
        // Target SDK debería coincidir con Compile SDK
        targetSdk = 35 // AJUSTAR según la última SDK estable
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Considera ponerlo a 'true' para lanzamientos
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
        // Habilitar ViewBinding (recomendado)
        viewBinding = true
    }
    // Necesario si usas Safe Args
    // navigation {
    //     safeArgs = true
    // }
}

dependencies {

    // --- Core y UI ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx) // KTX para Activity (útil con ViewModels)
    implementation(libs.androidx.fragment.ktx) // KTX para Fragment (útil con ViewModels)

    // --- Firebase ---
    // Importa el BoM (Bill of Materials) - ¡ASEGÚRATE DE USAR LA ÚLTIMA VERSIÓN!
    // Revisa https://firebase.google.com/docs/android/setup#available-libraries
    implementation(platform(libs.firebase.bom)) // Asume alias 'firebase-bom' en libs.versions.toml

    // Autenticación (ya no necesita -ktx explícito con BoM generalmente)
    implementation(libs.firebase.auth)
    // Firestore
    implementation(libs.firebase.firestore)
    // Cloud Messaging (si necesitas notificaciones push)
    // implementation(libs.firebase.messaging)

    // Google Sign-In (necesario si usas autenticación con Google)
    // ¡ASEGÚRATE DE USAR LA ÚLTIMA VERSIÓN!
    implementation(libs.google.services.auth) // Asume alias 'google-services-auth' en libs.versions.toml

    // --- Componentes de Arquitectura (MVVM) ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx) // O runtime-ktx si prefieres StateFlow
    implementation(libs.androidx.lifecycle.runtime.ktx) // Para lifecycleScope

    // --- Coroutines ---
    // ¡ASEGÚRATE DE USAR LA ÚLTIMA VERSIÓN!
    implementation(libs.kotlinx.coroutines.core) // Coroutines core (puede ser transitiva)
    implementation(libs.kotlinx.coroutines.android) // Para Dispatchers.Main
    implementation(libs.kotlinx.coroutines.play.services) // Para integrar Tasks de Firebase/Play Services

    // --- Navegación (Recomendado) ---
    // ¡ASEGÚRATE DE USAR LA ÚLTIMA VERSIÓN!
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9") // Reemplaza con la última versión estable
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")     // Reemplaza con la última versión estable
    implementation(libs.androidx.activity)

    // --- Pruebas ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}