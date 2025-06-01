package com.application.carlosgarro.mygarageapp.domain.model.vehiculo

import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity

data class VehiculoModel(
    val id: Long?,
    var marca: MarcaVehiculo,
    var modelo: ModeloVehiculo
){
    override fun toString(): String {
        return "$marca $modelo"
    }
}


fun VehiculoModel.toEntity(): VehiculoEntity {
    return VehiculoEntity(
        id = id ?: 0L,
        marca = marca,
        modelo = modelo
    )
}

fun VehiculoEntity.toModel(): VehiculoModel {
    return VehiculoModel(
        id = id,
        marca = marca,
        modelo = modelo
    )
}