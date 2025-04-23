package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.databinding.FragmentPatientDashboardBinding

class PatientDashboardFragment : Fragment() {

    private var _binding: FragmentPatientDashboardBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val upcomingAppointments = mutableListOf<Appointment>()
    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClicks()
        loadUpcomingAppointments()
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(upcomingAppointments)
        binding.recyclerViewUpcomingAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUpcomingAppointments.adapter = appointmentAdapter
    }

    private fun setupClicks() {
        binding.buttonNewAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_nav_patient_dashboard_to_newAppointmentFragment)
        }
    }

    private fun loadUpcomingAppointments() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("appointments")
            .whereEqualTo("idPaciente", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                upcomingAppointments.clear()
                upcomingAppointments.addAll(snapshot.mapNotNull { it.toObject(Appointment::class.java) })
                appointmentAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar las citas", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
