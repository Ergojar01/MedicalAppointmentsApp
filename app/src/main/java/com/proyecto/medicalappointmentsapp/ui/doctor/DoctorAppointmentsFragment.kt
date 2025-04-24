package com.proyecto.medicalappointmentsapp.ui.doctor

import android.icu.text.SimpleDateFormat // Import para formato de fecha
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast // Import para mensajes Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.medicalappointmentsapp.data.model.Appointment // Import del modelo Appointment
import com.proyecto.medicalappointmentsapp.data.model.AppointmentStatus // Import del Enum Status
import com.proyecto.medicalappointmentsapp.databinding.FragmentDoctorAppointmentsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale // Import para Locale

@AndroidEntryPoint // Asegúrate que esta anotación esté presente si usas Hilt para el ViewModel
class DoctorAppointmentsFragment : Fragment() {

    private var _binding: FragmentDoctorAppointmentsBinding? = null
    private val binding get() = _binding!!

    // Usar el ViewModel correcto inyectado por Hilt
    private val viewModel: DoctorAppointmentsViewModel by viewModels()

    // Nombre de variable y tipo de adaptador consistentes
    private lateinit var appointmentListAdapter: DoctorAppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el Binding correcto
        _binding = FragmentDoctorAppointmentsBinding.inflate(inflater, container, false)
        setupRecyclerView() // Configurar RecyclerView (ahora instancia el Adapter correctamente)
        observeViewModel() // Observar cambios del ViewModel

        // Llamar a la función para cargar las citas pendientes al crear la vista
        viewModel.loadPendingAppointments()

        return binding.root
    }

    private fun setupRecyclerView() {
        // 1. Define el formato de fecha que quieres usar
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        // 2. Define qué hacer al aceptar (llama al ViewModel)
        val acceptAction: (Appointment) -> Unit = { appointment ->
            viewModel.updateAppointmentStatus(appointment.idCita, AppointmentStatus.CONFIRMADA) // O el estado apropiado
            Toast.makeText(context, "Cita Aceptada: ${appointment.nombrePaciente}", Toast.LENGTH_SHORT).show()
        }

        // 3. Define qué hacer al rechazar (llama al ViewModel)
        val rejectAction: (Appointment) -> Unit = { appointment ->
            viewModel.updateAppointmentStatus(appointment.idCita, AppointmentStatus.CANCELADA_DOCTOR) // O el estado apropiado
            Toast.makeText(context, "Cita Rechazada: ${appointment.nombrePaciente}", Toast.LENGTH_SHORT).show()
        }

        // 4. CORRECCIÓN: Pasa los parámetros al constructor del adaptador
        appointmentListAdapter = DoctorAppointmentAdapter(dateFormat, acceptAction, rejectAction)

        // Asigna el adaptador y layout manager al RecyclerView
        binding.recyclerViewAppointments.apply {
            adapter = appointmentListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        // Observar la lista de citas pendientes
        viewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            appointmentListAdapter.submitList(appointments)
            // Mostrar/ocultar vistas según si la lista está vacía
            val isEmpty = appointments.isNullOrEmpty()
            binding.textViewNoAppointments.visibility = if (isEmpty && !viewModel.isLoading.value!!) View.VISIBLE else View.GONE // Solo mostrar si no está cargando
            binding.recyclerViewAppointments.visibility = if (isEmpty || viewModel.isLoading.value!!) View.GONE else View.VISIBLE // Ocultar si está vacío o cargando
        }

        // Observar el estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarAppointments.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Ocultar RecyclerView y texto "sin citas" mientras carga
            if (isLoading) {
                binding.recyclerViewAppointments.visibility = View.GONE
                binding.textViewNoAppointments.visibility = View.GONE
            } else {
                // Re-evaluar visibilidad cuando termina la carga
                val isEmpty = viewModel.appointments.value.isNullOrEmpty()
                binding.textViewNoAppointments.visibility = if (isEmpty) View.VISIBLE else View.GONE
                binding.recyclerViewAppointments.visibility = if (isEmpty) View.GONE else View.VISIBLE
            }
        }

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null && errorMsg.isNotEmpty()) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                viewModel.clearError() // Limpiar error después de mostrarlo (Opcional, pero recomendado)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar referencias para evitar memory leaks
        binding.recyclerViewAppointments.adapter = null // Importante para RecyclerView
        _binding = null
    }
}