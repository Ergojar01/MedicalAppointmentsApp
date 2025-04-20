package com.proyecto.medicalappointmentsapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.proyecto.medicalappointmentsapp.data.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await // Importante para usar await()

class UserViewModel : ViewModel() {

    private val db: FirebaseFirestore = Firebase.firestore

    // LiveData para exponer los detalles del usuario a la UI
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user // Exponer como LiveData inmutable

    // LiveData para exponer errores
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Función para cargar los detalles del usuario desde Firestore
    fun loadUserDetails(uid: String) {
        viewModelScope.launch {
            try {
                val documentSnapshot = db.collection("users").document(uid).get().await() // Usar await() en coroutine
                if (documentSnapshot.exists()) {
                    val userDetails = documentSnapshot.toObject<User>()
                    _user.value = userDetails // Actualizar LiveData
                    _error.value = null // Limpiar error previo
                    Log.d("UserViewModel", "Usuario cargado: $userDetails")
                } else {
                    // Usuario autenticado pero no encontrado en Firestore (caso raro o error)
                    _user.value = null
                    _error.value = "Detalles de usuario no encontrados en la base de datos."
                    Log.w("UserViewModel", "Documento de usuario no encontrado para UID: $uid")
                }
            } catch (e: Exception) {
                // Error al obtener datos
                _user.value = null
                _error.value = "Error al cargar datos del usuario: ${e.message}"
                Log.e("UserViewModel", "Error al cargar usuario $uid", e)
            }
        }
    }

    // Limpia el error para que no se muestre repetidamente
    fun clearError() {
        _error.value = null
    }
}