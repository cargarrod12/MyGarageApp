package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.toModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MantenimientoRepositoryImplTest {

 @RelaxedMockK
 private lateinit var dao: MantenimientoDAO

 private lateinit var repository: MantenimientoRepositoryImpl

 @Before
 fun onBefore() {
  MockKAnnotations.init(this)
  repository = MantenimientoRepositoryImpl(dao)
 }

 @Test
 fun `getMantenimientosByVehiculoPersonal returns mapped list when DAO returns data`() = runBlocking {
  val vehiculoId = 1L
  val daoEntities = listOf(
   MantenimientoEntity(),
   MantenimientoEntity()
  )
  val expectedModels = daoEntities.map { it.toModel() }

  coEvery { dao.getMantenimientosByVehiculoId(vehiculoId) } returns daoEntities

  val result = repository.getMantenimientosByVehiculoPersonal(vehiculoId)

  assertEquals(expectedModels, result)
 }

 @Test
 fun `getMantenimientosByVehiculoPersonal returns empty list when DAO throws exception`() = runBlocking {
  val vehiculoId = 1L

  coEvery { dao.getMantenimientosByVehiculoId(vehiculoId) } throws RuntimeException("DB error")

  val result = repository.getMantenimientosByVehiculoPersonal(vehiculoId)

  assertTrue(result.isEmpty())
 }

 @Test
 fun `saveMantenimiento returns true when DAO insert successful`() = runBlocking {
  val model = MantenimientoModel()

  coEvery { dao.insert(any()) } returns 1L

  val result = repository.saveMantenimiento(model)

  assertTrue(result)
  coVerify { dao.insert(model.toEntity()) }
 }

 @Test
 fun `saveMantenimiento returns false when DAO insert fails`() = runBlocking {
  val model = MantenimientoModel()

  coEvery { dao.insert(any()) } returns -1L

  val result = repository.saveMantenimiento(model)

  assertFalse(result)
 }

 @Test
 fun `saveMantenimiento returns false when DAO insert throws exception`() = runBlocking {
  val model = MantenimientoModel()

  coEvery { dao.insert(any()) } throws RuntimeException("DB error")

  val result = repository.saveMantenimiento(model)

  assertFalse(result)
 }
}
