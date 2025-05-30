package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.data.local.Converters
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo

@Entity(
    tableName = "vehiculo_personal",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["email"],
            childColumns = ["usuarioEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VehiculoEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehiculoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["usuarioEmail"]),
        Index(value = ["vehiculoId"])
    ]
)
data class VehiculoPersonalEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val usuarioEmail: String,
    val vehiculoId: Long,

    @TypeConverters(Converters::class)
    val estado: EstadoVehiculo,

    val anyo: Int,
    val kilometros: Int,
    val imagen: String
)
