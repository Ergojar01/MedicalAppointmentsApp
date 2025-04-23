package com.proyecto.medicalappointmentsapp.ui.auth

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ajustar ventana al mostrar teclado
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        // Inicializar ViewBinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hacer clicables los enlaces en el CheckBox
        binding.chkTerms.movementMethod = LinkMovementMethod.getInstance()

        auth = FirebaseAuth.getInstance()

        // Listener del botón Crear cuenta
        binding.btnRegister.setOnClickListener {
            performValidationAndRegister()
        }
    }

    private fun performValidationAndRegister() {
        // Limpiar errores
        binding.tilName.error = null
        binding.tilLastName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirm.error = null

        val name      = binding.etName.text.toString().trim()
        val lastName  = binding.etLastName.text.toString().trim()
        val username  = binding.etUsername.text.toString().trim()
        val email     = binding.etEmail.text.toString().trim()
        val password  = binding.etPassword.text.toString()
        val confirm   = binding.etConfirm.text.toString()
        val agreed    = binding.chkTerms.isChecked

        // Validaciones
        if (name.isEmpty()) {
            binding.tilName.error = "Obligatorio"
            binding.etName.requestFocus()
            return
        }
        if (lastName.isEmpty()) {
            binding.tilLastName.error = "Obligatorio"
            binding.etLastName.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Correo inválido"
            binding.etEmail.requestFocus()
            return
        }
        if (password.length < 8) {
            binding.tilPassword.error = "Mínimo 8 caracteres"
            binding.etPassword.requestFocus()
            return
        }
        if (password != confirm) {
            binding.tilConfirm.error = "No coincide"
            binding.etConfirm.requestFocus()
            return
        }
        if (!agreed) {
            Toast.makeText(this, "Debes aceptar términos y privacidad", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostrar carga y registrar
        showLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Error en registro: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addOnCompleteListener
                }

                // Guardar perfil en Firestore
                val uid = auth.currentUser!!.uid
                val userMap = hashMapOf(
                    "name" to name,
                    "lastName" to lastName,
                    "username" to username.ifEmpty { null },
                    "email" to email
                )
                db.collection("users").document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        showLoading(false)
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        showLoading(false)
                        Toast.makeText(
                            this,
                            "Error guardando perfil: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !isLoading
    }
}
