package com.application.carlosgarro.mygarageapp.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.data.local.dao.UsuarioDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usuarioDAO: UsuarioDAO,
    private val firestoreService: FirestoreService
) : ViewModel(){


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    private fun insertUsuario(userId: String, email: String, password: String, navigateToHome: () -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioDAO.getUsuarioByEmail(email)
            if (usuario == null) {
                usuarioDAO.insert(UsuarioEntity(email, email, ""))
            }
            firestoreService.synDatosUsuario(userId, email)
            _isLoading.value = false
            navigateToHome()
        }
    }

    fun login(auth: FirebaseAuth, email: String, password: String, context: Context, navigateToHome : () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("LOGIN OK")
                        insertUsuario(task.result.user?.uid ?: "", email, password, navigateToHome)

                    } else {
                        println("LOGIN KO")
                        _isLoading.value = false
                        when (val exception = task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                Toast.makeText(context, "Usuario no registrado", Toast.LENGTH_LONG).show()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                Toast.makeText(context, "Email o contraseña incorrectos", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Toast.makeText(context, "Error al iniciar sesión. Por favor, inténtelo de nuevo más tarde.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }
    }


}