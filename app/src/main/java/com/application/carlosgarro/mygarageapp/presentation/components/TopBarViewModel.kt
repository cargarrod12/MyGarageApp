package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.data.local.LocalDataSource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val localDB : LocalDataSource,
    private val firestoreService: FirestoreService
): ViewModel(){
    fun signOut() {
        auth.signOut()
    }

    fun deleteUser() {
        viewModelScope.launch {
        auth.currentUser?.let {
            val email = auth.currentUser!!.email ?:""
            println(email)
            println(it.uid)
            firestoreService.eliminarDatosUsuario(it.uid)
            localDB.notificacionDAO().deleteNotificacionesByUser(email)
            localDB.vehiculoPersonalDAO().deleteVehiculoPersonalByUser(email)
            localDB.mantenimientoDAO().deleteMantenimientosByUser(email)
            auth.currentUser!!.delete()
        }
        }
    }
}