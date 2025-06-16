package com.application.carlosgarro.mygarageapp.presentation.vehiculo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiculoViewModel @Inject constructor(
    private val getVehiculoByIdUseCase: getVehiculoByIdUseCase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _vehiculoId = MutableLiveData(0L)
    val vehiculoId: LiveData<Long> get() = _vehiculoId

    private val _vehiculo = MutableLiveData(VehiculoPersonalModel())
    val vehiculo: LiveData<VehiculoPersonalModel> = _vehiculo



    fun setVehiculoId(id: Long) {
        _vehiculoId.value = id
        cargaVechiculo()
    }

    private fun cargaVechiculo() {
        viewModelScope.launch {
            if (_vehiculoId.value != null) {
                //Log.i("PANTALLA VEHICULO", "ID: ${_vehiculoId.value}")
                getVehiculoByIdUseCase(_vehiculoId.value!!).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                            //Log.i("PANTALLA VEHICULO", "Loading VehiculosPersonalesByUSer: ${resource.data}")
                        }

                        is Resource.Success -> {
                            _vehiculo.value = resource.data ?: VehiculoPersonalModel()
                            _isLoading.value = false
                            //Log.i("PANTALLA VEHICULO", "Success: ${resource.data}")
                        }

                        is Resource.Error -> {
                            _isLoading.value = false
                            //Log.e("PANTALLA VEHICULO", "Error: ${resource.message}")
                        }
                    }
                }
            } else {
                //Log.i("RESUMEN", "ID: ${_vehiculoId.value}")
            }

        }
    }

}