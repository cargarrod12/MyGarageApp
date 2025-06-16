package com.application.carlosgarro.mygarageapp.presentation.vehiculo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehiculoViewModelTest{
    @RelaxedMockK
    private lateinit var getVehiculoByIdUseCase: getVehiculoByIdUseCase

     private lateinit var viewModel: VehiculoViewModel

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = VehiculoViewModel(getVehiculoByIdUseCase)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun whenRepositoryReturnsLoadingThenSuccess() = runTest {
        val vehiculoMock = VehiculoPersonalModel()
        val flow = flow {
            emit(Resource.Loading())
            emit(Resource.Success(vehiculoMock))
        }

        coEvery { getVehiculoByIdUseCase.invoke(1L) } returns flow

        viewModel.setVehiculoId(1L)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(vehiculoMock, viewModel.vehiculo.value)
    }
    @Test
    fun whenRepositoryReturnsLoadingThenError() = runTest {
        val errorMessage = "Error cargando vehículo"
        val flow = flow<Resource<VehiculoPersonalModel>> {
            emit(Resource.Loading())
            emit(Resource.Error(errorMessage))
        }

        coEvery { getVehiculoByIdUseCase.invoke(2L) } returns flow

        viewModel.setVehiculoId(2L)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(VehiculoPersonalModel(), viewModel.vehiculo.value)
    }
    @Test
    fun whenVehiculoIdIsZeroDoesNotCallUseCase() = runTest {

        viewModel.setVehiculoId(0L)
        testDispatcher.scheduler.advanceUntilIdle()


        assertEquals(false, viewModel.isLoading.value)
        assertEquals(VehiculoPersonalModel(), viewModel.vehiculo.value)
    }
    @Test
    fun whenVehiculoIdIsNull() = runTest {
        val flow = flow<Resource<VehiculoPersonalModel>> {
            emit(Resource.Loading())
        }


        (null as Long?)?.let { viewModel.setVehiculoId(it) }
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(VehiculoPersonalModel(), viewModel.vehiculo.value)
    }
}



