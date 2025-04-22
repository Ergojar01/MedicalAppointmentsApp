package com.proyecto.medicalappointmentsapp.ui.auth



import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.proyecto.medicalappointmentsapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Redirigir si ya está logueado (opcional, mejor en una Splash/Main Activity)
        if (auth.currentUser != null) {
            navigateToMain()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mostrar progreso (ej: ProgressBar)
            binding.buttonLogin.isEnabled = false // Deshabilitar botón durante el proceso

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Ocultar progreso
                    binding.buttonLogin.isEnabled = true // Habilitar botón de nuevo

                    if (task.isSuccessful) {
                        // Login exitoso
                        Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                        navigateToMain()
                    } else {
                        // Si falla el login, muestra mensaje
                        Toast.makeText(baseContext, "Error de autenticación: ${task.exception?.message}",
                            Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.textViewRegister.setOnClickListener {
            // Ir a la actividad de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.textViewForgotPassword.setOnClickListener {
            // Implementar lógica de recuperación de contraseña si es necesario
            // Ejemplo:
            val email = binding.editTextEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Correo de recuperación enviado.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error al enviar correo: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ingrese su correo electrónico para recuperar la contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        // Limpiar el stack de actividades para que no pueda volver al login con el botón "back"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra LoginActivity
    }
}