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
    val imagen: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VehiculoPersonalEntity

        if (id != other.id) return false
        if (usuarioEmail != other.usuarioEmail) return false
        if (vehiculoId != other.vehiculoId) return false
        if (estado != other.estado) return false
        if (anyo != other.anyo) return false
        if (kilometros != other.kilometros) return false
        if (!imagen.contentEquals(other.imagen)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + usuarioEmail.hashCode()
        result = 31 * result + vehiculoId.hashCode()
        result = 31 * result + estado.hashCode()
        result = 31 * result + anyo
        result = 31 * result + kilometros
        result = 31 * result + imagen.contentHashCode()
        return result
    }
}
