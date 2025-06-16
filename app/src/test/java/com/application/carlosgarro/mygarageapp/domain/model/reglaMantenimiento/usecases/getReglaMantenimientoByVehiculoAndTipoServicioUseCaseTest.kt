package com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.usecases

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.ReglaMantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.repository.ReglaMantenimientoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class getReglaMantenimientoByVehiculoAndTipoServicioUseCaseTest{

  @RelaxedMockK
  private lateinit var repository: ReglaMantenimientoRepository

  private lateinit var useCase: getReglaMantenimientoByVehiculoAndTipoServicioUseCase

  @Before
  fun setup() {
   MockKAnnotations.init(this)
   useCase = getReglaMantenimientoByVehiculoAndTipoServicioUseCase(repository)
  }


 @Test
 fun `when data is fetched successfully, emits Loading and Success with non-null data`() = runBlocking {
  val vehiculoPersonalId = 1L

  val mockRegla = ReglaMantenimientoModel(
   intervalo = 5000,
    tipoServicio = TipoServicio.MANTENIMIENTO_DE_BATERIA)

  coEvery {
   repository.getReglaMantenimientoByVehiculoAndTipoServicio(
    vehiculoPersonalId,
    TipoServicio.MANTENIMIENTO_DE_BATERIA.name
   )
  } returns mockRegla

  val result = useCase(vehiculoPersonalId, TipoServicio.MANTENIMIENTO_DE_BATERIA).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Success)
  assertEquals(mockRegla, (result[1] as Resource.Success).data)
 }

 @Test
 fun `when repository returns null, emits Loading and Success with null data`() = runBlocking {
  val vehiculoPersonalId = 2L
  val tipoServicio = TipoServicio.MANTENIMIENTO_DE_BATERIA

  coEvery {
   repository.getReglaMantenimientoByVehiculoAndTipoServicio(
    vehiculoPersonalId,
    tipoServicio.name
   )
  } returns null

  val result = useCase(vehiculoPersonalId, tipoServicio).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Success)
  assertEquals(null, (result[1] as Resource.Success).data)
 }

 @Test
 fun `when repository throws exception, emits Loading and Error`() = runBlocking {
  val vehiculoPersonalId = 3L
  val tipoServicio = TipoServicio.MANTENIMIENTO_DE_BATERIA

  coEvery {
   repository.getReglaMantenimientoByVehiculoAndTipoServicio(
    vehiculoPersonalId,
    tipoServicio.name
   )
  } throws RuntimeException("Error fetching regla")

  val result = useCase(vehiculoPersonalId, tipoServicio).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Error fetching regla", (result[1] as Resource.Error).message)
 }

 @Test
 fun `when exception message is null, emits Unknown Error`() = runBlocking {
  val vehiculoPersonalId = 4L
  val tipoServicio = TipoServicio.MANTENIMIENTO_DE_BATERIA

  coEvery {
   repository.getReglaMantenimientoByVehiculoAndTipoServicio(
    vehiculoPersonalId,
    tipoServicio.name
   )
  } throws RuntimeException(null as String?)

  val result = useCase(vehiculoPersonalId, tipoServicio).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Unknown Error", (result[1] as Resource.Error).message)
 }

 }