package com.application.carlosgarro.mygarageapp.presentation.consejo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.external.consultasia.api.ConsejosApi
import com.application.carlosgarro.mygarageapp.data.external.consultasia.request.Message
import com.application.carlosgarro.mygarageapp.data.external.consultasia.response.ChatGPTResponse
import com.application.carlosgarro.mygarageapp.data.external.consultasia.response.Choice
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
class ConsejoViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private lateinit var viewModel: ConsejoViewModel

 @RelaxedMockK
 private lateinit var firebaseAuth : FirebaseAuth

 @RelaxedMockK
 private lateinit var firebaseUser : FirebaseUser

 @RelaxedMockK
 private lateinit var getVehiculoPersonalByUser : getVehiculoPersonalByUserUseCase

 @RelaxedMockK
 private lateinit var consejosApi : ConsejosApi

 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setUp() {
    MockKAnnotations.init(this)
  Dispatchers.setMain(testDispatcher)
  every { firebaseAuth.currentUser } returns firebaseUser
  every { firebaseUser.email } returns "test@example.com"

  viewModel = ConsejoViewModel(firebaseAuth, getVehiculoPersonalByUser)
 }

 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `cargaVechiculos with Resource Success updates list`() = runTest {
  val vehiculos = listOf(VehiculoPersonalModel(id = 1L, modelo = VehiculoModel(1L, MarcaVehiculo.BMW, ModeloVehiculo.MODEL_3), anyo = 2018, kilometros = 80000))
  coEvery { getVehiculoPersonalByUser("test@example.com") } returns flow {
   emit(Resource.Loading())
   emit(Resource.Success(vehiculos))
  }

  viewModel.cargaVechiculos()
  advanceUntilIdle()

  assertEquals(vehiculos, viewModel.listaVehiculoPersonal.value)
  assertEquals(false, viewModel.isLoading.value)
 }

 @Test
 fun `cargaVechiculos with Resource Error sets empty list`() = runTest {
  coEvery { getVehiculoPersonalByUser("test@example.com") } returns flow {
   emit(Resource.Loading())
   emit(Resource.Error("error"))
  }

  viewModel.cargaVechiculos()
  advanceUntilIdle()

  assertEquals(emptyList<VehiculoPersonalModel>(), viewModel.listaVehiculoPersonal.value)
  assertEquals(false, viewModel.isLoading.value)
 }

 @Test
 fun `consultarIA with valid response sets respuesta`() = runTest {
  val vehiculo = VehiculoPersonalModel(id = 1L, modelo = VehiculoModel(1L, MarcaVehiculo.BMW, ModeloVehiculo.MODEL_3), anyo = 2018, kilometros = 80000)
  val choice = Choice(message = Message(role = "assistant", content = "Respuesta de prueba"))
  val response = ChatGPTResponse(choices = listOf(choice))

  coEvery { consejosApi.enviarPregunta(any()) } returns response

  viewModel.consultarIA(vehiculo, "¿Qué mantenimiento debo hacer?", consejosApi)
  advanceUntilIdle()

  assertEquals("Respuesta de prueba", viewModel.respuesta.value)
  assertEquals(false, viewModel.isLoading.value)
 }

 @Test
 fun `consultarIA with exception emits error message`() = runTest {
  val vehiculo = VehiculoPersonalModel(id = 1L, modelo = VehiculoModel(1L, MarcaVehiculo.BMW, ModeloVehiculo.MODEL_3), anyo = 2018, kilometros = 80000)
  coEvery { consejosApi.enviarPregunta(any()) } throws RuntimeException("API Error")

  val mensajes = mutableListOf<String>()
  backgroundScope.launch {
   viewModel.eventoMensaje.collect {
    mensajes.add(it)
   }
  }

  viewModel.consultarIA(vehiculo, "¿Qué mantenimiento debo hacer?", consejosApi)
  testDispatcher.scheduler.runCurrent()

  assertEquals("Ha ocurrido un error al realizar la consulta. Por favor, inténtelo de nuevo más tarde.", mensajes.firstOrNull())
  assertEquals(false, viewModel.isLoading.value)
 }

 @Test
 fun `setRespuestaVacia should clear respuesta`() {
  viewModel.setRespuestaVacia()
  assertEquals("", viewModel.respuesta.value)
 }
}
