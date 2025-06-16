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
    override val id: Long = 0L,

    @TypeConverters(Converters::class)
    val tipoServicio: TipoServicio,

    val vehiculoPersonalId: Long,

    @TypeConverters(Converters::class)
    val fechaServicio: String,

    val kilometrosServicio: Int,

    val precio: Double,

    @TypeConverters(Converters::class)
    override val fechaUltModificacion: String? = null,
): BaseEntity{

    constructor(): this(
        id = 0L,
        tipoServicio = TipoServicio.OTRO,
        vehiculoPersonalId = 0L,
        fechaServicio = LocalDate.now().toString(),
        kilometrosServicio = 0,
        precio = 0.0,
        fechaUltModificacion = null
    )

    override fun toFirestore(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "tipoServicio" to tipoServicio,
            "vehiculoPersonalId" to vehiculoPersonalId,
            "fechaServicio" to fechaServicio,
            "kilometrosServicio" to kilometrosServicio,
            "precio" to precio,
            "fechaUltModificacion" to fechaUltModificacion
        )
    }

}
