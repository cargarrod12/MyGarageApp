package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.data.local.Converters
import java.time.LocalDate

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
    @PrimaryKey(autoGenerate = true) override val id: Long = 0L,
    val usuarioEmail: String = "",
    val vehiculoId: Long = 0L,
    val estado: EstadoVehiculo = EstadoVehiculo.NUEVO,
    val anyo: Int = 0,
    val kilometros: Int = 0,
    val imagen: ByteArray? = null,
    @TypeConverters(Converters::class)
    override val fechaUltModificacion: String? = null,
    val borrado : Boolean = false,
): BaseEntity {
    constructor() : this(0L, "", 0L, EstadoVehiculo.NUEVO, 0, 0, null, null, false)


    override fun toFirestore(): Map<String, Any?> {
        return mapOf(
            "usuarioEmail" to usuarioEmail,
            "id" to id,
            "vehiculoId" to vehiculoId,
            "estado" to estado,
            "anyo" to anyo,
            "kilometros" to kilometros,
            "fechaUltModificacion" to (fechaUltModificacion ?: LocalDate.now().toString()),
            "borrado" to borrado,
        )
    }

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
