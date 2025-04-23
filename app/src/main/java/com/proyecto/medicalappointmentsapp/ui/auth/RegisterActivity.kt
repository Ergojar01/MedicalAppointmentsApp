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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.medicalappointmentsapp.R
import com.proyecto.medicalappointmentsapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // — Construir spans para el CheckBox —
        val fullText = "He leído y acepto los Términos y la Política de Privacidad"
        val ss = SpannableString(fullText)

        // "Términos"
        val termsText = "Términos"
        val termsStart = fullText.indexOf(termsText)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/terms")))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.purple_500)
                ds.isUnderlineText = false
            }
        }, termsStart, termsStart + termsText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // "Política de Privacidad"
        val privacyText = "Política de Privacidad"
        val privStart = fullText.indexOf(privacyText)
        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/privacy")))
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.purple_500)
                ds.isUnderlineText = false
            }
        }, privStart, privStart + privacyText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.chkTerms.text = ss
        binding.chkTerms.movementMethod = LinkMovementMethod.getInstance()

        // — Listener de registro —
        binding.btnRegister.setOnClickListener {
            // Limpiar errores
            binding.tilName.error = null
            binding.tilLastName.error = null
            binding.tilEmail.error = null
            binding.tilPhone.error = null
            binding.tilPassword.error = null
            binding.tilConfirm.error = null

            val name     = binding.etName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val phone    = binding.etPhone.text.toString().trim()
            val pass     = binding.etPassword.text.toString()
            val confirm  = binding.etConfirm.text.toString()
            val agreed   = binding.chkTerms.isChecked

            // Validaciones
            if (name.isEmpty()) {
                binding.tilName.error = "Obligatorio"
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            if (lastName.isEmpty()) {
                binding.tilLastName.error = "Obligatorio"
                binding.etLastName.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "Correo inválido"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                binding.tilPhone.error = "Obligatorio"
                binding.etPhone.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.PHONE.matcher(phone).matches()) {
                binding.tilPhone.error = "Número inválido"
                binding.etPhone.requestFocus()
                return@setOnClickListener
            }
            if (pass.length < 8) {
                binding.tilPassword.error = "Mínimo 8 caracteres"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (pass != confirm) {
                binding.tilConfirm.error = "No coincide"
                binding.etConfirm.requestFocus()
                return@setOnClickListener
            }
            if (!agreed) {
                Toast.makeText(this, "Debes aceptar términos y privacidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressRegister.visibility = View.VISIBLE
            binding.btnRegister.isEnabled = false

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        binding.progressRegister.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(this, "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }
                    val uid = auth.currentUser!!.uid
                    val userMap = hashMapOf(
                        "name"     to name,
                        "lastName" to lastName,
                        "username" to username.ifEmpty { null },
                        "email"    to email,
                        "phone"    to phone
                    )
                    db.collection("users").document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            binding.progressRegister.visibility = View.GONE
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            binding.progressRegister.visibility = View.GONE
                            binding.btnRegister.isEnabled = true
                            Toast.makeText(this, "Error guardando perfil: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                }
        }
    }
}
