package com.application.carlosgarro.mygarageapp.presentation.historial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases.getMantenimientosByVehiculoPersonal
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases.saveMantenimientoUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionByVehiculoPersonalAndTipoServicioUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.saveNotificacionUseCase
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.usecases.getReglaMantenimientoByVehiculoAndTipoServicioUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val getMantenimientosByVehiculoPersonal: getMantenimientosByVehiculoPersonal,
    private val saveMantenimientoUseCase: saveMantenimientoUseCase,
    private val getNotificacionByVehiculoPersonalUseCase : getNotificacionByVehiculoPersonalAndTipoServicioUseCase,
    private val getReglaMantenimientoByVehiculoPersonalAndTipoServicio: getReglaMantenimientoByVehiculoAndTipoServicioUseCase,
    private val saveNotificacionUseCase: saveNotificacionUseCase,
    private val getVehiculoPersonalByIdUseCase: getVehiculoByIdUseCase
    ): ViewModel() {

    private val _mantenimiento = MutableLiveData(MantenimientoModel())
    val mantenimiento: LiveData<MantenimientoModel> get() = _mantenimiento

    private val _vehiculoId = MutableLiveData(0L)
    val vehiculoId: LiveData<Long> get() = _vehiculoId

    private val _vehiculo = MutableLiveData(VehiculoPersonalModel())
    val vehiculo: MutableLiveData<VehiculoPersonalModel?> get() = _vehiculo

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mantenimientos = MutableLiveData<List<MantenimientoModel>>(emptyList())
    val mantenimientos: LiveData<List<MantenimientoModel>> = _mantenimientos

    private val _eventoMensaje = MutableSharedFlow<String>()
    val eventoMensaje: SharedFlow<String> = _eventoMensaje

    fun setVehiculoId(id: Long) {
        _vehiculoId.value = id
        cagaMantenimientos()
    }

    private fun cagaMantenimientos() {
        viewModelScope.launch {
            if (_vehiculoId.value != null) {
                //Log.i("PANTALLA HISTORIAL", "ID: ${_vehiculoId.value}")
                getMantenimientosByVehiculoPersonal(_vehiculoId.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                            //Log.i("PANTALLA HISTORIAL", "Loading Mantenimientos: ${resource.data}")
                        }

                        is Resource.Success -> {
                            _mantenimientos.value = resource.data ?: emptyList()
                            _isLoading.value = false
                            //Log.i("PANTALLA HISTORIAL", "Success Mantenimientos: ${resource.data}")
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
                            //Log.e("PANTALLA HISTORIAL", "Error Mantenimientos: ${resource.message}")
                        }
                    }

                    getVehiculoPersonalByIdUseCase(_vehiculoId.value!!).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                _isLoading.value = true
                                //Log.i("PANTALLA HISTORIAL", "Loading VehiculoPersonal: ${resource.data}")
                            }

                            is Resource.Success -> {
                                _vehiculo.value = resource.data
                                _isLoading.value = false
                                //Log.i("PANTALLA HISTORIAL", "Success: ${resource.data}")
                            }

                            is Resource.Error -> {
                                _isLoading.value = false
                                //Log.e("PANTALLA HISTORIAL", "Error: ${resource.message}")
                            }
                        }
                    }
                }


            } else {
                //Log.i("HISTORIAL", "ID: ${_vehiculoId.value}")
            }

        }
    }

    fun addEntradaMantenimiento(value: MantenimientoModel) {
        viewModelScope.launch {
            val data = value.copy(vehiculoId = _vehiculoId.value!!)
            println(data)
            saveMantenimientoUseCase(data).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        //Log.i(
//                            "NUEVA ENTRADA HISTORIAL",
//                            "AÑADIENDO NUEVA ENTRADA HISTORIAL: $data"
//                        )
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
                        //Log.i(
//                            "NUEVA ENTRADA HISTORIAL",
//                            "Success: ENTRADA HISTORIAL AÑADIDA ${resource.data}"
//                        )
                        val result = resource.data
                        cagaMantenimientos()
                        getNotificacion(data)
                        _eventoMensaje.emit("Nueva entrada del Historial añadida correctamente")
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        //Log.e("NUEVA ENTRADA HISTORIAL", "Error: ${resource.message}")
                        _eventoMensaje.emit("Error al añadir nueva entrada del Historial")
                    }
                }
            }



        }
    }

    private fun getNotificacion(
        mantenmiento: MantenimientoModel,
    ) {
        viewModelScope.launch {
            getNotificacionByVehiculoPersonalUseCase(
                mantenmiento.vehiculoId,
                mantenmiento.tipoServicio
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _isLoading.value = true
                        //Log.i("NUEVA ENTRADA HISTORIAL", "Loading Notificacion: ${resource.data}")
                    }

                    is Resource.Success -> {
                        val notificacion = resource.data
                        if (notificacion != null) {
                            //Log.i("NUEVA ENTRADA HISTORIAL", "Success: ${resource.data}")
                        } else {
                            //Log.i(
//                                "NUEVA ENTRADA HISTORIAL",
//                                "Success: CREAMOS UNA NUEVA NOTIFICACION"
//                            )
                            getReglaMantenimiento(mantenmiento, _vehiculo.value?.id!!)
                        }
                    }

                    is Resource.Error -> {
                        _isLoading.value = false
                        //Log.e("NUEVA ENTRADA HISTORIAL", "Error: ${resource.message}")
                    }
                }

            }

        }


        }
        fun getReglaMantenimiento(
            mantenmiento: MantenimientoModel,
            vehiculoId: Long,
        ) {
            viewModelScope.launch {
                //Log.i("NUEVA ENTRADA HISTORIAL", "VehiculoId: $vehiculoId" +
//                        " TipoServicio: ${mantenmiento.tipoServicio}")
                getReglaMantenimientoByVehiculoPersonalAndTipoServicio(
                    vehiculoId,
                    mantenmiento.tipoServicio
                ).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
//                            //Log.i(
//                                "NUEVA ENTRADA HISTORIAL",
//                                "Loading ReglaMantenimiento: ${resource.data}"
//                            )
                        }

                        is Resource.Success -> {
                            val reglaMantenimiento = resource.data
                            if (reglaMantenimiento != null) {
                                //Log.i("NUEVA ENTRADA HISTORIAL", "Success: ${resource.data}")
                                addNuevaNotificacion(mantenmiento, reglaMantenimiento)
                            } else {
//                                //Log.i(
//                                    "NUEVA ENTRADA HISTORIAL",
//                                    "Success: NO HAY REGLA DE MANTENIMIENTO"
//                                )
                            }
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
                            //Log.e("NUEVA ENTRADA HISTORIAL", "Error: ${resource.message}")
                        }
                    }
                }
            }
        }

        fun addNuevaNotificacion(
            mantenmiento: MantenimientoModel,
            reglaMantenimiento: ReglaMantenimientoModel
        ) {
            viewModelScope.launch {
                val notificacion = NotificacionModel(
                    vehiculoPersonalId = mantenmiento.vehiculoId,
                    tipoServicio = mantenmiento.tipoServicio,
                    kilometrosUltimoServicio = mantenmiento.kilometrosServicio,
                    kilometrosProximoServicio = mantenmiento.kilometrosServicio + reglaMantenimiento.intervalo,
                    activo = false,
                    notificado = false,
                    reglaMantenimientoId = reglaMantenimiento.id!!

                )
                saveNotificacionUseCase(notificacion).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
//                            //Log.i(
//                                "NUEVA ENTRADA HISTORIAL",
//                                "Loading NUEVA NOTIFICACION: ${resource.data}"
//                            )
                        }

                        is Resource.Success -> {
                            _isLoading.value = false
                            //Log.i(
//                                "NUEVA ENTRADA HISTORIAL",
//                                "Success NUEVA NOTIFICACION: ${resource.data}"
//                            )
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
                            //Log.e(
//                                "NUEVA ENTRADA HISTORIAL",
//                                "Error NUEVA NOTIFICACION: ${resource.message}"
//                            )
                        }
                    }
                }

            }
    }
}