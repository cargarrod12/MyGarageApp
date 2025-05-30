package com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity

data class ReglaMantenimientoModel(

    val id: Long? = 0L,
    val vehiculoId: Long = 0L,
    val tipoServicio: TipoServicio,
    val intervalo: Int,

)


fun ReglaMantenimientoModel.toEntity(): ReglaMantenimientoEntity {
    return ReglaMantenimientoEntity(
        id = id?: 0L,
        vehiculoId = vehiculoId,
        tipoServicio = tipoServicio,
        intervalo = intervalo
    )
}

fun ReglaMantenimientoEntity.toModel(): ReglaMantenimientoModel {
    return ReglaMantenimientoModel(
        id = id,
        vehiculoId = vehiculoId,
        tipoServicio = tipoServicio,
        intervalo = intervalo
    )
}
