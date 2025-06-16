package com.application.carlosgarro.mygarageapp.presentation.editavehiculo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.deleteVehiculoPersonalUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoByIdUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EditarVehiculoViewModelTest {

 @get:Rule
 val instantExecutorRule = InstantTaskExecutorRule()

 private val getVehiculoByIdUseCase: getVehiculoByIdUseCase = mockk()
 private val saveVehiculoPersonal: saveVehiculoPersonalUseCase = mockk()
 private val deleteVehiculoPersonal: deleteVehiculoPersonalUseCase = mockk()
 private val firebaseAuth: FirebaseAuth = mockk()
 private val firebaseUser: FirebaseUser = mockk()

 private lateinit var viewModel: EditarVehiculoViewModel

 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setup() {
  Dispatchers.setMain(testDispatcher)
  every { firebaseAuth.currentUser } returns firebaseUser
  viewModel = EditarVehiculoViewModel(
   getVehiculoByIdUseCase,
   firebaseAuth,
   saveVehiculoPersonal,
   deleteVehiculoPersonal
  )
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `cargaVechiculo should update vehiculo and imagenSeleccionada on success`() = runTest {
  val vehiculo = VehiculoPersonalModel(id = 1L)
  coEvery { getVehiculoByIdUseCase(1L) } returns flow {
   emit(Resource.Success(vehiculo))
  }

  viewModel.setVehiculoId(1L)

  advanceUntilIdle()
  assertEquals(vehiculo, viewModel.vehiculo.value)
  assertEquals(vehiculo.imagen, viewModel.imagenSeleccionada.value)
 }

 @Test
 fun `actualizarVehiculoPersonal should emit success message on success`() = runTest {
  val vehiculo = VehiculoPersonalModel(id = 1L)
  coEvery { saveVehiculoPersonal(any()) } returns flow {
   emit(Resource.Success(true))
  }
  every { firebaseUser.email } returns "test@example.com"

  viewModel.actualizarVehiculoPersonal(vehiculo)

  viewModel.eventoMensaje.test {
   assertEquals("Vehículo actualizado correctamente", awaitItem())
   cancel()
  }
 }

 @Test
 fun `actualizarVehiculoPersonal should emit error when user not authenticated`() = runTest {
  every { firebaseAuth.currentUser } returns null

  viewModel.actualizarVehiculoPersonal(VehiculoPersonalModel())

  viewModel.eventoMensaje.test {
   assertEquals("Error: Usuario no autenticado", awaitItem())
   cancel()
  }
 }

 @Test
 fun `deleteVehiculoPersonal should emit success message on success`() = runTest {
  coEvery { deleteVehiculoPersonal(1L) } returns flow {
   emit(Resource.Success(true))
  }

  viewModel.deleteVehiculoPersonal(1L)

  viewModel.eventoMensaje.test {
   assertEquals("Vehículo eliminado correctamente", awaitItem())
   cancel()
  }
 }
}
