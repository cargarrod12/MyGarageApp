package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio


@Entity(tableName = "notificacion",
    foreignKeys = [
        ForeignKey(
            entity = VehiculoPersonalEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehiculoPersonalId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ReglaMantenimientoEntity::class,
            parentColumns = ["id"],
            childColumns = ["reglaMantenimientoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["vehiculoPersonalId", "tipoServicio"], unique = true),
        Index(value = ["reglaMantenimientoId"])

    ]
    )
data class NotificacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val vehiculoPersonalId: Long,


    val reglaMantenimientoId: Long,

    val tipoServicio: TipoServicio,

    val kilometrosUltimoServicio: Int,

    val kilometrosProximoServicio: Int,

    val notificado: Boolean,

    val activo : Boolean
)