package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentPatientProfileBinding // Nombre de layout correcto

class PatientProfileFragment : Fragment() {

    private var _binding: FragmentPatientProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí cargarías y mostrarías los datos del perfil del paciente
        binding.textViewProfilePlaceholder.text = "Aquí se mostrarán los datos del perfil."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}