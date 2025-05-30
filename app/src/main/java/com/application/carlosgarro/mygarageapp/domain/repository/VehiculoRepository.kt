package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel

interface VehiculoRepository {

    suspend fun getAllVehiculos(): List<VehiculoModel>
}