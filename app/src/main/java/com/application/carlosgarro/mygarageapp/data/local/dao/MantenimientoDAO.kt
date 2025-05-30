package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity


@Dao
interface MantenimientoDAO {

    @Query("SELECT * FROM mantenimiento WHERE vehiculoPersonalId = :vehiculoPersonalId")
    suspend fun getMantenimientosByVehiculoId(vehiculoPersonalId: Long): List<MantenimientoEntity>

    @Upsert
    suspend fun insertMantenimiento(toEntity: MantenimientoEntity): Long
}