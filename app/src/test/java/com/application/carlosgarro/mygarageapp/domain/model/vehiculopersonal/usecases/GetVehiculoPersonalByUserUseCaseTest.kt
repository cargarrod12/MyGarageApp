package com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases

import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class GetVehiculoPersonalByUserUseCaseTest{

  @RelaxedMockK
  private lateinit var repository: VehiculoPersonalRepository

  private lateinit var useCase: getVehiculoPersonalByUserUseCase

  @Before
  fun setup() {
   MockKAnnotations.init(this)
   useCase = getVehiculoPersonalByUserUseCase(repository)
  }

 @Test
 fun `when data is fetched successfully, emits Loading and Success`() = runBlocking {
  val usuarioEmail = "prueba@prueba.com"

  val mockList = listOf(
   VehiculoPersonalModel(
    modelo = VehiculoModel(
     id = 1L,
     marca = MarcaVehiculo.HONDA,
     modelo = ModeloVehiculo.CIVIC
    ),
    anyo = 2020,
    usuarioEmail = usuarioEmail
   )
  )

  coEvery { repository.getVehiculosPersonalesByUsuario(usuarioEmail) } returns mockList

  val result = useCase(usuarioEmail).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Success)
  assertEquals(mockList, (result[1] as Resource.Success).data)
 }


 @Test
 fun `when repository throws exception, emits Loading and Error`() = runBlocking {
  val usuarioEmail = "prueba@prueba.com"

  coEvery {
   repository.getVehiculosPersonalesByUsuario(usuarioEmail)
  } throws RuntimeException("Error al obtener los datos")

  val result = useCase(usuarioEmail).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Error al obtener los datos", (result[1] as Resource.Error).message)
 }

 @Test
 fun `when exception has null message, emits Unknown Error`() = runBlocking {
  val usuarioEmail = "prueba@prueba.com"

  coEvery { repository.getVehiculosPersonalesByUsuario(usuarioEmail) } throws RuntimeException(null as String?)

  val result = useCase(usuarioEmail).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Unknown Error", (result[1] as Resource.Error).message)
 }


 }