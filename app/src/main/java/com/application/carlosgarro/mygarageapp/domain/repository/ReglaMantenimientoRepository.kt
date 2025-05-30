package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel

interface ReglaMantenimientoRepository {

    suspend fun getReglaMantenimientoByVehiculoAndTipoServicio(vehiculoPersonalId: Long, tipoServicio: String): ReglaMantenimientoModel?



}
