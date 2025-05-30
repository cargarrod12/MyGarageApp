package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity


@Dao
interface ReglaMantenimientoDAO {

    @Query("SELECT * FROM regla_mantenimiento WHERE vehiculoId = :vehiculoPersonalId AND tipoServicio = :tipoServicio")
    suspend fun getReglaMantenimientoByVehiculoPersonalAndTipoServicio(
        vehiculoPersonalId: Long,
        tipoServicio: String
    ): ReglaMantenimientoEntity?
}