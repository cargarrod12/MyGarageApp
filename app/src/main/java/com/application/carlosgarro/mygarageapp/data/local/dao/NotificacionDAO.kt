package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity

@Dao
interface NotificacionDAO {


     @Query("SELECT * FROM notificacion WHERE vehiculoPersonalId = :vehiculoPersonalId AND tipoServicio = :tipoServicio")
     suspend fun getNotificacionesByVehiculoPersonalAndTipoServicio(vehiculoPersonalId: Long, tipoServicio: TipoServicio): NotificacionEntity?

    @Upsert
     suspend fun saveNotificacion(notificacion: NotificacionEntity): Long

     @Query("SELECT * FROM notificacion WHERE vehiculoPersonalId = :vehiculoPersonalId")
     suspend fun getNotificacionesByVehiculoPersonalId(vehiculoPersonalId: Long): List<NotificacionEntity>

     @Update
     suspend fun updateNotificaciones(notificaciones: List<NotificacionEntity>): Int
}
