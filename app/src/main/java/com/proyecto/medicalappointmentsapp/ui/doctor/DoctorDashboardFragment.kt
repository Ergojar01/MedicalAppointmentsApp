package com.proyecto.medicalappointmentsapp.ui.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.medicalappointmentsapp.R // Importar R
import com.proyecto.medicalappointmentsapp.databinding.FragmentDoctorDashboardBinding
import com.proyecto.medicalappointmentsapp.viewmodel.UserViewModel // Podrías usar este o crear uno específico
import java.text.SimpleDateFormat // O el import de android.icu si usas API >= 24
import java.util.Locale

class DoctorDashboardFragment : Fragment() {

    private var _binding: FragmentDoctorDashboardBinding? = null
    private val binding get() = _binding!!

    // Puedes usar UserViewModel para obtener datos generales del doctor o crear un ViewModel específico
    private val userViewModel: UserViewModel by viewModels()
    // TODO: Crear e inyectar un DoctorDashboardViewModel si necesitas lógica más compleja (ej. citas de hoy)
    // private val dashboardViewModel: DoctorDashboardViewModel by viewModels()

    // TODO: Si muestras citas de hoy, necesitarás un Adapter
    // private lateinit var todayAppointmentsAdapter: DoctorAppointmentAdapter // O uno específico

    // Formato de fecha (si lo necesitas aquí)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorDashboardBinding.inflate(inflater, container, false)
        setupUI()
        observeViewModels()
        // Cargar datos necesarios al crear la vista
        // userViewModel.loadUserDetails(FirebaseAuth.getInstance().currentUser!!.uid) // Asegúrate que el usuario no sea nulo
        // dashboardViewModel.loadTodayAppointments() // Si tienes este ViewModel
        return binding.root
    }

    private fun setupUI() {
        // Configurar RecyclerView para citas de hoy (si aplica)
        // setupTodayAppointmentsRecyclerView()

        // Botón para gestionar horario
        binding.buttonManageSchedule.setOnClickListener {
            // Navegar al fragmento de gestión de horario
            findNavController().navigate(R.id.doctorScheduleFragment) // Asegúrate que este ID exista en nav_graph
        }

        // Placeholder para la lista de citas de hoy
        binding.textViewTodayAppointmentsLabel.text = "Citas de Hoy (Implementación Pendiente)"
        binding.recyclerViewTodayAppointments.visibility = View.GONE // Ocultar hasta implementar
    }

    // TODO: Implementar si muestras citas de hoy
    /*
    private fun setupTodayAppointmentsRecyclerView() {
        todayAppointmentsAdapter = DoctorAppointmentAdapter(dateFormat, onAcceptClick = {}, onRejectClick = {}) // O un adapter diferente
        binding.recyclerViewTodayAppointments.apply {
            adapter = todayAppointmentsAdapter
            layoutManager = LinearLayoutManager(context)
            visibility = View.VISIBLE // Hacer visible
        }
    }
    */

    private fun observeViewModels() {
        // Observar datos del usuario (ej. nombre para bienvenida)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.textViewWelcomeDoctor.text = "Bienvenido/a, Dr./Dra. ${user.nombre ?: ""}!"
            } else {
                binding.textViewWelcomeDoctor.text = "Bienvenido/a, Doctor/a!"
            }
        }
        userViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, "Error cargando datos: $error", Toast.LENGTH_SHORT).show()
                userViewModel.clearError()
            }
        }

        // TODO: Observar ViewModel del Dashboard (ej. todayAppointments, isLoading, error)
        /*
        dashboardViewModel.todayAppointments.observe(viewLifecycleOwner) { appointments ->
             todayAppointmentsAdapter.submitList(appointments)
             // Podrías mostrar/ocultar un TextView si no hay citas hoy
        }
        dashboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
             // Mostrar/ocultar un ProgressBar específico para esta lista
        }
        dashboardViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
             if (errorMsg != null) {
                  Toast.makeText(context, "Error citas de hoy: $errorMsg", Toast.LENGTH_SHORT).show()
                  dashboardViewModel.clearError() // Suponiendo que existe este método
             }
        }
        */
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewTodayAppointments.adapter = null // Limpiar adapter
        _binding = null
    }
}