package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoDAO
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoRepository
import javax.inject.Inject

class VehiculoRepositoryImpl @Inject constructor(
    private val vehiculoDAO: VehiculoDAO,
) : VehiculoRepository {

    override suspend fun getAllVehiculos(): List<VehiculoModel> {
        try {
            return vehiculoDAO.getAllVehiculos().map { it.toModel() }

        }catch (e: Exception){
            println(e.message)
        }
        return emptyList()
    }
}