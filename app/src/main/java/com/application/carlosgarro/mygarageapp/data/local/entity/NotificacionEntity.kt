package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.Converters
import java.time.LocalDate


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
    override val id: Long = 0L,

    val vehiculoPersonalId: Long,


    val reglaMantenimientoId: Long,

    @TypeConverters(Converters::class)
    val tipoServicio: TipoServicio,

    val kilometrosUltimoServicio: Int,

    val kilometrosProximoServicio: Int,

    val notificado: Boolean,

    val activo : Boolean,

    @TypeConverters(Converters::class)
    override val fechaUltModificacion: String? = null,
): BaseEntity {

    constructor(): this(0L, 0L, 0L, TipoServicio.OTRO, 0, 0, false, true, null)

    override fun toFirestore(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "vehiculoPersonalId" to vehiculoPersonalId,
            "reglaMantenimientoId" to reglaMantenimientoId,
            "tipoServicio" to tipoServicio,
            "kilometrosUltimoServicio" to kilometrosUltimoServicio,
            "kilometrosProximoServicio" to kilometrosProximoServicio,
            "notificado" to notificado,
            "activo" to activo,
            "fechaUltModificacion" to (fechaUltModificacion ?: LocalDate.now().toString()),
        )
    }
}

