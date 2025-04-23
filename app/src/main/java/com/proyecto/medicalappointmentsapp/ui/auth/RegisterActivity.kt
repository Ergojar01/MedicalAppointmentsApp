package com.proyecto.medicalappointmentsapp.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge // De Cambios-Zebas
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // De master
import androidx.core.view.ViewCompat // De Cambios-Zebas
import androidx.core.view.WindowInsetsCompat // De Cambios-Zebas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.data.model.UserRole // De Cambios-Zebas
import com.proyecto.medicalappointmentsapp.databinding.ActivityRegisterBinding
import com.proyecto.medicalappointmentsapp.ui.MainActivity // Asumiendo que esta es la actividad principal tras registro/login (de Cambios-Zebas)
import com.proyecto.medicalappointmentsapp.ui.auth.LoginActivity // De Cambios-Zebas (para el link de login)


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Configuraciones iniciales combinadas ---
        enableEdgeToEdge() // De Cambios-Zebas
        window.setSoftInputMode( // De master
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Ajuste de Padding para Edge-to-Edge --- (De Cambios-Zebas)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, sysBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        // --- Configuración de Términos y Condiciones --- (De master)
        setupTermsAndPrivacyLink()

        // --- Listeners de botones --- (Combinado)
        // Asume que el botón de registro tiene ID 'btnRegister' y la barra de progreso 'progressRegister' (de master)
        binding.btnRegister.setOnClickListener { performRegistration() }
        // Asume que el TextView para ir a Login tiene ID 'textViewLoginLink' (de Cambios-Zebas)
        binding.textViewLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Cierra la actividad de registro si va a Login
        }
    }

    private fun setupTermsAndPrivacyLink() {
        val fullText = getString(R.string.terms_and_privacy_prompt) // Es mejor usar strings.xml
        // val fullText = "He leído y acepto los Términos y la Política de Privacidad" // Hardcoded como en 'master'
        val ss = SpannableString(fullText)

        // Términos
        val termsText = getString(R.string.terms) // "Términos"
        val termsUrl = getString(R.string.terms_url) // "https://policies.google.com/terms"
        val termsStart = fullText.indexOf(termsText)
        if (termsStart != -1) {
             ss.setSpan(object : ClickableSpan() {
                 override fun onClick(widget: View) {
                     startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl)))
                 }
                 override fun updateDrawState(ds: TextPaint) {
                     super.updateDrawState(ds)
                     ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.purple_500) // Asegúrate que este color existe
                     ds.isUnderlineText = false
                 }
             }, termsStart, termsStart + termsText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }


        // Política de Privacidad
        val privacyText = getString(R.string.privacy_policy) // "Política de Privacidad"
        val privacyUrl = getString(R.string.privacy_url) // "https://policies.google.com/privacy"
        val privStart = fullText.indexOf(privacyText)
         if (privStart != -1) {
             ss.setSpan(object : ClickableSpan() {
                 override fun onClick(widget: View) {
                     startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)))
                 }
                 override fun updateDrawState(ds: TextPaint) {
                     super.updateDrawState(ds)
                     ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.purple_500) // Asegúrate que este color existe
                     ds.isUnderlineText = false
                 }
             }, privStart, privStart + privacyText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
         }

        // Asume que el CheckBox tiene ID 'chkTerms' (de master)
        binding.chkTerms.text = ss
        binding.chkTerms.movementMethod = LinkMovementMethod.getInstance()
    }


    private fun performRegistration() {
        // --- Limpiar errores previos (de master) ---
        // Asume IDs de TextInputLayout: tilName, tilLastName, tilEmail, tilPhone, tilPassword, tilConfirm
        binding.tilName.error = null
        binding.tilLastName.error = null
        // binding.tilUsername.error = null // Añadir si existe un campo y TIL para username
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilPassword.error = null
        binding.tilConfirm.error = null

        // --- Obtener datos de entrada (de master) ---
        // Asume IDs de EditText: etName, etLastName, etUsername, etEmail, etPhone, etPassword, etConfirm
        val name     = binding.etName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim() // Opcional, si existe
        val email    = binding.etEmail.text.toString().trim()
        val phone    = binding.etPhone.text.toString().trim()
        val pass     = binding.etPassword.text.toString()
        val confirm  = binding.etConfirm.text.toString()
        val agreed   = binding.chkTerms.isChecked

        // --- Validaciones (combinadas, priorizando las de master) ---
        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.error_required) // Usar strings.xml
            binding.etName.requestFocus()
            return
        }
        if (lastName.isEmpty()) {
            binding.tilLastName.error = getString(R.string.error_required)
            binding.etLastName.requestFocus()
            return
        }
        // Añadir validación para username si es necesario
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            binding.etEmail.requestFocus()
            return
        }
        if (phone.isEmpty()) { // Validación de teléfono de master
            binding.tilPhone.error = getString(R.string.error_required)
            binding.etPhone.requestFocus()
            return
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            binding.tilPhone.error = getString(R.string.error_invalid_phone)
            binding.etPhone.requestFocus()
            return
        }
        if (pass.length < 8) { // Longitud mínima de 'master' (era 6 en Cambios-Zebas)
            binding.tilPassword.error = getString(R.string.error_password_length, 8) // "Mínimo 8 caracteres"
            binding.etPassword.requestFocus()
            return
        }
        if (pass != confirm) {
            binding.tilConfirm.error = getString(R.string.error_password_mismatch)
            binding.etConfirm.requestFocus()
            return
        }
        if (!agreed) {
            Toast.makeText(this, getString(R.string.error_terms_not_accepted), Toast.LENGTH_SHORT).show()
            return
        }

        // --- Iniciar proceso de registro ---
        // Asume ID de ProgressBar: progressRegister (de master)
        binding.progressRegister.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false // Deshabilitar botón

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task -> // Usar addOnCompleteListener para manejar éxito y fallo
                if (!task.isSuccessful) {
                    // Fallo en la creación de usuario en Auth
                    onFailure("Error de registro: ${task.exception?.localizedMessage}")
                    return@addOnCompleteListener
                }

                // Éxito en Auth, ahora guardar datos en Firestore
                val uid = auth.currentUser?.uid
                if (uid == null) {
                    onFailure("Error: No se pudo obtener el ID de usuario.")
                    // Considerar eliminar el usuario de Auth si falla el guardado en Firestore? (más complejo)
                    return@addOnCompleteListener
                }

                // --- Crear mapa de usuario (combinado) ---
                // Incluye campos de 'master' y el 'role' de 'Cambios-Zebas'
                val userMap = mapOf(
                    "idUsuario" to uid,       // Clave de Cambios-Zebas
                    "name"      to name,      // De master
                    "lastName"  to lastName,  // De master
                    "username"  to username.ifEmpty { null }, // De master (opcional)
                    "email"     to email,     // Email (usar 'email' como clave es común)
                    "phone"     to phone,     // De master
                    "role"      to UserRole.PATIENT.name // Rol de Cambios-Zebas
                )

                db.collection("users").document(uid)
                    .set(userMap)
                    .addOnSuccessListener {
                        // Éxito guardando en Firestore
                        binding.progressRegister.visibility = View.GONE
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        // Navegar a MainActivity (de Cambios-Zebas)
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                        Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                        )
                        finish() // Cierra RegisterActivity
                    }
                    .addOnFailureListener { e ->
                        // Fallo guardando en Firestore
                        onFailure("Error guardando perfil: ${e.localizedMessage}")
                        // Considerar eliminar el usuario de Auth aquí también si falló Firestore
                    }
            }
    }

    // --- Función Helper para fallos (de Cambios-Zebas, modificada) ---
    private fun onFailure(message: String?) {
        binding.progressRegister.visibility = View.GONE
        binding.btnRegister.isEnabled = true // Rehabilitar botón en caso de fallo
        Toast.makeText(this, message ?: "Ocurrió un error desconocido", Toast.LENGTH_LONG).show()
    }
}