package com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal

import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.toModel
import java.time.LocalDate

data class VehiculoPersonalModel(
    val id: Long? = 0L,
    val usuarioEmail: String = "",
    var modelo: VehiculoModel = VehiculoModel(0L, MarcaVehiculo.HONDA, ModeloVehiculo.CIVIC),
    var anyo: Int = 0,
    var estado: EstadoVehiculo = EstadoVehiculo.NUEVO,
    var kilometros: Int = 0,
    val mantenimientos: List<MantenimientoModel> = emptyList(),
    val notificaciones: List<NotificacionModel> = emptyList(),
    val imagen: ByteArray? = null,
    val fechaUltModificacion: LocalDate? = null

    ) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VehiculoPersonalModel

        if (id != other.id) return false
        if (usuarioEmail != other.usuarioEmail) return false
        if (modelo != other.modelo) return false
        if (anyo != other.anyo) return false
        if (estado != other.estado) return false
        if (kilometros != other.kilometros) return false
        if (mantenimientos != other.mantenimientos) return false
        if (notificaciones != other.notificaciones) return false
        if (imagen != null) {
            if (other.imagen == null) return false
            if (!imagen.contentEquals(other.imagen)) return false
        } else if (other.imagen != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + usuarioEmail.hashCode()
        result = 31 * result + modelo.hashCode()
        result = 31 * result + anyo
        result = 31 * result + estado.hashCode()
        result = 31 * result + kilometros
        result = 31 * result + mantenimientos.hashCode()
        result = 31 * result + notificaciones.hashCode()
        result = 31 * result + (imagen?.contentHashCode() ?: 0)
        return result
    }
}


fun VehiculoPersonalModel.toEntity(): VehiculoPersonalEntity {
    return VehiculoPersonalEntity(
        usuarioEmail = usuarioEmail,
        vehiculoId = modelo.id?: 0L,
        estado = estado,
        anyo = anyo,
        kilometros = kilometros,
        imagen = imagen,
        id = id?: 0L,
        fechaUltModificacion = LocalDate.now() // Uncomment if you want to set the current date as default
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

