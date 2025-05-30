package com.application.carlosgarro.mygarageapp.data.local.repository

import android.util.Log
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toModel
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import javax.inject.Inject

class NotificacionRepositoryImpl @Inject constructor(
    private val notificacionDao: NotificacionDAO
) : NotificacionRepository {

    override suspend fun getNotificacionByVehiculoPersonalAndTipoServicio(
        vehiculoPersonalId: Long,
        tipoServicio: TipoServicio
    ): NotificacionModel? {
        val result =  notificacionDao.getNotificacionesByVehiculoPersonalAndTipoServicio(
            vehiculoPersonalId,
            tipoServicio
        )
        return result?.toModel()
    }

    override suspend fun saveNotificacion(notificacion: NotificacionModel): Boolean {
        try {
            Log.i("NotificacionRepositoryImp", "Saving notificacion: $notificacion")
            val result = notificacionDao.saveNotificacion(notificacion.toEntity())
            Log.i("NotificacionRepositoryImp", "Notificacion saved with id: $result")
            return result.toInt() != -1
        }catch (e: Exception){
            Log.e("NotificacionRepositoryImp", "Error saving notificacion: ${e.message}")
        }
        return false
    }

    override suspend fun getNotificacionesByVehiculoPersonalId(vehiculoPersonalId: Long): List<NotificacionModel>{
        val result = notificacionDao.getNotificacionesByVehiculoPersonalId(vehiculoPersonalId)
        return result.map { it.toModel() }
    }

    override suspend fun updateNotificaciones(notificaciones: List<NotificacionModel>): Boolean {
        try {
            Log.i("NotificacionRepositoryImp", "update notificaciones: $notificaciones")
            val result = notificacionDao.updateNotificaciones(notificaciones.map { it.toEntity() })
            Log.i("NotificacionRepositoryImp", "Notificaciones saved: $result")
            return result != 0
        }catch (e: Exception){
            Log.e("NotificacionRepositoryImp", "Error saving notificacion: ${e.message}")
        }
        return false
    }
}