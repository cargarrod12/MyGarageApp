package com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class getNotificacionesByUserToNotifyTest{

    @RelaxedMockK
    private lateinit var repository: NotificacionRepository

    private lateinit var useCase: getNotificacionesByUserToNotify

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = getNotificacionesByUserToNotify(repository)
    }


  @Test
  fun `when repository returns notifications, returns the list`() = runBlocking {
   val userEmail = "test@example.com"
   val vehiculoId = 1L

   val mockList = listOf(
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
     vehiculoPersonalId = 1L,
     reglaMantenimientoId = 3L,
     tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS,
     kilometrosUltimoServicio = 11000,
     kilometrosProximoServicio = 18000,
     notificado = false,
     activo = true
    )
   )

   coEvery { repository.getNotificacionesByUserToNotify(userEmail, vehiculoId) } returns mockList

   val result = useCase.invoke(userEmail, vehiculoId)

   assertEquals(mockList, result)
  }

  @Test
  fun `when repository throws exception, returns empty list`() = runBlocking {
   val userEmail = "test@example.com"
   val vehiculoId = 1L

   coEvery { repository.getNotificacionesByUserToNotify(userEmail, vehiculoId) } throws RuntimeException("Error")

   val result = useCase.invoke(userEmail, vehiculoId)

   assertEquals(emptyList<NotificacionModel>(), result)
  }

 }