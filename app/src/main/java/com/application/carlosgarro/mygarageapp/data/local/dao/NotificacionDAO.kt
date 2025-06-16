package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity

@Dao
interface NotificacionDAO : BaseDAO<NotificacionEntity> {



    @Query("""
        SELECT n.* FROM Notificacion n
        INNER JOIN vehiculo_personal v ON n.vehiculoPersonalId = v.Id
        INNER JOIN Usuario u ON v.usuarioEmail = u.email
        WHERE u.email = :userEmail
    """)
     suspend fun getNotificacionesByUser(userEmail: String): List<NotificacionEntity>


     @Query("SELECT * FROM notificacion WHERE vehiculoPersonalId = :vehiculoPersonalId AND tipoServicio = :tipoServicio")
      suspend fun getNotificacionesByVehiculoPersonalAndTipoServicio(vehiculoPersonalId: Long, tipoServicio: TipoServicio): NotificacionEntity?

//    @Upsert
//    suspend fun insert(notificacion: NotificacionEntity): Long

     @Query("SELECT * FROM notificacion WHERE vehiculoPersonalId = :vehiculoPersonalId")
     suspend fun getNotificacionesByVehiculoPersonalId(vehiculoPersonalId: Long): List<NotificacionEntity>

     @Update
     suspend fun updateNotificaciones(notificaciones: List<NotificacionEntity>): Int

    @Query("""
        SELECT n.* FROM Notificacion n
        INNER JOIN vehiculo_personal v ON n.vehiculoPersonalId = v.Id
        INNER JOIN Usuario u ON v.usuarioEmail = u.email
        WHERE u.email = :userEmail
        AND n.notificado = 0
        and n.activo = 1
        and v.id = :vehiculos
        and n.kilometrosProximoServicio - v.kilometros < 1000
    """)
     suspend fun getNotificacionesByUserToNotify(userEmail: String, vehiculos: Long): List<NotificacionEntity>



    @Query("""
        DELETE FROM Notificacion
        WHERE vehiculoPersonalId IN (
            SELECT v.id FROM vehiculo_personal v
            WHERE v.usuarioEmail = :userEmail
        )
    """)
    suspend fun deleteNotificacionesByUser(userEmail: String)
}
