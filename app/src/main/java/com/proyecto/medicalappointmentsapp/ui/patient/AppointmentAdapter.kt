package com.proyecto.medicalappointmentsapp.ui.patient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.databinding.ItemAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    private val appointments: List<Appointment>
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AppointmentViewHolder(binding)
    }

    override fun getItemCount(): Int = appointments.size

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    inner class AppointmentViewHolder(
        private val binding: ItemAppointmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: Appointment) {
            binding.textViewDoctor.text = "Dr. ${appointment.nombreDoctor}"
            binding.textViewFecha.text = formatDate(appointment.fecha)
        }

        private fun formatDate(date: Date?): String {
            return date?.let {
                SimpleDateFormat("dd/MM/yyyy – hh:mm a", Locale.getDefault()).format(it)
            } ?: "Sin fecha"
        }
    }
}
