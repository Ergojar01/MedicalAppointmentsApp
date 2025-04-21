package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentPatientAppointmentsBinding // Nombre de layout correcto

class PatientAppointmentsFragment : Fragment() {

    private var _binding: FragmentPatientAppointmentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientAppointmentsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí configurarías un RecyclerView para mostrar las citas, por ejemplo
        binding.textViewAppointmentsPlaceholder.text = "Aquí se mostrará la lista de citas del paciente."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}