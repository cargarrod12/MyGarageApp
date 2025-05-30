package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.Converters
import java.time.LocalDate


@Entity(tableName = "mantenimiento",
    foreignKeys = [
         ForeignKey(
             entity = VehiculoPersonalEntity::class,
             parentColumns = ["id"],
             childColumns = ["vehiculoPersonalId"],
             onDelete = ForeignKey.CASCADE
         )
    ],
    indices = [
        Index(value = ["vehiculoPersonalId"])
    ]
)
data class MantenimientoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @TypeConverters(Converters::class)
    val tipoServicio: TipoServicio,

    val vehiculoPersonalId: Long,

    @TypeConverters(Converters::class)
    val fechaServicio: LocalDate,

    val kilometrosServicio: Int,

    val precio: Double,
)