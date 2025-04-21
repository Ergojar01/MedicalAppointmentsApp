package com.proyecto.medicalappointmentsapp.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentDoctorPatientsBinding // Nombre de layout correcto

class DoctorPatientsFragment : Fragment() {

    private var _binding: FragmentDoctorPatientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorPatientsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí mostrarías la lista de pacientes asignados al doctor
        binding.textViewPatientsPlaceholder.text = "Aquí se mostrará la lista de pacientes."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}