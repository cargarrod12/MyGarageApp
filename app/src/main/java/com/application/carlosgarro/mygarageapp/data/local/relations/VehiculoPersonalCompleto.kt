package com.application.carlosgarro.mygarageapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity

data class VehiculoPersonalCompleto(

    @Embedded val vehiculoPersonal: VehiculoPersonalEntity,
    @Relation(
        parentColumn = "usuarioEmail",
        entityColumn = "email",
    )
    val usuario: UsuarioEntity,
    @Relation(
        parentColumn = "vehiculoId",
        entityColumn = "id",
    )
    val vehiculo: VehiculoEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "vehiculoPersonalId",
    )
    val mantenimientos: List<MantenimientoEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "vehiculoPersonalId",
    )
    val notificaciones : List<NotificacionEntity>

)