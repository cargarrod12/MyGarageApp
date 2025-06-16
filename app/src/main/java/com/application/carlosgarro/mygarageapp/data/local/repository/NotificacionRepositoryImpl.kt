package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toModel
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
            val result = notificacionDao.insert(notificacion.toEntity())
            return result.toInt() != -1
        }catch (e: Exception){
            println(e.message)
        }
        return false
    }

    override suspend fun getNotificacionesByVehiculoPersonalId(vehiculoPersonalId: Long): List<NotificacionModel>{
        val result = notificacionDao.getNotificacionesByVehiculoPersonalId(vehiculoPersonalId)
        return result.map { it.toModel() }
    }

    override suspend fun updateNotificaciones(notificaciones: List<NotificacionModel>): Boolean {
        try {
            val result = notificacionDao.updateNotificaciones(notificaciones.map { it.toEntity() })
            return result != 0
        }catch (e: Exception){
            println(e.message)
        }
        return false
    }

    override suspend fun getNotificacionesByUserToNotify(userEmail: String, vehiculo: Long): List<NotificacionModel> {
        try {
        val result = notificacionDao.getNotificacionesByUserToNotify(userEmail, vehiculo)
        return result.map { it.toModel() }
        }catch (e: Exception){
            println(e.message)
        }
        return emptyList()
    }
}