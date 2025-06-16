package com.application.carlosgarro.mygarageapp.presentation.notificaciones

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionesByVehiculoPersonalIdUseCase
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.updateNotificacionesUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificacionesViewModelTest {

 @RelaxedMockK
 private lateinit var getNotificacionesByVehiculoPersonalIdUseCase: getNotificacionesByVehiculoPersonalIdUseCase

 @RelaxedMockK
 private lateinit var updateNotificacionesUseCase: updateNotificacionesUseCase

 private lateinit var viewModel: NotificacionesViewModel

 private val testDispatcher = StandardTestDispatcher()

 @get:Rule
 val instantExecutorRule = InstantTaskExecutorRule()

 @Before
 fun setup() {
  MockKAnnotations.init(this)
  Dispatchers.setMain(testDispatcher)
  viewModel = NotificacionesViewModel(
   getNotificacionesByVehiculoPersonalIdUseCase,
   updateNotificacionesUseCase
  )
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `setVehiculoId loads notifications successfully`() = runTest {
  val vehiculoId = 1L
  val notificacionesMock = listOf(
   NotificacionModel(id = 1, vehiculoPersonalId = vehiculoId, tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE),
   NotificacionModel(id = 2, vehiculoPersonalId = vehiculoId, tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS),
  )

  val flow = flow {
   emit(Resource.Loading())
   emit(Resource.Success(notificacionesMock))
  }

  coEvery { getNotificacionesByVehiculoPersonalIdUseCase(vehiculoId) } returns flow

  viewModel.setVehiculoId(vehiculoId)

  // Avanzar el scheduler para que corran las corutinas
  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(false, viewModel.isLoading.value)
  assertEquals(notificacionesMock, viewModel.notificaciones.value)
 }


 @Test
 fun `setVehiculoId loads notifications successfully but null`() = runTest {
  val vehiculoId = 1L
  val notificacionesMock = listOf(
   NotificacionModel(id = 1, vehiculoPersonalId = vehiculoId, tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE),
   NotificacionModel(id = 2, vehiculoPersonalId = vehiculoId, tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS),
  )

  val flow = flow {
   emit(Resource.Loading())
   emit(Resource.Success( null as List<NotificacionModel>?))
  }

  coEvery { getNotificacionesByVehiculoPersonalIdUseCase(vehiculoId) } returns flow

  viewModel.setVehiculoId(vehiculoId)

  // Avanzar el scheduler para que corran las corutinas
  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(false, viewModel.isLoading.value)
  assertTrue(viewModel.notificaciones.value?.isEmpty() == true)
 }

 @Test
 fun `setVehiculoId handles error loading notifications`() = runTest {
  val vehiculoId = 2L
  val errorMsg = "Error loading"
  val flow = flow<Resource<List<NotificacionModel>>> {
   emit(Resource.Loading())
   emit(Resource.Error(errorMsg))
  }

  coEvery { getNotificacionesByVehiculoPersonalIdUseCase(vehiculoId) } returns flow
 }


 @Test
 fun `updateNotificaciones emits loading then success`() = runTest {
  val notificaciones = listOf<NotificacionModel>()
  val flow = flow {
   emit(Resource.Loading())
   emit(Resource.Success(true))
  }
  coEvery { updateNotificacionesUseCase(notificaciones) } returns flow

  val emittedMessages = mutableListOf<String>()
  val job = launch {
   viewModel.eventoMensaje.collect {
    emittedMessages.add(it)
   }
  }

  viewModel.updateNotificaciones(notificaciones)

  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(false, viewModel.isLoading.value)
  assertTrue(emittedMessages.contains("Notificaciones actualizadas correctamente"))

  job.cancel()
 }

 @Test
 fun `updateNotificaciones emits loading then error`() = runTest {
  val mensajeError= "Error updating"
  val notificaciones = listOf<NotificacionModel>()
  val flow = flow<Resource<Boolean>> {
    emit(Resource.Loading())
    emit(Resource.Error(mensajeError))
  }
  coEvery { updateNotificacionesUseCase(notificaciones) } returns flow

  val emittedMessages = mutableListOf<String>()
  val job = launch {
   viewModel.eventoMensaje.collect {
    emittedMessages.add(it)
   }
  }

  viewModel.updateNotificaciones(notificaciones)

  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(false, viewModel.isLoading.value)
  assertTrue(emittedMessages.contains("Error al actualizar notificaciones"))

  job.cancel()
 }
}
