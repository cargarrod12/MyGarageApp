package com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases


import com.application.carlosgarro.mygarageapp.domain.model.Resource
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

class DeleteVehiculoPersonalUseCaseTest {

 @RelaxedMockK
 private lateinit var repository: VehiculoPersonalRepository

 private lateinit var useCase: deleteVehiculoPersonalUseCase

 @Before
 fun setup() {
  MockKAnnotations.init(this)
  useCase = deleteVehiculoPersonalUseCase(repository)
 }

 @Test
 fun `when delete is successful, emits Loading and Success`() = runBlocking {
  val vehiculoId = 1L

  coEvery { repository.deleteVehiculoPersonal(vehiculoId) } returns true

  val result = useCase(vehiculoId).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Success)
  assertEquals(true, (result[1] as Resource.Success).data)
 }

 @Test
 fun `when delete returns false, emits Loading and Success with false`() = runBlocking {
  val vehiculoId = 2L

  coEvery { repository.deleteVehiculoPersonal(vehiculoId) } returns false

  val result = useCase(vehiculoId).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Success)
  assertEquals(false, (result[1] as Resource.Success).data)
 }

 @Test
 fun `when delete throws exception, emits Loading and Error`() = runBlocking {
  val vehiculoId = 3L

  coEvery { repository.deleteVehiculoPersonal(vehiculoId) } throws RuntimeException("Error al eliminar")

  val result = useCase(vehiculoId).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Error al eliminar", (result[1] as Resource.Error).message)
 }

 @Test
 fun `when exception has null message, emits Unknown Error`() = runBlocking {
  val vehiculoId = 4L

  coEvery { repository.deleteVehiculoPersonal(vehiculoId) } throws RuntimeException(null as String?)

  val result = useCase(vehiculoId).toList()

  assertTrue(result[0] is Resource.Loading)
  assertTrue(result[1] is Resource.Error)
  assertEquals("Unknown Error", (result[1] as Resource.Error).message)
 }
}
