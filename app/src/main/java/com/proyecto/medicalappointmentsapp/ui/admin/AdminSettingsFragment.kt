package com.proyecto.medicalappointmentsapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentAdminSettingsBinding // Nombre de layout correcto

class AdminSettingsFragment : Fragment() {

    private var _binding: FragmentAdminSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí irían las opciones de configuración para el administrador
        binding.textViewSettingsPlaceholder.text = "Aquí irán las configuraciones de administrador."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
