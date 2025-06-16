package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio


@Entity(tableName = "regla_mantenimiento",
    foreignKeys = [
         ForeignKey(
             entity = VehiculoEntity::class,
             parentColumns = ["id"],
             childColumns = ["vehiculoId"],
             onDelete = ForeignKey.CASCADE
         )
    ],
    indices = [
         Index(value = ["vehiculoId"])
    ]
)
data class ReglaMantenimientoEntity(

    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0L,

    val vehiculoId: Long,

    val tipoServicio: TipoServicio,

    val intervalo: Int,


    ): DatosGeneralesEntity{

        constructor(): this(0L, 0L, TipoServicio.OTRO, 200 )
    }


