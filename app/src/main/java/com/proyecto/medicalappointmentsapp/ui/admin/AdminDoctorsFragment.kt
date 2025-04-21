package com.proyecto.medicalappointmentsapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentAdminDoctorsBinding // Nombre de layout correcto

class AdminDoctorsFragment : Fragment() {

    private var _binding: FragmentAdminDoctorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDoctorsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí gestionarías específicamente a los doctores
        binding.textViewDoctorsPlaceholder.text = "Aquí se gestionarán los doctores."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
