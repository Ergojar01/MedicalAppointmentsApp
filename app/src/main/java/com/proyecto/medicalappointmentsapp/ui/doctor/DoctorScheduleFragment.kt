package com.proyecto.medicalappointmentsapp.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentDoctorScheduleBinding // Nombre de layout correcto

class DoctorScheduleFragment : Fragment() {

    private var _binding: FragmentDoctorScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorScheduleBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí mostrarías la agenda del doctor (quizás un calendario o lista de citas)
        binding.textViewSchedulePlaceholder.text = "Aquí se mostrará la agenda del doctor."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}