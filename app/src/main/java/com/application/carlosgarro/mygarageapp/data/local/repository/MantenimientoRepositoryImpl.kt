package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.MantenimientoRepository
import javax.inject.Inject

class MantenimientoRepositoryImpl @Inject constructor(
    private val mantenimientoDao: MantenimientoDAO,
) : MantenimientoRepository {
    override suspend fun getMantenimientosByVehiculoPersonal(vehiculoPersonalId: Long): List<MantenimientoModel> {
        try {
            val result = mantenimientoDao.getMantenimientosByVehiculoId(vehiculoPersonalId)
            return result.map { it.toModel() }
        }catch (e: Exception){
            println(e.message)
        }
        return emptyList()
    }

    override suspend fun saveMantenimiento(mantenimiento: MantenimientoModel): Boolean {
        try {
            val result = mantenimientoDao.insert(
                mantenimiento.toEntity()
            )
            return result.toInt() != -1
        }catch (e: Exception){
            println(e.message)
        }
        return false
    }

}