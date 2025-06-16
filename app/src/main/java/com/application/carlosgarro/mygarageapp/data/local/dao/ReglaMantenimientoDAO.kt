package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity


@Dao
interface ReglaMantenimientoDAO : BaseDAO<ReglaMantenimientoEntity> {

    @Query("SELECT * FROM regla_mantenimiento")
     suspend fun getAllReglasMantenimiento(): List<ReglaMantenimientoEntity>

    @Query("SELECT * FROM regla_mantenimiento WHERE vehiculoId = :vehiculoPersonalId AND tipoServicio = :tipoServicio")
     suspend fun getReglaMantenimientoByVehiculoPersonalAndTipoServicio(
        vehiculoPersonalId: Long,
        tipoServicio: String
    ): ReglaMantenimientoEntity?

//    @Upsert
//    abstract suspend fun insert(reglaMantenimiento: ReglaMantenimientoEntity): Long
}