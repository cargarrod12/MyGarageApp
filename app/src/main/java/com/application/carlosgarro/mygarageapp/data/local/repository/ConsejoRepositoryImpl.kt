package com.application.carlosgarro.mygarageapp.data.local.repository

import android.util.Log
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.dao.ConsejoDAO
import com.application.carlosgarro.mygarageapp.domain.model.consejo.ConsejoModel
import com.application.carlosgarro.mygarageapp.domain.model.consejo.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.ConsejoRepository
import javax.inject.Inject


class ConsejoRepositoryImpl @Inject constructor(
    private val consejoDao: ConsejoDAO,
) : ConsejoRepository {

    override suspend fun getAllConsejos(): List<ConsejoModel> {
        try {
            val result = consejoDao.getAllConsejos()
            return result.map { it.toModel() }
            Log.i("ConsejoRepositoryImpl", "Fetched ${result.size} consejos successfully.")
        } catch (e: Exception) {
            Log.e("ConsejoRepositoryImpl", "Error fetching consejos: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun getConsejoByTipoServicio(tipoServicio: TipoServicio): ConsejoModel? {
        try {
            val result = consejoDao.getConsejosByTipoServicio(tipoServicio)
            if (result.isNotEmpty()) {
                Log.i("ConsejoRepositoryImpl", "Fetched consejo for tipoServicio: $tipoServicio successfully.")
                return result.first().toModel()
            } else {
                Log.w("ConsejoRepositoryImpl", "No consejo found for tipoServicio: $tipoServicio")
            }
        } catch (e: Exception) {
            Log.e("ConsejoRepositoryImpl", "Error fetching consejo by tipoServicio: ${e.message}")
        }
        return null
    }
}