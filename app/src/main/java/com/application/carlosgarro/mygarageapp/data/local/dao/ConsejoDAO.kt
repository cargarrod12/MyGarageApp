package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.ConsejoEntity

@Dao
interface ConsejoDAO {


     @Query("SELECT * FROM consejo WHERE tipoServicio = :tipoServicio")
     suspend fun getConsejosByTipoServicio(tipoServicio: TipoServicio): List<ConsejoEntity>


    @Query("SELECT * FROM consejo")
    suspend fun getAllConsejos(): List<ConsejoEntity>

}