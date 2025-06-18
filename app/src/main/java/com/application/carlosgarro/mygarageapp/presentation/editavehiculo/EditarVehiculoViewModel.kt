package com.application.carlosgarro.mygarageapp.presentation.editavehiculo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.deleteVehiculoPersonalUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditarVehiculoViewModel @Inject constructor(
    private val getVehiculoByIdUseCase: getVehiculoByIdUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val saveVehiculoPersonal: saveVehiculoPersonalUseCase,
    private val deleteVehiculoPersonalUseCase: deleteVehiculoPersonalUseCase
): ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _vehiculoId = MutableLiveData<Long>(0L)
    val vehiculoId: LiveData<Long> get() = _vehiculoId

    private val _vehiculo = MutableLiveData(VehiculoPersonalModel())
    val vehiculo: LiveData<VehiculoPersonalModel> = _vehiculo

    private val _imagenSeleccionada = MutableLiveData<ByteArray?>(null)
    val imagenSeleccionada: LiveData<ByteArray?> = _imagenSeleccionada

    private val _eventoMensaje = MutableSharedFlow<String>()
    val eventoMensaje: SharedFlow<String> = _eventoMensaje

    private val _imagenSeleccionadaUri = MutableLiveData<Uri>(null)
    val imagenSeleccionadaUri: LiveData<Uri?> = _imagenSeleccionadaUri


    fun setVehiculoId(id: Long) {
        _vehiculoId.value = id
        cargaVechiculo()
    }
    private fun cargaVechiculo() {
        viewModelScope.launch {
            if (_vehiculoId.value != null) {
                getVehiculoByIdUseCase(_vehiculoId.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }

                        is Resource.Success -> {
                            _vehiculo.value = resource.data ?: VehiculoPersonalModel()
                            _imagenSeleccionada.value = resource.data?.imagen
                            _isLoading.value = false
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
                        }
                    }
                }
            }

        }
    }

    fun setImagenDesdeUri(context: Context, uri: Uri) {
        _imagenSeleccionadaUri.value = uri
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val bytes = input.readBytes()
                _imagenSeleccionada.value = bytes
            }
        } catch (e: Exception) {

        }
    }

    fun actualizarVehiculoPersonal(value: VehiculoPersonalModel) {
        viewModelScope.launch {
            val usuarioEmail = firebaseAuth.currentUser?.email
            if (usuarioEmail != null) {
                val data = value.copy(usuarioEmail = usuarioEmail, imagen = imagenSeleccionada.value)
                saveVehiculoPersonal(data).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            _isLoading.value = false
                            _eventoMensaje.emit("Vehículo actualizado correctamente")
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            _eventoMensaje.emit("Error al actualizar vehículo")
                        }
                    }
                }
            }else {
                _eventoMensaje.emit("Error: Usuario no autenticado")
            }
        }

    }

    fun deleteVehiculoPersonal(vehiculoId: Long){
        viewModelScope.launch {
            deleteVehiculoPersonalUseCase(vehiculoId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        _isLoading.value = false
                        _eventoMensaje.emit("Vehículo eliminado correctamente")
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _eventoMensaje.emit("Error al eliminar vehículo")
                    }
                }
            }
        }
    }


}