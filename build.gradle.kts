// build.gradle.kts (raíz de MedicalAppointmentsApp)

plugins {
    // Android Gradle Plugin
    id("com.android.application")       version "8.8.0" apply false
    // Kotlin Android
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    // Parcelize
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.9.24" apply false
    // Navigation Safe Args
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
    // Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    // Google Services (Firebase)
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// ¡No pongas nada más aquí! Ni android {…}, ni dependencyResolutionManagement, ni dependencies {…}
