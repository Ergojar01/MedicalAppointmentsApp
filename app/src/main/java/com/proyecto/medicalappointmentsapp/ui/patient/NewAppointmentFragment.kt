package com.proyecto.medicalappointmentsapp.ui.patient

import android.os.Bundle
import android.util.Log // Asegúrate de importar Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.data.model.Appointment
import com.proyecto.medicalappointmentsapp.data.model.AppointmentStatus
// import com.proyecto.medicalappointmentsapp.data.model.Doctor // <- Ya no se necesita Doctor aquí
import com.proyecto.medicalappointmentsapp.data.model.User // <- Se necesita User
import com.proyecto.medicalappointmentsapp.data.model.UserRole // <- Se necesita UserRole
import com.proyecto.medicalappointmentsapp.databinding.FragmentNewAppointmentBinding
import java.util.*

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentNewAppointmentBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Lista para almacenar los User que son doctores
    private val doctorUsers = mutableListOf<User>()
    private var selectedDoctorUser: User? = null // Variable para el User (doctor) seleccionado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDoctorSpinner() // Configura el spinner con la nueva lógica
        binding.buttonGuardar.setOnClickListener { saveAppointment() } // Configura el botón de guardar
    }

    private fun setupDoctorSpinner() {
        val initialItems = mutableListOf("Cargando doctores...")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, initialItems)
        binding.spinnerDoctor.adapter = adapter

        // ----- Línea Modificada -----
        // Consulta a la colección 'user' (en minúscula) filtrando por rol "Doctor"
        db.collection("user")
            .whereEqualTo("role", UserRole.DOCTOR.roleName) // Busca usuarios con rol "Doctor"
            // --------------------------
            .get()
            .addOnSuccessListener { snapshot ->
                doctorUsers.clear() // Limpia la lista antes de llenarla
                val doctorDisplayList = mutableListOf<String>() // Lista para mostrar en el spinner

                if (snapshot.isEmpty) {
                    Log.d("NewAppointmentFragment", "No doctors found in 'user' collection.")
                }

                snapshot.forEach { doc ->
                    // Convierte el documento a User y añade el UID del documento
                    val user = doc.toObject(User::class.java).copy(uid = doc.id)
                    // Asegúrate que la conversión fue exitosa y el rol es correcto
                    if (user != null && user.role.equals(UserRole.DOCTOR.roleName, ignoreCase = true)) {
                        doctorUsers.add(user)
                        // Formato para mostrar en el spinner (Nombre - Especialidad)
                        doctorDisplayList.add("Dr. ${user.nombre} – ${user.especialidad ?: "General"}")
                    } else if (user != null) {
                        Log.w("NewAppointmentFragment", "User ${doc.id} fetched but role is not Doctor: ${user.role}")
                    } else {
                        Log.w("NewAppointmentFragment", "Failed to convert document ${doc.id} to User object.")
                    }
                }

                adapter.clear() // Limpia los items iniciales ("Cargando...")
                if (doctorDisplayList.isNotEmpty()) {
                    adapter.addAll(doctorDisplayList) // Añade la lista formateada
                } else {
                    adapter.add("No hay doctores disponibles") // Mensaje si no se encontraron doctores
                }
                adapter.notifyDataSetChanged() // Notifica al adapter que los datos cambiaron
            }
            .addOnFailureListener { exception ->
                // Manejo de error si la consulta falla
                Log.e("NewAppointmentFragment", "Error loading doctors from 'user' collection", exception)
                adapter.clear()
                adapter.add("Error cargando doctores")
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Error al cargar doctores: ${exception.message}", Toast.LENGTH_LONG).show()
            }

        // Listener para cuando se selecciona un item en el spinner
        binding.spinnerDoctor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Actualiza la variable 'selectedDoctorUser' con el User correspondiente a la posición
                selectedDoctorUser = doctorUsers.getOrNull(position)
                Log.d("NewAppointmentFragment", "Selected doctor: ${selectedDoctorUser?.nombre}")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Si no se selecciona nada, la variable se pone a null
                selectedDoctorUser = null
            }
        }
    }


    private fun saveAppointment() {
        // Obtiene el usuario actual autenticado
        val currentUser = auth.currentUser ?: run {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        // Verifica que se haya seleccionado un doctor desde el spinner
        val doctorUser = selectedDoctorUser ?: run {
            Toast.makeText(requireContext(), "Selecciona un doctor", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtiene el motivo de la consulta del EditText
        val motivo = binding.editTextMotivo.text.toString().trim()
        if (motivo.isEmpty()) {
            binding.editTextMotivo.error = "Ingresa el motivo de la consulta" // Muestra error en el EditText
            return
        } else {
            binding.editTextMotivo.error = null // Limpia error si estaba puesto
        }

        // Crea el objeto Appointment con los datos correctos
        val cita = Appointment(
            idPaciente = currentUser.uid, // UID del paciente logueado
            nombrePaciente = currentUser.displayName ?: "Nombre Paciente No Disponible", // Considera obtener nombre de 'user'
            idDoctor = doctorUser.uid, // UID del doctor seleccionado (desde la colección 'user')
            nombreDoctor = doctorUser.nombre, // Nombre del doctor (desde la colección 'user')
            motivoConsulta = motivo, // Motivo ingresado
            estado = AppointmentStatus.PENDIENTE.statusName, // Estado inicial Pendiente
            fecha = Date(), // Considera usar DatePicker/TimePicker
            fechaCreacion = Date() // Fecha de creación del documento
        )

        // Deshabilitar botón para evitar doble click mientras guarda
        binding.buttonGuardar.isEnabled = false
        // Opcional: Mostrar un ProgressBar

        // Guarda la nueva cita en la colección 'appointments'
        db.collection("appointments")
            .add(cita)
            .addOnSuccessListener { documentReference ->
                Log.d("NewAppointmentFragment", "Appointment saved with ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "Cita solicitada correctamente", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Regresa a la pantalla anterior
            }
            .addOnFailureListener { e ->
                Log.e("NewAppointmentFragment", "Error saving appointment", e)
                Toast.makeText(requireContext(), "Error al solicitar cita: ${e.message}", Toast.LENGTH_LONG).show()
                binding.buttonGuardar.isEnabled = true // Habilitar botón de nuevo si falla
                // Opcional: Ocultar ProgressBar
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Importante para evitar memory leaks con ViewBinding
    }
}