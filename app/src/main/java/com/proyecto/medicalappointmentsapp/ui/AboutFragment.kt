package com.proyecto.medicalappointmentsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentAboutBinding // Asegúrate que el nombre del layout sea correcto
import com.proyecto.medicalappointmentsapp.databinding.FragmentAboutBinding.inflate

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    // Esta propiedad solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        val view = binding.root
        // Configura tus vistas aquí usando binding.nombreDeTuVista
        binding.textViewAboutContent.text = "Información acerca de la aplicación..." // Ejemplo
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpia la referencia al binding
    }
}