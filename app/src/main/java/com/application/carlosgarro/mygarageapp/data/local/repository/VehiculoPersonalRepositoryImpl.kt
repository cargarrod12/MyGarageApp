package com.application.carlosgarro.mygarageapp.data.local.repository

import android.util.Log
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import javax.inject.Inject

class VehiculoPersonalRepositoryImpl @Inject constructor(
    private val vehiculoPersonalDao: VehiculoPersonalDAO,
) : VehiculoPersonalRepository {
    override suspend fun saveVehiculoPersonal(vehiculoPersonal: VehiculoPersonalModel): Boolean {
        try {
            val result = vehiculoPersonalDao.insertVehiculoPersonal(
                vehiculoPersonal.toEntity()
            )
            return result.toInt() != -1
        }catch (e: Exception){
            Log.e("VehiculoPersonalRepositoryImpl", "Error saving vehiculo personal: ${e.message}")
        }
        return false
    }

    override suspend fun getVehiculosPersonalesByUsuario(usuarioEmail: String): List<VehiculoPersonalModel> {
        try {
            val result = vehiculoPersonalDao.getVehiculosPersonalesByUsuario(usuarioEmail)
            return result.map { it.toModel() }
        }catch (e: Exception){
            Log.e("VehiculoPersonalRepositoryImpl", "Error fetching vehiculos personales: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getVehiculoPersonalById(id: Long): VehiculoPersonalModel? {
        try {
            Log.i("VehiculoPersonalRepositoryImpl", "Fetching vehiculo personal by id: $id")
            val result = vehiculoPersonalDao.getVehiculoPersonalById(id)
            return result?.toModel()
        }catch (e: Exception){
            Log.e("VehiculoPersonalRepositoryImpl", "Error fetching vehiculo personal by id: ${e.message}")
        }
        return null
    }


}