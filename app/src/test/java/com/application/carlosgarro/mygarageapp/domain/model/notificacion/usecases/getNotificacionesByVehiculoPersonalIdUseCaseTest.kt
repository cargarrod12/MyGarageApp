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


class getNotificacionesByVehiculoPersonalIdUseCaseTest{


    @RelaxedMockK
    private lateinit var repository: NotificacionRepository

    private lateinit var useCase: getNotificacionesByVehiculoPersonalIdUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = getNotificacionesByVehiculoPersonalIdUseCase(repository)
    }

    @Test
    fun `when data is fetched successfully, emits Loading and Success with list of NotificacionModel`() = runBlocking {
        val vehiculoPersonalId = 1L
        val notificaciones = listOf(
        NotificacionModel(
            id = 1L,
            vehiculoPersonalId = vehiculoPersonalId,
            reglaMantenimientoId = 1L,
            tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
            kilometrosUltimoServicio = 10000,
            kilometrosProximoServicio = 15000,
            notificado = false,
            activo = true
        )
        )

        coEvery { repository.getNotificacionesByVehiculoPersonalId(vehiculoPersonalId) } returns notificaciones

        val emissions = useCase(vehiculoPersonalId).toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Success && emissions[1].data == notificaciones)
    }

    @Test
    fun `when repository throws exception, emits Loading and Error`() = runBlocking {
        val vehiculoPersonalId = 1L
        val exceptionMessage = "Error fetching data"

        coEvery { repository.getNotificacionesByVehiculoPersonalId(vehiculoPersonalId) } throws Exception(exceptionMessage)

        val emissions = useCase(vehiculoPersonalId).toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        assertTrue(emissions[1] is Resource.Error && emissions[1].message == exceptionMessage)
    }

 }

