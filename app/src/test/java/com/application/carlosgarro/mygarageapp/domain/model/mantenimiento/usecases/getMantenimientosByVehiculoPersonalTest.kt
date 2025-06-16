package com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.repository.MantenimientoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class getMantenimientosByVehiculoPersonalTest{

  @RelaxedMockK
  private lateinit var repository: MantenimientoRepository

  private lateinit var useCase: getMantenimientosByVehiculoPersonal

  @Before
  fun setup() {
   MockKAnnotations.init(this)
   useCase = getMantenimientosByVehiculoPersonal(repository)
  }


  @Test
  fun `when repository returns data, emits Loading and Success`() = runBlocking {
   val vehiculoId = 1L
   val mockMantenimientos = listOf(
    MantenimientoModel(),
    MantenimientoModel(
        id = 2L,
        vehiculoId = vehiculoId,
        tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
        fechaServicio = LocalDate.now(),
        kilometrosServicio = 15000,
        precio = 500.0,
    )
   )

   coEvery { repository.getMantenimientosByVehiculoPersonal(vehiculoId) } returns mockMantenimientos

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(mockMantenimientos, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when repository throws exception, emits Loading and Error`() = runBlocking {
   val vehiculoId = 2L

   coEvery { repository.getMantenimientosByVehiculoPersonal(vehiculoId) } throws RuntimeException("Error al obtener mantenimientos")

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Error al obtener mantenimientos", (result[1] as Resource.Error).message)
  }

  @Test
  fun `when exception has null message, emits Unknown Error`() = runBlocking {
   val vehiculoId = 3L

   coEvery { repository.getMantenimientosByVehiculoPersonal(vehiculoId) } throws RuntimeException(null as String?)

   val result = useCase(vehiculoId).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Unknown Error", (result[1] as Resource.Error).message)
  }
 }