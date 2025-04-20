package com.proyecto.medicalappointmentsapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentAdminDashboardBinding // Importa tu ViewBinding

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)

        binding.buttonManagePatients.setOnClickListener {
            // TODO: Navegar a la lista/gestión de pacientes
        }
        binding.buttonManageDoctors.setOnClickListener {
            // TODO: Navegar a la lista/gestión de doctores
        }
        binding.buttonViewAllAppointments.setOnClickListener {
            // TODO: Navegar a la lista de todas las citas
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}