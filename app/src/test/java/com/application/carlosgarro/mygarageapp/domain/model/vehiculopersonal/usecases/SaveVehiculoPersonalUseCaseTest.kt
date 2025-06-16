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


class SaveVehiculoPersonalUseCaseTest {

       @RelaxedMockK
       private lateinit var repository: VehiculoPersonalRepository

       private lateinit var useCase: saveVehiculoPersonalUseCase

       @Before
       fun setup() {
        MockKAnnotations.init(this)
        useCase = saveVehiculoPersonalUseCase(repository)
       }

       @Test
       fun `when save is successful, emits Loading and Success`() = runBlocking {
        // Crea el modelo correctamente, sin anidarlo erróneamente
        val mockVehiculo = VehiculoPersonalModel(
         modelo = VehiculoModel(
          id = 1L,
          marca = MarcaVehiculo.HONDA,
          modelo = ModeloVehiculo.CIVIC
         ),
         anyo = 2020,
         usuarioEmail = "prueba@prueba.com"
        )

        coEvery { repository.saveVehiculoPersonal(mockVehiculo) } returns true

        val result = useCase(mockVehiculo).toList()
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
        assertEquals(true, (result[1] as Resource.Success).data)
       }

    @Test
    fun `when save fails, emits Loading and Error with message`() = runBlocking {
        val mockVehiculo = VehiculoPersonalModel(
            modelo = VehiculoModel(
                id = 1L,
                marca = MarcaVehiculo.HONDA,
                modelo = ModeloVehiculo.CIVIC
            ),
            anyo = 2020,
            usuarioEmail = "prueba@prueba.com"
        )

        // Simula un error al guardar
        coEvery { repository.saveVehiculoPersonal(mockVehiculo) } throws RuntimeException("Unknown Error")

        val result = useCase(mockVehiculo).toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Unknown Error", (result[1] as Resource.Error).message)
    }

    @Test
    fun `when save fails, emits Loading and Error with null`() = runBlocking {
        val mockVehiculo = VehiculoPersonalModel(
            modelo = VehiculoModel(
                id = 1L,
                marca = MarcaVehiculo.HONDA,
                modelo = ModeloVehiculo.CIVIC
            ),
            anyo = 2020,
            usuarioEmail = "prueba@prueba.com"
        )

        // Simula un error al guardar
        coEvery { repository.saveVehiculoPersonal(mockVehiculo) } throws RuntimeException(null as String?)

        val result = useCase(mockVehiculo).toList()

        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals("Unknown Error", (result[1] as Resource.Error).message)
    }
}

