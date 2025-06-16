package com.application.carlosgarro.mygarageapp.presentation.notificaciones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionesByVehiculoPersonalIdUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.updateNotificacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificacionesViewModel @Inject constructor(
    private val getNotificacionesByVehiculoPersonalIdUseCase: getNotificacionesByVehiculoPersonalIdUseCase,
    private val updateNotificacionesUseCase: updateNotificacionesUseCase
) :ViewModel()

{
    private val _notificaciones = MutableLiveData<List<NotificacionModel>>(emptyList())
    val notificaciones: LiveData<List<NotificacionModel>> get() = _notificaciones

    private val _vehiculoId = MutableLiveData(0L)
    val vehiculoId: LiveData<Long> get() = _vehiculoId

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventoMensaje = MutableSharedFlow<String>()
    val eventoMensaje: SharedFlow<String> = _eventoMensaje

    fun setVehiculoId(id: Long) {
        _vehiculoId.value = id
        cargaNotificaciones()
    }
    private fun cargaNotificaciones() {
        viewModelScope.launch {
            if (_vehiculoId.value != null) {
//                Log.i("PANTALLA NOTIFICACIONES", "ID: ${_vehiculoId.value}")
                getNotificacionesByVehiculoPersonalIdUseCase(_vehiculoId.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
//                            Log.i("PANTALLA NOTIFICACIONES", "Loading NOTIFICACIONES: ${resource.data}")
                        }

                        is Resource.Success -> {
                            _notificaciones.value = resource.data ?: emptyList()
                            _isLoading.value = false
//                            Log.i("PANTALLA NOTIFICACIONES", "Success NOTIFICACIONES: ${resource.data}")
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
//                            Log.e("PANTALLA NOTIFICACIONES", "Error NOTIFICACIONES: ${resource.message}")
                        }
                    }
                }
            }
        }
    }


    fun updateNotificaciones(notificaciones: List<NotificacionModel>){
//        Log.e("PANTALLA NOTIFICACIONES", "Lista Notificaciones a actualizar: $notificaciones" )
        viewModelScope.launch {
            updateNotificacionesUseCase(notificaciones).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
//                        Log.i(
//                            "NUEVA ENTRADA HISTORIAL",
//                            "Success: ENTRADA HISTORIAL AÑADIDA ${resource.data}"
//                        )
                        cargaNotificaciones()
                        _eventoMensaje.emit("Notificaciones actualizadas correctamente")
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
//                        Log.e("NUEVA ENTRADA HISTORIAL", "Error: ${resource.message}")
                        _eventoMensaje.emit("Error al actualizar notificaciones")
                    }
                }
            }
        }
    }


}
