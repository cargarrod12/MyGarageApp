package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import java.time.LocalDate
import javax.inject.Inject

class VehiculoPersonalRepositoryImpl @Inject constructor(
    private val vehiculoPersonalDao: VehiculoPersonalDAO,
) : VehiculoPersonalRepository {


    override suspend fun getVehiculosPersonalesByUsuario(usuarioEmail: String): List<VehiculoPersonalModel> {
        try {
            val result = vehiculoPersonalDao.getVehiculosPersonalesByUsuario(usuarioEmail, listOf(0))
            return result.map { it.toModel() }
        }catch (e: Exception){
            println(e.message)
        }
        return emptyList()
    }



    override suspend fun saveVehiculoPersonal(vehiculoPersonal: VehiculoPersonalModel): Boolean {
        try {
            val result = vehiculoPersonalDao.insert(
                vehiculoPersonal.toEntity()
            )
            return result.toInt() != -1
        }catch (e: Exception){
            println(e.message)
        }
        return false
    }


    override suspend fun getVehiculoPersonalById(id: Long): VehiculoPersonalModel? {
        try {
            val result = vehiculoPersonalDao.getVehiculoPersonalById(id)
            return result?.toModel()
        }catch (e: Exception){
            println(e.message)
        }

        return null
    }

    override suspend fun deleteVehiculoPersonal(vehiculoId: Long): Boolean {
        try {
            val result = vehiculoPersonalDao.deleteVehiculoPersonal(vehiculoId, LocalDate.now().toString())
            return result > 0
        }catch (e: Exception){
            println(e.message)
        }
        return false
    }


}