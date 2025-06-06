package com.application.carlosgarro.mygarageapp.presentation.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResumenViewModel @Inject constructor(
    private val saveVehiculoPersonal: saveVehiculoPersonalUseCase,
    private val getVehiculoPersonalByUser: getVehiculoPersonalByUserUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val vehiculoRepository: VehiculoRepository
): ViewModel() {


    private val _listaVehiculoPersonal = MutableLiveData<List<VehiculoPersonalModel>>(emptyList())  // mutable interna
    val listaVehiculoPersonal: LiveData<List<VehiculoPersonalModel>> = _listaVehiculoPersonal // solo lectura pública

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _vehiculoPersonal = MutableLiveData(VehiculoPersonalModel())
    val vehiculoPersonal: LiveData<VehiculoPersonalModel> = _vehiculoPersonal

    private val _listaVehiculo = MutableLiveData<List<VehiculoModel>>(emptyList())
    val listaVehiculo: LiveData<List<VehiculoModel>> = _listaVehiculo

    private val _eventoMensaje = MutableSharedFlow<String>()
    val eventoMensaje: SharedFlow<String> = _eventoMensaje

    private val _imagenSeleccionada = MutableLiveData<ByteArray?>(null)
    val imagenSeleccionada: LiveData<ByteArray?> = _imagenSeleccionada

    private val _imagenSeleccionadaUri = MutableLiveData<Uri>(null)
    val imagenSeleccionadaUri: LiveData<Uri?> = _imagenSeleccionadaUri



    init {
        cargaVechiculos()
    }

    private fun cargaVechiculos() {
        viewModelScope.launch {

            val usuarioEmail = firebaseAuth.currentUser?.email
            if (usuarioEmail != null) {
                getVehiculoPersonalByUser(usuarioEmail).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _listaVehiculoPersonal.value = emptyList()
                            _isLoading.value = true
                            Log.i("RESUMEN", "Loading VehiculosPersonalesByUSer: ${resource.data}")
                        }
                        is Resource.Success -> {
                            _listaVehiculoPersonal.value = resource.data ?: emptyList()
                            _isLoading.value = false
                            Log.i("RESUMEN", "Success: ${resource.data}")
                        }
                        is Resource.Error -> {
                            _listaVehiculoPersonal.value = emptyList()
                            _isLoading.value = false
                            Log.e("RESUMEN", "Error: ${resource.message}")
                        }
                    }
                }
                _listaVehiculo.value = vehiculoRepository.getAllVehiculos()
            }else {
                _listaVehiculoPersonal.value = emptyList()
            }
        }

    }
    fun setImagenDesdeUri(context: Context, uri: Uri) {
        Log.i("Imagen", "Inicio de lectura de imagen desde URI: $uri")
        _imagenSeleccionadaUri.value = uri
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val bytes = input.readBytes()
                _imagenSeleccionada.value = bytes
            }
        } catch (e: Exception) {
            Log.e("Imagen", "Error leyendo imagen", e)
        }
    }

    fun addVehiculoPersonal(value: VehiculoPersonalModel) {
        viewModelScope.launch {
            val usuarioEmail = firebaseAuth.currentUser?.email
            if (usuarioEmail != null) {
                val data = value.copy(usuarioEmail = usuarioEmail, imagen = imagenSeleccionada.value)
                saveVehiculoPersonal(data).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                            Log.i("NUEVO VEHICULO", "AÑADIENDO NUEVO VEHICULO: $data")
                        }
                        is Resource.Success -> {
                            _isLoading.value = false
                            Log.i("NUEVO VEHICULO", "Success: VEHICULO AÑADIDO ${resource.data}")
                            cargaVechiculos()
                            _eventoMensaje.emit("Vehículo añadido correctamente")
                        }
                        is Resource.Error -> {
                            _isLoading.value = false
                            Log.e("NUEVO VEHICULO", "Error: ${resource.message}")
                            _eventoMensaje.emit("Error al añadir vehículo")
                        }
                    }
                }
            }else {
                _listaVehiculoPersonal.value = emptyList()
            }
        }

    }

}
