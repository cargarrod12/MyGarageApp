package com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases

import android.util.Log
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.repository.MantenimientoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class getMantenimientosByVehiculoPersonal @Inject constructor(
    private val repository: MantenimientoRepository
) {
    operator fun invoke(id: Long): Flow<Resource<List<MantenimientoModel>>> = flow {
        Log.i("getMantenimientosByVehiculoPersonal", "ID: $id")
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    data = repository.getMantenimientosByVehiculoPersonal(id) ?: emptyList()
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error(e.message ?: "Unknown Error")
            )
        }
    }


}