package com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.usecases

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.repository.ReglaMantenimientoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class getReglaMantenimientoByVehiculoAndTipoServicioUseCase @Inject constructor(
    private val repository: ReglaMantenimientoRepository
) {
    operator fun invoke(vehiculoPersonalId: Long, tipoServicio: TipoServicio): Flow<Resource<ReglaMantenimientoModel?>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    data = repository.getReglaMantenimientoByVehiculoAndTipoServicio(
                        vehiculoPersonalId,
                        tipoServicio.name
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