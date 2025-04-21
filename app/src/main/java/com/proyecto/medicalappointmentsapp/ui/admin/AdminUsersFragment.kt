package com.proyecto.medicalappointmentsapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentAdminUsersBinding // Nombre de layout correcto

class AdminUsersFragment : Fragment() {

    private var _binding: FragmentAdminUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUsersBinding.inflate(inflater, container, false)
        val view = binding.root
        // Aquí gestionarías la lista de todos los usuarios (pacientes, doctores)
        binding.textViewUsersPlaceholder.text = "Aquí se gestionarán los usuarios."
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
