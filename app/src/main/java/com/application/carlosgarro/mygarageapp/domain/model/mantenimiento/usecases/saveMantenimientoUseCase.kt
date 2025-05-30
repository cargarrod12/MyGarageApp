package com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases

import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.repository.MantenimientoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class saveMantenimientoUseCase @Inject constructor(
    private val repository: MantenimientoRepository
) {
    operator fun invoke(mantenimiento: MantenimientoModel): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    data = repository.saveMantenimiento(mantenimiento)
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(e.message ?: "Unknown Error")
            )
        }
    }
}