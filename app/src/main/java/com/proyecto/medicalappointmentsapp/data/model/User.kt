package com.proyecto.medicalappointmentsapp.data.model
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class User(
    @DocumentId val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val role: String = "", // "Administrador", "Paciente", "Doctor"
    val telefono: String? = null,
    // Campos Paciente
    val historialMedico: String? = null,
    // Campos Doctor
    val especialidad: String? = null,
    val horarioDisponible: Map<String, List<String>>? = null
) : Parcelable

enum class UserRole(val roleName: String) {
    ADMIN("Administrador"),
    PATIENT("Paciente"),
    DOCTOR("Doctor");

    companion object {
        fun fromString(role: String?): UserRole? {
            // entries es una propiedad auto-generada para Enums desde Kotlin 1.9+
            // Si usas una versión anterior, reemplaza entries por values()
            return entries.find { it.roleName.equals(role, ignoreCase = true) }
        }
    }
}