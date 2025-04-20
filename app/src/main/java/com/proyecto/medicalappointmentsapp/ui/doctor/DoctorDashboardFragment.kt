package com.proyecto.medicalappointmentsapp.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentDoctorDashboardBinding // Importa tu ViewBinding

class DoctorDashboardFragment : Fragment() {

    private var _binding: FragmentDoctorDashboardBinding? = null
    private val binding get() = _binding!!

    // TODO: Inyectar DoctorDashboardViewModel
    // TODO: Inicializar Adapter para citas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorDashboardBinding.inflate(inflater, container, false)
        setupUI()
        observeViewModel()
        return binding.root
    }

    private fun setupUI() {
        // Configurar RecyclerView
        // TODO: setupRecyclerView()

        binding.buttonManageSchedule.setOnClickListener {
            // TODO: Navegar al fragmento/actividad de gestión de horario
        }
    }

    private fun observeViewModel() {
        // Observar LiveData para la lista de citas de hoy
        // TODO: viewModel.todayAppointments.observe(viewLifecycleOwner) { ... }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}