package com.application.carlosgarro.mygarageapp.data.local.repository

import android.util.Log
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
             Log.i("ReglaMantenimientoRepositoryImpl", "Fetching regla mantenimiento by vehiculo and tipo servicio: $vehiculoPersonalId, $tipoServicio")
            val result =
                reglaMantenimientoDao.getReglaMantenimientoByVehiculoPersonalAndTipoServicio(
                    vehiculoPersonalId = vehiculoPersonalId,
                    tipoServicio = tipoServicio
                )
            Log.i("ReglaMantenimientoRepositoryImpl", "Result: $result")
            return result?.toModel()
        } catch (e: Exception) {
            Log.i("ReglaMantenimientoRepositoryImpl", "Error fetching regla mantenimiento by vehiculo and tipo servicio: ${e.message}")
            e.printStackTrace()
            return null
        }
    }
}


