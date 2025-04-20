package com.proyecto.medicalappointmentsapp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proyecto.medicalappointmentsapp.databinding.FragmentErrorBinding // Importa tu ViewBinding

class ErrorFragment : Fragment() {

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        // Aquí podrías obtener argumentos si pasas un mensaje de error específico
        // val errorMessage = arguments?.getString("error_message")
        // binding.textViewErrorMessage.text = errorMessage ?: "Error desconocido"

        // Setup para el botón de reintento (si lo añades)
        // binding.buttonRetry.setOnClickListener { /* Lógica de reintento */ }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}