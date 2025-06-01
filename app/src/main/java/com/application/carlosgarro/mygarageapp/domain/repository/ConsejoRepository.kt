package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio

interface ConsejoRepository {

    suspend fun getAllConsejos(): List<com.application.carlosgarro.mygarageapp.domain.model.consejo.ConsejoModel>

    suspend fun getConsejoByTipoServicio(tipoServicio: TipoServicio): com.application.carlosgarro.mygarageapp.domain.model.consejo.ConsejoModel?
}