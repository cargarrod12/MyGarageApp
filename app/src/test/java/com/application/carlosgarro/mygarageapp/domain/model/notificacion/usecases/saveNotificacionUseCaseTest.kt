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


class saveNotificacionUseCaseTest{

    @RelaxedMockK
    private lateinit var repository: NotificacionRepository

    private lateinit var useCase: saveNotificacionUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = saveNotificacionUseCase(repository)
    }

    @Test
    fun `when save is successful, emits Loading and Success with true`() = runBlocking {
        val notificacion = NotificacionModel(
        id = 1L,
        vehiculoPersonalId = 1L,
        reglaMantenimientoId = 1L,
        tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
        kilometrosUltimoServicio = 10000,
        kilometrosProximoServicio = 15000,
        notificado = false,
        activo = true
        )

        coEvery { repository.saveNotificacion(notificacion) } returns true

        val emissions = useCase(notificacion).toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success && emissions[1].data == true)
    }

    @Test
    fun `when save fails, emits Loading and Error`() = runBlocking {
        val notificacion = NotificacionModel(
        id = 1L,
        vehiculoPersonalId = 1L,
        reglaMantenimientoId = 1L,
        tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
        kilometrosUltimoServicio = 10000,
        kilometrosProximoServicio = 15000,
        notificado = false,
        activo = true
        )

        val errorMessage = "Error saving notification"
        coEvery { repository.saveNotificacion(notificacion) } throws Exception(errorMessage)

        val emissions = useCase(notificacion).toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error && emissions[1].message == errorMessage)
    }
 }