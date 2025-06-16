package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity


@Dao
interface MantenimientoDAO : BaseDAO<MantenimientoEntity> {

    @Query("SELECT * FROM mantenimiento")
    suspend fun getAllMantenimientos(): List<MantenimientoEntity>

    @Query("SELECT * FROM mantenimiento WHERE vehiculoPersonalId = :vehiculoPersonalId")
    suspend fun getMantenimientosByVehiculoId(vehiculoPersonalId: Long): List<MantenimientoEntity>

//    @Upsert
//    suspend fun insert(toEntity: MantenimientoEntity): Long


    @Query("""
        SELECT m.* FROM mantenimiento m
        INNER JOIN vehiculo_personal v ON m.vehiculoPersonalId = v.Id
        WHERE v.usuarioEmail = :userEmail
    """)
    suspend fun getMantenimientosByUsuario(userEmail: String): List<MantenimientoEntity>

    @Query("""
        DELETE FROM mantenimiento
        WHERE vehiculoPersonalId IN (
            SELECT v.id FROM vehiculo_personal v
            WHERE v.usuarioEmail = :email
        )
    """)
    suspend fun deleteMantenimientosByUser(email: String)
}