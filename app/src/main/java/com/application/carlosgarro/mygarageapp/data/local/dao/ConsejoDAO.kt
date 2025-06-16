package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.ConsejoEntity

@Dao
interface ConsejoDAO : BaseDAO<ConsejoEntity>{


     @Query("SELECT * FROM consejo WHERE tipoServicio = :tipoServicio")
      suspend fun getConsejosByTipoServicio(tipoServicio: TipoServicio): List<ConsejoEntity>


    @Query("SELECT * FROM consejo")
     suspend fun getAllConsejos(): List<ConsejoEntity>

    @Upsert
    override suspend fun insert(consejo: ConsejoEntity): Long

}