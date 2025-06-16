package com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases


import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class updateNotificacionesUseCaseTest{

  @RelaxedMockK
  private lateinit var repository: NotificacionRepository

  private lateinit var useCase: updateNotificacionesUseCase

  @Before
  fun setup() {
   MockKAnnotations.init(this)
   useCase = updateNotificacionesUseCase(repository)
  }

  @Test
  fun `when update is successful, emits Loading and Success with true`() = runBlocking {
   val notificaciones = listOf(
        NotificacionModel(
         id = 1L,
         vehiculoPersonalId = 1L,
         reglaMantenimientoId = 1L,
         tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
         kilometrosUltimoServicio = 10000,
         kilometrosProximoServicio = 15000,
         notificado = false,
         activo = true
        ),
        NotificacionModel(
         id = 2L,
         vehiculoPersonalId = 2L,
         reglaMantenimientoId = 1L,
         tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
         kilometrosUltimoServicio = 10000,
         kilometrosProximoServicio = 15000,
         notificado = false,
         activo = true
        )
   )

   coEvery { repository.updateNotificaciones(notificaciones) } returns true

   val result = useCase(notificaciones).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(true, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when update returns false, emits Loading and Success with false`() = runBlocking {
   val notificaciones = listOf<NotificacionModel>() // lista vacía o con datos

   coEvery { repository.updateNotificaciones(notificaciones) } returns false

   val result = useCase(notificaciones).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(false, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when repository throws exception, emits Loading and Error`() = runBlocking {
   val notificaciones = listOf(
    NotificacionModel(
        id = 1L,
        vehiculoPersonalId = 1L,
        reglaMantenimientoId = 1L,
        tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
        kilometrosUltimoServicio = 10000,
        kilometrosProximoServicio = 15000,
        notificado = false,
        activo = true
    ),
    NotificacionModel(
     id = 2L,
     vehiculoPersonalId = 2L,
     reglaMantenimientoId = 1L,
     tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
     kilometrosUltimoServicio = 10000,
     kilometrosProximoServicio = 15000,
     notificado = false,
     activo = true
    )
   )

   coEvery { repository.updateNotificaciones(notificaciones) } throws RuntimeException("Error actualizando")

   val result = useCase(notificaciones).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Error actualizando", (result[1] as Resource.Error).message)
  }

  @Test
  fun `when exception message is null, emits Unknown Error`() = runBlocking {
   val notificaciones = listOf<NotificacionModel>()

   coEvery { repository.updateNotificaciones(notificaciones) } throws RuntimeException(null as String?)

   val result = useCase(notificaciones).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Unknown Error", (result[1] as Resource.Error).message)
  }

 }