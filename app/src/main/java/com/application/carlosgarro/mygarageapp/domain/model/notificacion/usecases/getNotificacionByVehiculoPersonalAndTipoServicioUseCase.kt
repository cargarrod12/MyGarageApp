package com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class getNotificacionByVehiculoPersonalAndTipoServicioUseCase @Inject constructor(
    private val repository: NotificacionRepository
) {
    operator fun invoke(vehiculoPersonalId: Long, tipoServicio: TipoServicio): Flow<Resource<NotificacionModel>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    data = repository.getNotificacionByVehiculoPersonalAndTipoServicio(
                        vehiculoPersonalId,
                        tipoServicio
                    )
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(e.message ?: "Unknown Error")
            )
        }
    }

}
