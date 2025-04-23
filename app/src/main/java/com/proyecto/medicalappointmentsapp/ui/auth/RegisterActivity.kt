package com.proyecto.medicalappointmentsapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.data.model.UserRole
import com.proyecto.medicalappointmentsapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, sysBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.buttonRegister.setOnClickListener { performRegistration() }
        binding.textViewLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performRegistration() {
        val name      = binding.editTextFullName.text.toString().trim()
        val email     = binding.editTextEmail.text.toString().trim()
        val password  = binding.editTextPassword.text.toString().trim()
        val confirmPw = binding.editTextConfirmPassword.text.toString().trim()

        when {
            name.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirmPw.isEmpty() -> {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return
            }
            password != confirmPw -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return
            }
            password.length < 6 -> {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return
            }
        }

        binding.progressBarRegister.visibility = android.view.View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    onFailure("UID inválido")
                    return@addOnSuccessListener
                }

                val userMap = mapOf(
                    "idUsuario" to uid,
                    "nombre"    to name,
                    "correo"    to email,
                    "role"      to UserRole.PATIENT.name
                )

                db.collection("users")
                    .document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        )
                        finish()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Error guardando datos: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onFailure("Error en registro: ${e.message}")
            }
    }

    private fun onFailure(message: String?) {
        binding.progressBarRegister.visibility = android.view.View.GONE
        Toast.makeText(this, message ?: "Ocurrió un error", Toast.LENGTH_LONG).show()
    }
}
