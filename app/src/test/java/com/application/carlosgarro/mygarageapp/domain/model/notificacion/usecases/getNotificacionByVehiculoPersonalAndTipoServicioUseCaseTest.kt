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


 class getNotificacionByVehiculoPersonalAndTipoServicioUseCaseTest{

  @RelaxedMockK
    private lateinit var repository: NotificacionRepository

    private lateinit var useCase: getNotificacionByVehiculoPersonalAndTipoServicioUseCase


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = getNotificacionByVehiculoPersonalAndTipoServicioUseCase(repository)
    }
     @Test
     fun `when repository returns a notification, emits Loading and Success`() = runBlocking {
         val vehiculoId = 1L
         val tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS

         val notificacion =     NotificacionModel(
             id = 1L,
             vehiculoPersonalId = 1L,
             reglaMantenimientoId = 1L,
             tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
             kilometrosUltimoServicio = 10000,
             kilometrosProximoServicio = 15000,
             notificado = false,
             activo = true
         )

         coEvery {
             repository.getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio)
         } returns notificacion

         val result = useCase(vehiculoId, tipoServicio).toList()

         assertTrue(result[0] is Resource.Loading)
         assertTrue(result[1] is Resource.Success)
         assertEquals(notificacion, (result[1] as Resource.Success).data)
     }

     @Test
     fun `when repository throws exception, emits Loading and Error`() = runBlocking {
         val vehiculoId = 2L
         val tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS

         coEvery {
             repository.getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio)
         } throws RuntimeException("Error de repositorio")

         val result = useCase(vehiculoId, tipoServicio).toList()

         assertTrue(result[0] is Resource.Loading)
         assertTrue(result[1] is Resource.Error)
         assertEquals("Error de repositorio", (result[1] as Resource.Error).message)
     }

     @Test
     fun `when exception message is null, emits Unknown Error`() = runBlocking {
         val vehiculoId = 3L
         val tipoServicio = TipoServicio.CAMBIO_DE_BUJIAS

         coEvery {
             repository.getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio)
         } throws RuntimeException(null as String?)

         val result = useCase(vehiculoId, tipoServicio).toList()

         assertTrue(result[0] is Resource.Loading)
         assertTrue(result[1] is Resource.Error)
         assertEquals("Unknown Error", (result[1] as Resource.Error).message)
     }



 }