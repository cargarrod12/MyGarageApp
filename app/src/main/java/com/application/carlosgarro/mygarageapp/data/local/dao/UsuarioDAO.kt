package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDAO: BaseDAO<UsuarioEntity> {

    @Query("SELECT * FROM usuario WHERE email = :email")
    suspend fun getUsuarioByEmail(email: String): UsuarioEntity?
}