package com.application.carlosgarro.mygarageapp.presentation.historial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases.getMantenimientosByVehiculoPersonal
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases.saveMantenimientoUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionByVehiculoPersonalAndTipoServicioUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.saveNotificacionUseCase
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.usecases.getReglaMantenimientoByVehiculoAndTipoServicioUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class HistorialViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private lateinit var historialViewModel: HistorialViewModel

 @RelaxedMockK
 private lateinit var getMantenimientosByVehiculoPersonal: getMantenimientosByVehiculoPersonal

 @RelaxedMockK
 private lateinit var saveMantenimientoUseCase: saveMantenimientoUseCase

 @RelaxedMockK
 private lateinit var getNotificacionByVehiculoPersonalUseCase: getNotificacionByVehiculoPersonalAndTipoServicioUseCase

 @RelaxedMockK
 private lateinit var getReglaMantenimientoByVehiculoPersonalAndTipoServicio: getReglaMantenimientoByVehiculoAndTipoServicioUseCase

 @RelaxedMockK
 private lateinit var saveNotificacionUseCase: saveNotificacionUseCase

 @RelaxedMockK
 private lateinit var getVehiculoPersonalByIdUseCase: getVehiculoByIdUseCase

 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setup() {
  MockKAnnotations.init(this)
  Dispatchers.setMain(testDispatcher)
  historialViewModel = HistorialViewModel(
   getMantenimientosByVehiculoPersonal,
   saveMantenimientoUseCase,
   getNotificacionByVehiculoPersonalUseCase,
   getReglaMantenimientoByVehiculoPersonalAndTipoServicio,
   saveNotificacionUseCase,
   getVehiculoPersonalByIdUseCase
  )
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `setVehiculoId triggers cargaMantenimientos and updates mantenimientos and vehiculo`() = runTest {
  val vehiculoId = 1L
  val mantenimientosList = listOf(MantenimientoModel(id = 1L))
  val vehiculoMock = VehiculoPersonalModel(id = vehiculoId)

  coEvery { getMantenimientosByVehiculoPersonal(vehiculoId) } returns flowOf(Resource.Success(mantenimientosList))
  coEvery { getVehiculoPersonalByIdUseCase(vehiculoId) } returns flowOf(Resource.Success(vehiculoMock))

  historialViewModel.setVehiculoId(vehiculoId)

  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(mantenimientosList, historialViewModel.mantenimientos.value)
  assertEquals(vehiculoMock, historialViewModel.vehiculo.value)
  assertEquals(false, historialViewModel.isLoading.value)
 }

 @Test
 fun `addEntradaMantenimiento saves mantenimiento and refreshes data`() = runTest {
  val vehiculoId = 1L
  val mantenimientoToAdd = MantenimientoModel(vehiculoId = vehiculoId, id = 0L)
  val mantenimientoSaved = mantenimientoToAdd.copy(id = 10L)
  val mantenimientosList = listOf(mantenimientoSaved)
  val vehiculoMock = VehiculoPersonalModel(id = vehiculoId)

  historialViewModel.setVehiculoId(vehiculoId)

  coEvery { saveMantenimientoUseCase(any()) } returns flowOf(Resource.Success(true))
  coEvery { getMantenimientosByVehiculoPersonal(vehiculoId) } returns flowOf(Resource.Success(mantenimientosList))
  coEvery { getVehiculoPersonalByIdUseCase(vehiculoId) } returns flowOf(Resource.Success(vehiculoMock))

  coEvery {
   getNotificacionByVehiculoPersonalUseCase(vehiculoId, mantenimientoToAdd.tipoServicio)
  } returns flowOf(Resource.Success(null))

  coEvery {
   getReglaMantenimientoByVehiculoPersonalAndTipoServicio(vehiculoId, mantenimientoToAdd.tipoServicio)
  } returns flowOf(Resource.Success(ReglaMantenimientoModel(
   id = 1L, intervalo = 10000,
   vehiculoId = vehiculoId,
   tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE
  )))

  coEvery { saveNotificacionUseCase(any()) } returns flowOf(Resource.Success(true))

  val mensajeCollector = mutableListOf<String>()
  val job = launch {
   historialViewModel.eventoMensaje.collect {
    mensajeCollector.add(it)
   }
  }

  historialViewModel.addEntradaMantenimiento(mantenimientoToAdd)

  testDispatcher.scheduler.runCurrent()

  coVerify { saveMantenimientoUseCase(mantenimientoToAdd) }
  coVerify { getMantenimientosByVehiculoPersonal(vehiculoId) }
  coVerify { getNotificacionByVehiculoPersonalUseCase(vehiculoId, mantenimientoToAdd.tipoServicio) }
  coVerify { getReglaMantenimientoByVehiculoPersonalAndTipoServicio(vehiculoId, mantenimientoToAdd.tipoServicio) }
  coVerify { saveNotificacionUseCase(any()) }

  // Verificar que el mensaje esperado se emitió
  assert(mensajeCollector.contains("Nueva entrada del Historial añadida correctamente"))

  job.cancel()
 }

 @Test
 fun `addEntradaMantenimiento emits error message on failure`() = runTest {
  val vehiculoId = 1L
  val mantenimientoToAdd = MantenimientoModel(vehiculoId = vehiculoId)

  historialViewModel.setVehiculoId(vehiculoId)

  coEvery { saveMantenimientoUseCase(any()) } returns flowOf(Resource.Error("Error guardando mantenimiento"))

  val mensajeCollector = mutableListOf<String>()
  val job = launch {
   historialViewModel.eventoMensaje.collect {
    mensajeCollector.add(it)
   }
  }

  historialViewModel.addEntradaMantenimiento(mantenimientoToAdd)

  testDispatcher.scheduler.advanceUntilIdle()

  assert(mensajeCollector.contains("Error al añadir nueva entrada del Historial"))

  job.cancel()
 }
}
