package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity


@Dao
interface VehiculoDAO {

     @Query("SELECT * FROM vehiculo order by marca, modelo")
     suspend fun getAllVehiculos(): List<VehiculoEntity>

}