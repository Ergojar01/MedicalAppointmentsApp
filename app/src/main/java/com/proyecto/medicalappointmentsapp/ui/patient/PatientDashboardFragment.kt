package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.databinding.FragmentPatientDashboardBinding // Importa tu ViewBinding

class PatientDashboardFragment : Fragment() {

    private var _binding: FragmentPatientDashboardBinding? = null
    private val binding get() = _binding!!

    // TODO: Inyectar el ViewModel apropiado (ej: PatientDashboardViewModel)
    // private val viewModel: PatientDashboardViewModel by viewModels()

    // TODO: Inicializar el Adapter para el RecyclerView
    // private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientDashboardBinding.inflate(inflater, container, false)
        setupUI()
        observeViewModel()
        return binding.root
    }

    private fun setupUI() {
        // Configurar RecyclerView (LayoutManager, Adapter)
        // TODO: setupRecyclerView()

        // Configurar clicks de botones
        binding.buttonRequestAppointment.setOnClickListener {
            // TODO: Navegar al fragmento/actividad para solicitar cita
            // findNavController().navigate(R.id.action_patientDashboard_to_requestAppointment)
        }

        binding.buttonViewDoctors.setOnClickListener {
            // TODO: Navegar al fragmento/actividad para ver doctores
            // findNavController().navigate(R.id.action_patientDashboard_to_doctorList)
        }
    }

    private fun observeViewModel() {
        // Observar LiveData del ViewModel para la lista de citas próximas
        // TODO: viewModel.upcomingAppointments.observe(viewLifecycleOwner) { appointments ->
        //     appointmentAdapter.submitList(appointments)
        // }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}