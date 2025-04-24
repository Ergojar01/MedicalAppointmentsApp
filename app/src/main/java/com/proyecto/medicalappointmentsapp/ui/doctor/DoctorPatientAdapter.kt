package com.proyecto.medicalappointmentsapp.ui.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.medicalappointmentsapp.data.model.User // Modelo de datos para paciente/usuario
// CORRECCIÓN: Usar el Binding correcto generado por item_doctor_patient.xml
import com.proyecto.medicalappointmentsapp.databinding.ItemDoctorPatientBinding

// La clase se llama PatientListAdapter (como espera DoctorPatientsFragment)
class PatientListAdapter(
    // private val onPatientClick: (User) -> Unit // Descomenta si necesitas un listener de clic
) : ListAdapter<User, PatientListAdapter.PatientViewHolder>(PatientDiffCallback()) {

    // Implementación correcta de onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        // CORRECCIÓN: Usar ItemDoctorPatientBinding (generado por item_doctor_patient.xml)
        val binding = ItemDoctorPatientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Devuelve la instancia del ViewHolder correcto
        return PatientViewHolder(binding /*, onPatientClick */)
    }

    // Implementación correcta de onBindViewHolder
    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(getItem(position)) // Llama al bind del ViewHolder
    }

    // Definición correcta del ViewHolder interno
    // CORRECCIÓN: Hereda de RecyclerView.ViewHolder y usa ItemDoctorPatientBinding
    class PatientViewHolder(
        private val binding: ItemDoctorPatientBinding // CORRECCIÓN: Usa ItemDoctorPatientBinding
        // , private val onPatientClick: (User) -> Unit // Recibe el listener si existe
    ) : RecyclerView.ViewHolder(binding.root) {

        // Método para asignar datos del User a las vistas del layout ACTUALIZADO item_doctor_patient.xml
        fun bind(patient: User) {
            // CORRECCIÓN: Asigna los datos usando los IDs del layout ACTUALIZADO item_doctor_patient.xml
            binding.textViewPatientName.text = "${patient.nombre ?: ""} ${patient.nombre ?: ""}".trim()
            // Asumiendo que el User model tiene un campo email. Ajusta si es necesario.
            binding.textViewPatientEmail.text = patient.correo ?: "Email no disponible"

            // Ya no es necesario ocultar vistas si item_doctor_patient.xml solo tiene estos dos TextViews

            // Configura el click listener para todo el item si se proporciona
            // binding.root.setOnClickListener { onPatientClick(patient) }
        }
    }

    // Implementación de DiffUtil.ItemCallback para el modelo User (sin cambios aquí)
    class PatientDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}