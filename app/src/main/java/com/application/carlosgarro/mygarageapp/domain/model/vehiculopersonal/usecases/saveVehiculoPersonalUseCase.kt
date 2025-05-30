package com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases

import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class saveVehiculoPersonalUseCase @Inject constructor(
    private val repository: VehiculoPersonalRepository
) {
    operator fun invoke(vehiculoPersonal: VehiculoPersonalModel): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    data = repository.saveVehiculoPersonal(vehiculoPersonal)
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(e.message ?: "Unknown Error")
            )
        }
    }
}