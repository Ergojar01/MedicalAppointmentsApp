package com.proyecto.medicalappointmentsapp.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Appointment(
    @DocumentId val idCita: String = "",
    @ServerTimestamp val fecha: Date? = null,
    val estado: String = AppointmentStatus.PENDIENTE.statusName,
    val idPaciente: String = "",
    val nombrePaciente: String = "",
    val idDoctor: String = "",
    val nombreDoctor: String = "",
    val motivoConsulta: String? = null,
    val notasDoctor: String? = null,
    @ServerTimestamp val fechaCreacion: Date? = null
) : Parcelable

enum class AppointmentStatus(val statusName: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ATENDIDA("Atendida"),
    CANCELADA_PACIENTE("Cancelada_Paciente"),
    CANCELADA_DOCTOR("Cancelada_Doctor"),
    NO_ASISTIO("No_Asistio");

    companion object {
        fun fromString(status: String?): AppointmentStatus? {
            return entries.find { it.statusName.equals(status, ignoreCase = true) }
        }
    }
}
