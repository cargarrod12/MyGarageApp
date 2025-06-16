package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto

@Dao
interface VehiculoPersonalDAO : BaseDAO<VehiculoPersonalEntity> {

    @Transaction
    @Query("SELECT * FROM vehiculo_personal WHERE usuarioEmail = :usuarioEmail and borrado in (:borrado)")
    suspend fun getVehiculosPersonalesByUsuario(usuarioEmail: String, borrado: List<Int>)
    : List<VehiculoPersonalCompleto>

    @Query("SELECT * FROM vehiculo_personal")
    suspend fun getAllVehiculosPersonales(): List<VehiculoPersonalEntity>

//    @Upsert
//    suspend fun insert(vehiculoPersonal: VehiculoPersonalEntity): Long

    @Query("Update vehiculo_personal set borrado = 1, fechaUltModificacion = :fecha WHERE id = :vehiculoId")
    suspend fun deleteVehiculoPersonal(vehiculoId: Long, fecha: String): Int


    @Transaction
    @Query("SELECT * FROM vehiculo_personal WHERE id = :id")
    suspend fun getVehiculoPersonalById(id: Long): VehiculoPersonalCompleto?

    @Query("""
        DELETE FROM vehiculo_personal
        WHERE usuarioEmail = :email
    """)
    suspend fun deleteVehiculoPersonalByUser(email: String)

}