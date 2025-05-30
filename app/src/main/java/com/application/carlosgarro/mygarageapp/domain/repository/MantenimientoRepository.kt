package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel

interface MantenimientoRepository {

    suspend fun getMantenimientosByVehiculoPersonal(vehiculoPersonalId: Long): List<MantenimientoModel>

    suspend fun saveMantenimiento(mantenimiento: MantenimientoModel): Boolean
}