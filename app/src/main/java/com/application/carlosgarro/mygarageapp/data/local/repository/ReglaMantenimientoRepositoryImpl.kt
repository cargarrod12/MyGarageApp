package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.ReglaMantenimientoDAO
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.ReglaMantenimientoRepository
import javax.inject.Inject

class ReglaMantenimientoRepositoryImpl @Inject constructor(
    private val reglaMantenimientoDao: ReglaMantenimientoDAO
) : ReglaMantenimientoRepository {


    override suspend fun getReglaMantenimientoByVehiculoAndTipoServicio(
        vehiculoPersonalId: Long,
        tipoServicio: String
    ): ReglaMantenimientoModel? {
        try {
            val result =
                reglaMantenimientoDao.getReglaMantenimientoByVehiculoPersonalAndTipoServicio(
                    vehiculoPersonalId = vehiculoPersonalId,
                    tipoServicio = tipoServicio
                )
            return result?.toModel()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}


