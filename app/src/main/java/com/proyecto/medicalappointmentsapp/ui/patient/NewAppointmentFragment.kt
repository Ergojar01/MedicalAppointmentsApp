package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
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
import com.proyecto.medicalappointmentsapp.data.model.Doctor
import com.proyecto.medicalappointmentsapp.databinding.FragmentNewAppointmentBinding
import java.util.*

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentNewAppointmentBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val doctors = mutableListOf<Doctor>()
    private var selectedDoctor: Doctor? = null

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

        db.collection("doctors")
            .get()
            .addOnSuccessListener { snapshot ->
                doctors.clear()
                snapshot.forEach { doc ->
                    val doctor = doc.toObject(Doctor::class.java)
                    doctors.add(doctor)
                }
                val displayList = doctors.map { "Dr. ${it.nombreDoctor} – ${it.especialidad}" }
                adapter.clear()
                adapter.addAll(displayList)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                adapter.clear()
                adapter.add("Error cargando doctores")
                adapter.notifyDataSetChanged()
            }

        binding.spinnerDoctor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDoctor = doctors.getOrNull(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedDoctor = null
            }
        }
    }


    private fun saveAppointment() {
        val user = auth.currentUser ?: run {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val doctor = selectedDoctor ?: run {
            Toast.makeText(requireContext(), "Selecciona un doctor", Toast.LENGTH_SHORT).show()
            return
        }

        val motivo = binding.editTextMotivo.text.toString().trim()
        if (motivo.isEmpty()) {
            Toast.makeText(requireContext(), "Ingresa el motivo de la consulta", Toast.LENGTH_SHORT).show()
            return
        }

        val cita = Appointment(
            idPaciente = user.uid,
            nombrePaciente = user.displayName ?: "Paciente",
            idDoctor = doctor.idDoctor,
            nombreDoctor = doctor.nombreDoctor,
            motivoConsulta = motivo,
            estado = AppointmentStatus.PENDIENTE.statusName,
            fecha = Date(),
            fechaCreacion = Date()
        )


        db.collection("appointments")
            .add(cita)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Cita guardada correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar cita", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
