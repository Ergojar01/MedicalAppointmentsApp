package com.proyecto.medicalappointmentsapp.ui.doctor

import android.icu.text.SimpleDateFormat // Usar import correcto para SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.databinding.ItemDoctorAppointmentBinding

// Nombre de archivo y clase correctos
class DoctorAppointmentAdapter(
    private val dateFormat: SimpleDateFormat,
    private val onAcceptClick: (Appointment) -> Unit,
    private val onRejectClick: (Appointment) -> Unit
) : ListAdapter<Appointment, DoctorAppointmentAdapter.AppointmentViewHolder>(AppointmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemDoctorAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding, dateFormat, onAcceptClick, onRejectClick) // Pasar lambdas al ViewHolder
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Simplificado: no necesita doble anidación
    class AppointmentViewHolder(
        private val binding: ItemDoctorAppointmentBinding,
        private val dateFormat: SimpleDateFormat, // Recibir formato
        private val onAcceptClick: (Appointment) -> Unit, // Recibir lambdas
        private val onRejectClick: (Appointment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // El método bind ahora solo necesita la cita, las acciones se configuran una vez
        fun bind(appointment: Appointment) {
            binding.textViewPatientNameValue.text = appointment.nombrePaciente ?: "N/A"
            // Asegurarse de manejar fecha nula
            binding.textViewAppointmentDateValue.text = appointment.fecha?.let { dateFormat.format(it) } ?: "Sin fecha asignada"
            binding.textViewAppointmentReasonValue.text = appointment.motivoConsulta ?: "Sin motivo especificado"

            // Configurar listeners aquí usando las lambdas recibidas
            binding.buttonAccept.setOnClickListener {
                onAcceptClick(appointment)
            }

            binding.buttonReject.setOnClickListener {
                onRejectClick(appointment)
            }
        }
    }

    class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            // Comparación corregida usando idCita
            return oldItem.idCita == newItem.idCita
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            // Comparación completa del objeto
            return oldItem == newItem
        }
    }
}