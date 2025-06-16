package com.application.carlosgarro.mygarageapp.presentation.singup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.data.local.dao.UsuarioDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
) : ViewModel(){

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    private fun insertUsuario(email: String, password: String) {
        viewModelScope.launch {
            usuarioDAO.insert(UsuarioEntity(email, email, ""))
            _isLoading.value = false
        }
    }

    fun sigUp(email: String, password: String, context: Context, navigateToHome : () -> Unit, auth: FirebaseAuth) {
        _isLoading.value = true
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        insertUsuario(email,password)
                        navigateToHome()
//                        Log.i("REGISTRO", "REGISTRO OK")
                    } else {
                        _isLoading.value = false
                        when (val exception = task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                // El email ya está en uso
//                                Log.i("REGISTRO", "El correo ya está en uso")
                                Toast.makeText(
                                    context,
                                    "El correo ya está en uso",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is FirebaseAuthWeakPasswordException -> {
                                // La contraseña es muy débil
//                                Log.i("REGISTRO", "Contraseña débil")
                                Toast.makeText(
                                    context,
                                    "La contraseña es demasiado débil",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                // Email con formato inválido
//                                Log.i("REGISTRO", "Email inválido")
                                Toast.makeText(context, "El email no es válido", Toast.LENGTH_LONG)
                                    .show()
                            }

                            else -> {

                                // Otro error
//                                Log.i("REGISTRO", "Error desconocido: ${exception?.message}")
                                Toast.makeText(
                                    context,
                                    "Error realizar el registro. Por favor, intntelo de nuevo más tarde",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
        }
    }
}