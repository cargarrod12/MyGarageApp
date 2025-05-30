package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto

@Dao
interface VehiculoPersonalDAO {

    @Query("SELECT * FROM vehiculo_personal")
    suspend fun getAllVehiculosPersonales(): List<VehiculoPersonalEntity>

    @Upsert
    suspend fun insertVehiculoPersonal(vehiculoPersonal: VehiculoPersonalEntity): Long

    @Delete
    suspend fun deleteVehiculoPersonal(vehiculoPersonal: VehiculoPersonalEntity): Int

    @Transaction
    @Query("SELECT * FROM vehiculo_personal WHERE usuarioEmail = :usuarioEmail")
    suspend fun getVehiculosPersonalesByUsuario(usuarioEmail: String): List<VehiculoPersonalCompleto>

    @Transaction
    @Query("SELECT * FROM vehiculo_personal WHERE id = :id")
    suspend fun getVehiculoPersonalById(id: Long): VehiculoPersonalCompleto?

}