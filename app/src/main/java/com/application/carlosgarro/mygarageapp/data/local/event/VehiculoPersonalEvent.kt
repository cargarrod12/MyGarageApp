package com.application.carlosgarro.mygarageapp.data.local.event

import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo

sealed interface VehiculoPersonalEvent {

    object saveVehiculoPersonal : VehiculoPersonalEvent
    data class SetTipoEstado(val estado: EstadoVehiculo) : VehiculoPersonalEvent
    data class SetModelo(val modelo: VehiculoEntity) : VehiculoPersonalEvent
    data class SetAnyo(val anyo: Int) : VehiculoPersonalEvent
    data class SetKilometros(val kilometros: Int) : VehiculoPersonalEvent
    data class SetImagen(val imagen: String) : VehiculoPersonalEvent
    data class DeleteVehiculoPersonal(val vehiculoId: Long) : VehiculoPersonalEvent
}