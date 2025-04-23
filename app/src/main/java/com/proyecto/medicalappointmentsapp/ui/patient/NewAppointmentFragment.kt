package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.data.model.AppointmentStatus
import com.proyecto.medicalappointmentsapp.data.model.User
import com.proyecto.medicalappointmentsapp.data.model.UserRole
import com.proyecto.medicalappointmentsapp.databinding.FragmentNewAppointmentBinding
import java.util.*

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentNewAppointmentBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val doctorUsers = mutableListOf<User>()
    private var selectedDoctorUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDoctorSpinner()
        binding.buttonGuardar.setOnClickListener { saveAppointment() }
    }

    private fun setupDoctorSpinner() {
        val initialItems = mutableListOf("Cargando doctores...")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, initialItems)
        binding.spinnerDoctor.adapter = adapter

        // 🔧 Corrección importante aquí: se cambia "user" por "users"
        db.collection("users")
            .whereEqualTo("role", UserRole.DOCTOR.roleName)
            .get()
            .addOnSuccessListener { snapshot ->
                doctorUsers.clear()
                val doctorDisplayList = mutableListOf<String>()

                if (snapshot.isEmpty) {
                    Log.d("NewAppointmentFragment", "No doctors found in 'users' collection.")
                }

                snapshot.forEach { doc ->
                    val user = doc.toObject(User::class.java).copy(uid = doc.id)
                    if (user != null && user.role.equals(UserRole.DOCTOR.roleName, ignoreCase = true)) {
                        doctorUsers.add(user)
                        doctorDisplayList.add("Dr. ${user.nombre} – ${user.especialidad ?: "General"}")
                    } else if (user != null) {
                        Log.w("NewAppointmentFragment", "User ${doc.id} fetched but role is not Doctor: ${user.role}")
                    } else {
                        Log.w("NewAppointmentFragment", "Failed to convert document ${doc.id} to User object.")
                    }
                }

                adapter.clear()
                if (doctorDisplayList.isNotEmpty()) {
                    adapter.addAll(doctorDisplayList)
                } else {
                    adapter.add("No hay doctores disponibles")
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("NewAppointmentFragment", "Error loading doctors from 'users' collection", exception)
                adapter.clear()
                adapter.add("Error cargando doctores")
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Error al cargar doctores: ${exception.message}", Toast.LENGTH_LONG).show()
            }

        binding.spinnerDoctor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDoctorUser = doctorUsers.getOrNull(position)
                Log.d("NewAppointmentFragment", "Selected doctor: ${selectedDoctorUser?.nombre}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedDoctorUser = null
            }
        }
    }

    private fun saveAppointment() {
        val currentUser = auth.currentUser ?: run {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val doctorUser = selectedDoctorUser ?: run {
            Toast.makeText(requireContext(), "Selecciona un doctor", Toast.LENGTH_SHORT).show()
            return
        }

        val motivo = binding.editTextMotivo.text.toString().trim()
        if (motivo.isEmpty()) {
            binding.editTextMotivo.error = "Ingresa el motivo de la consulta"
            return
        } else {
            binding.editTextMotivo.error = null
        }

        val cita = Appointment(
            idPaciente = currentUser.uid,
            nombrePaciente = currentUser.displayName ?: "Nombre Paciente No Disponible",
            idDoctor = doctorUser.uid,
            nombreDoctor = doctorUser.nombre,
            motivoConsulta = motivo,
            estado = AppointmentStatus.PENDIENTE.statusName,
            fecha = Date(),
            fechaCreacion = Date()
        )

        binding.buttonGuardar.isEnabled = false

        db.collection("appointments")
            .add(cita)
            .addOnSuccessListener { documentReference ->
                Log.d("NewAppointmentFragment", "Appointment saved with ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "Cita solicitada correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                Log.e("NewAppointmentFragment", "Error saving appointment", e)
                Toast.makeText(requireContext(), "Error al solicitar cita: ${e.message}", Toast.LENGTH_LONG).show()
                binding.buttonGuardar.isEnabled = true
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
