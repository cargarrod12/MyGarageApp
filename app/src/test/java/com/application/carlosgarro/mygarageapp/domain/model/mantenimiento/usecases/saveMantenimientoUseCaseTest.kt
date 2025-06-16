package com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.usecases


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


class saveMantenimientoUseCaseTest{

    @RelaxedMockK
    private lateinit var repository: MantenimientoRepository

    private lateinit var useCase: saveMantenimientoUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = saveMantenimientoUseCase(repository)
    }

  @Test
  fun `when save is successful, emits Loading and Success with true`() = runBlocking {
   val mantenimiento = MantenimientoModel()

   coEvery { repository.saveMantenimiento(mantenimiento) } returns true

   val result = useCase(mantenimiento).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(true, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when save fails, emits Loading and Success with false`() = runBlocking {
   val mantenimiento = MantenimientoModel()

   coEvery { repository.saveMantenimiento(mantenimiento) } returns false

   val result = useCase(mantenimiento).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Success)
   assertEquals(false, (result[1] as Resource.Success).data)
  }

  @Test
  fun `when repository throws exception, emits Loading and Error`() = runBlocking {
   val mantenimiento = MantenimientoModel()

   coEvery { repository.saveMantenimiento(mantenimiento) } throws RuntimeException("Falló el guardado")

   val result = useCase(mantenimiento).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Falló el guardado", (result[1] as Resource.Error).message)
  }

  @Test
  fun `when exception has null message, emits Unknown Error`() = runBlocking {
   val mantenimiento = MantenimientoModel()

   coEvery { repository.saveMantenimiento(mantenimiento) } throws RuntimeException(null as String?)

   val result = useCase(mantenimiento).toList()

   assertTrue(result[0] is Resource.Loading)
   assertTrue(result[1] is Resource.Error)
   assertEquals("Unknown Error", (result[1] as Resource.Error).message)
  }

 }