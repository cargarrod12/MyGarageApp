package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.data.local.dao.ReglaMantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity
import com.application.carlosgarro.mygarageapp.domain.model.reglaMantenimiento.toModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


class ReglaMantenimientoRepositoryImplTest {

 @RelaxedMockK
 private lateinit var dao: ReglaMantenimientoDAO

 private lateinit var repository: ReglaMantenimientoRepositoryImpl

 @Before
 fun onBefore() {
  MockKAnnotations.init(this)
  repository = ReglaMantenimientoRepositoryImpl(dao)
 }

 @Test
 fun `getReglaMantenimientoByVehiculoAndTipoServicio returns model when DAO returns data`() = runBlocking {
  val vehiculoPersonalId = 1L
  val tipoServicio = "CAMBIO_DE_ACEITE"

  val daoEntity = ReglaMantenimientoEntity()
  val expectedModel = daoEntity.toModel()

  coEvery {
   dao.getReglaMantenimientoByVehiculoPersonalAndTipoServicio(vehiculoPersonalId, tipoServicio)
  } returns daoEntity

  val result = repository.getReglaMantenimientoByVehiculoAndTipoServicio(vehiculoPersonalId, tipoServicio)

  assertEquals(expectedModel, result)
 }

 @Test
 fun `getReglaMantenimientoByVehiculoAndTipoServicio returns null when DAO returns null`() = runBlocking {
  val vehiculoPersonalId = 1L
  val tipoServicio = "CAMBIO_DE_ACEITE"

  coEvery {
   dao.getReglaMantenimientoByVehiculoPersonalAndTipoServicio(vehiculoPersonalId, tipoServicio)
  } returns null

  val result = repository.getReglaMantenimientoByVehiculoAndTipoServicio(vehiculoPersonalId, tipoServicio)

  assertNull(result)
 }

 @Test
 fun `getReglaMantenimientoByVehiculoAndTipoServicio returns null when DAO throws exception`() = runBlocking {
  val vehiculoPersonalId = 1L
  val tipoServicio = "CAMBIO_DE_ACEITE"

  coEvery {
   dao.getReglaMantenimientoByVehiculoPersonalAndTipoServicio(vehiculoPersonalId, tipoServicio)
  } throws RuntimeException("DB error")

  val result = repository.getReglaMantenimientoByVehiculoAndTipoServicio(vehiculoPersonalId, tipoServicio)

  assertNull(result)
 }
}
