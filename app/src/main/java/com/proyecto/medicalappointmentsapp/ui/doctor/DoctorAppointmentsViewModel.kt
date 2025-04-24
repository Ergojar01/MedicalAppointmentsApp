package com.proyecto.medicalappointmentsapp.ui.doctor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.data.model.AppointmentStatus
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Clase ViewModel renombrada correctamente
class DoctorAppointmentsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> get() = _appointments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadPendingAppointments() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val doctorId = auth.currentUser?.uid
            if (doctorId == null) {
                Log.w("DoctorAppointmentsVM", "Doctor ID no encontrado.")
                _appointments.value = emptyList()
                _error.value = "Usuario no autenticado."
                _isLoading.value = false
                return@launch
            }

            try {
                Log.d("DoctorAppointmentsVM", "Cargando citas pendientes para Doctor ID: $doctorId")
                val snapshot = db.collection("appointments")
                    .whereEqualTo("idDoctor", doctorId)
                    .whereEqualTo("estado", AppointmentStatus.PENDIENTE.statusName)
                    .orderBy("fecha", Query.Direction.ASCENDING) // Ordenar por fecha ascendente
                    .get()
                    .await()

                // Mapeo corregido: @DocumentId se encarga de 'idCita'
                val citas = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject<Appointment>()
                    } catch (e: Exception) {
                        Log.e("DoctorAppointmentsVM", "Error parseando cita ${doc.id}", e)
                        null // Ignorar citas con errores de parseo
                    }
                }
                Log.d("DoctorAppointmentsVM", "Citas pendientes cargadas: ${citas.size}")
                _appointments.value = citas
            } catch (e: Exception) {
                Log.e("DoctorAppointmentsVM", "Error cargando citas pendientes", e)
                _appointments.value = emptyList()
                _error.value = "No se pudieron cargar las citas: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: AppointmentStatus) {
        if (appointmentId.isEmpty()) {
            Log.w("DoctorAppointmentsVM", "Intento de actualizar cita con ID vacío.")
            _error.value = "ID de cita inválido."
            return
        }
        // No modificamos isLoading aquí para no ocultar la lista mientras se actualiza un item
        viewModelScope.launch {
            _error.value = null // Limpiar error previo
            try {
                Log.d("DoctorAppointmentsVM", "Actualizando estado de cita $appointmentId a ${newStatus.statusName}")
                db.collection("appointments").document(appointmentId)
                    .update("estado", newStatus.statusName)
                    .await()
                // Volver a cargar la lista para reflejar el cambio
                loadPendingAppointments()
            } catch (e: Exception) {
                Log.e("DoctorAppointmentsVM", "Error actualizando estado de cita $appointmentId", e)
                _error.value = "Error al actualizar la cita: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}