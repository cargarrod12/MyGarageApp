package com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal

import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.toModel

data class VehiculoPersonalModel(
    val id: Long? = 0L,
    val usuarioEmail: String = "",
    var modelo: VehiculoModel = VehiculoModel(0L, MarcaVehiculo.HONDA, ModeloVehiculo.CIVIC),
    var anyo: Int = 0,
    var estado: EstadoVehiculo = EstadoVehiculo.NUEVO,
    var kilometros: Int = 0,
    val imagen: String = "",
    val mantenimientos: List<MantenimientoModel> = emptyList(),
    val notificaciones: List<NotificacionModel> = emptyList()

    )




fun VehiculoPersonalModel.toEntity(): VehiculoPersonalEntity {
    return VehiculoPersonalEntity(
        usuarioEmail = usuarioEmail,
        vehiculoId = modelo.id?: 0L,
        estado = estado,
        anyo = anyo,
        kilometros = kilometros,
        imagen = imagen,
        id = id?: 0L
    )
}

fun VehiculoPersonalCompleto.toModel(): VehiculoPersonalModel {
    return VehiculoPersonalModel(
        id = vehiculoPersonal.id,
        usuarioEmail = vehiculoPersonal.usuarioEmail,
        modelo = vehiculo.toModel(),
        anyo = vehiculoPersonal.anyo,
        estado = vehiculoPersonal.estado,
        kilometros = vehiculoPersonal.kilometros,
        imagen = vehiculoPersonal.imagen,
        mantenimientos = mantenimientos.map { it.toModel() },
        notificaciones = notificaciones.map { it.toModel() }
    )
}

