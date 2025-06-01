package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel

interface VehiculoPersonalRepository {

    suspend fun saveVehiculoPersonal(vehiculoPersonal: VehiculoPersonalModel): Boolean

    suspend fun getVehiculosPersonalesByUsuario(usuarioEmail: String): List<VehiculoPersonalModel>

    suspend fun getVehiculoPersonalById(id: Long): VehiculoPersonalModel?

    suspend fun deleteVehiculoPersonal(vehiculoId: Long): Boolean
}