package com.application.carlosgarro.mygarageapp.domain.model.mantenimiento

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity
import java.time.LocalDate

data class MantenimientoModel(
    val id: Long? = 0L,
    val vehiculoId: Long = 0L,
    var tipoServicio: TipoServicio = TipoServicio.OTRO,
    var fechaServicio: LocalDate = LocalDate.now(),
    var kilometrosServicio: Int = 0,
    var precio: Double = 0.0,
    )

fun MantenimientoModel.toEntity(): MantenimientoEntity {
    return MantenimientoEntity(
        id = id?: 0L,
        vehiculoPersonalId = vehiculoId,
        tipoServicio = tipoServicio,
        fechaServicio = fechaServicio.toString(),
        kilometrosServicio = kilometrosServicio,
        precio = precio,
        fechaUltModificacion = LocalDate.now().toString()

    )
}

fun MantenimientoEntity.toModel(): MantenimientoModel {
    return MantenimientoModel(
        id = id,
        vehiculoId = vehiculoPersonalId,
        tipoServicio = tipoServicio,
        fechaServicio = LocalDate.parse(fechaServicio),
        kilometrosServicio = kilometrosServicio,
        precio = precio
    )
}
