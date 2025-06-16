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


 class GetVehiculoByIdUseCaseTest {

  @RelaxedMockK
  private lateinit var repository: VehiculoPersonalRepository

  private lateinit var useCase: getVehiculoByIdUseCase

  @Before
  fun setup() {
   MockKAnnotations.init(this)
   useCase = getVehiculoByIdUseCase(repository)
  }

  @Test
  fun `when vehicle is found, emits Loading and Success with data`() = runBlocking {
   val vehiculoId = 1L
   val mockVehiculo = VehiculoPersonalModel(
    modelo = VehiculoModel(
     id = vehiculoId,
     marca = MarcaVehiculo.HONDA,
     modelo = ModeloVehiculo.CIVIC
    ),
    anyo = 2020,
    usuarioEmail = "prueba@prueba.com"
   )

   coEvery { repository.getVehiculoPersonalById(vehiculoId) } returns mockVehiculo

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(mockVehiculo, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when repository returns null, emits Loading and Success with default model`() = runBlocking {
   val vehiculoId = 2L

   coEvery { repository.getVehiculoPersonalById(vehiculoId) } returns null

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)

   val defaultModel = VehiculoPersonalModel()
   assertEquals(defaultModel, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when repository throws exception, emits Loading and Error`() = runBlocking {
   val vehiculoId = 3L

   coEvery { repository.getVehiculoPersonalById(vehiculoId) } throws RuntimeException("Error al buscar por ID")

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Error al buscar por ID", (result[1] as Resource.Error).message)
  }

  @Test
  fun `when exception message is null, emits Unknown Error`() = runBlocking {
   val vehiculoId = 4L

   coEvery { repository.getVehiculoPersonalById(vehiculoId) } throws RuntimeException(null as String?)

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Unknown Error", (result[1] as Resource.Error).message)
  }

 }



