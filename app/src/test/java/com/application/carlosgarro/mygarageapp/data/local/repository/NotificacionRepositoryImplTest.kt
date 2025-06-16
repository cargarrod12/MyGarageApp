package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.toModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificacionRepositoryImplTest {

 @RelaxedMockK
 private lateinit var dao: NotificacionDAO

 private lateinit var repository: NotificacionRepositoryImpl

 @Before
 fun onBefore() {
  MockKAnnotations.init(this)
  repository = NotificacionRepositoryImpl(dao)
 }

 @Test
 fun `getNotificacionByVehiculoPersonalAndTipoServicio returns model when DAO returns data`() = runBlocking {
  val vehiculoId = 1L
  val tipoServicio = TipoServicio.CAMBIO_DE_ACEITE
  val daoEntity = NotificacionEntity()
  val expectedModel = daoEntity.toModel()

  coEvery { dao.getNotificacionesByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio) } returns daoEntity

  val result = repository.getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio)

  assertEquals(expectedModel, result)
 }

 @Test
 fun `getNotificacionByVehiculoPersonalAndTipoServicio returns null when DAO returns null`() = runBlocking {
  val vehiculoId = 1L
  val tipoServicio = TipoServicio.CAMBIO_DE_ACEITE

  coEvery { dao.getNotificacionesByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio) } returns null

  val result = repository.getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoId, tipoServicio)

  assertNull(result)
 }

 @Test
 fun `saveNotificacion returns true when DAO insert successful`() = runBlocking {
  val model = NotificacionModel(
   id = 1L,
   vehiculoPersonalId = 1L,
   reglaMantenimientoId = 1L,
   tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
   kilometrosUltimoServicio = 10000,
   kilometrosProximoServicio = 15000,
   notificado = false,
   activo = true
  )

  coEvery { dao.insert(any()) } returns 1L

  val result = repository.saveNotificacion(model)

  assertTrue(result)
  coVerify { dao.insert(model.toEntity()) }
 }

 @Test
 fun `saveNotificacion returns false when DAO insert fails`() = runBlocking {
  val model = NotificacionModel(
   id = 1L,
   vehiculoPersonalId = 1L,
   reglaMantenimientoId = 1L,
   tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
   kilometrosUltimoServicio = 10000,
   kilometrosProximoServicio = 15000,
   notificado = false,
   activo = true
  )

  coEvery { dao.insert(any()) } returns -1L

  val result = repository.saveNotificacion(model)

  assertFalse(result)
 }

 @Test
 fun `saveNotificacion returns false when DAO insert fails and exception`() = runBlocking {
  val model = NotificacionModel(
   id = 1L,
   vehiculoPersonalId = 1L,
   reglaMantenimientoId = 1L,
   tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
   kilometrosUltimoServicio = 10000,
   kilometrosProximoServicio = 15000,
   notificado = false,
   activo = true
  )

  coEvery { dao.insert(any()) } throws RuntimeException("DB error")

  val result = repository.saveNotificacion(model)

  assertFalse(result)
 }

 @Test
 fun `getNotificacionesByVehiculoPersonalId returns list mapped from DAO`() = runBlocking {
  val vehiculoId = 1L
  val daoEntities = listOf(
   NotificacionEntity(
    id = 1L,
    vehiculoPersonalId = 1L,
    reglaMantenimientoId = 1L,
    tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
    kilometrosUltimoServicio = 10000,
    kilometrosProximoServicio = 15000,
    notificado = false,
    activo = true
   ),
   NotificacionEntity(
    id = 2L,
    vehiculoPersonalId = 1L,
    reglaMantenimientoId = 2L,
    tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE,
    kilometrosUltimoServicio = 100000,
    kilometrosProximoServicio = 150000,
    notificado = false,
    activo = true
   )
  )
  val expectedModels = daoEntities.map { it.toModel() }

  coEvery { dao.getNotificacionesByVehiculoPersonalId(vehiculoId) } returns daoEntities

  val result = repository.getNotificacionesByVehiculoPersonalId(vehiculoId)

  assertEquals(expectedModels, result)
 }

 @Test
 fun `updateNotificaciones returns true when DAO update returns non-zero`() = runBlocking {
  val models = listOf(NotificacionModel(
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
    reglaMantenimientoId = 2L,
    tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE,
    kilometrosUltimoServicio = 100000,
    kilometrosProximoServicio = 150000,
    notificado = false,
    activo = true
   )
  )

  coEvery { dao.updateNotificaciones(any()) } returns 2

  val result = repository.updateNotificaciones(models)

  assertTrue(result)
  coVerify { dao.updateNotificaciones(models.map { it.toEntity() }) }
 }

 @Test
 fun `updateNotificaciones returns false when DAO update returns zero`() = runBlocking {
  val models = listOf(NotificacionModel(
   id = 1L,
   vehiculoPersonalId = 1L,
   reglaMantenimientoId = 1L,
   tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
   kilometrosUltimoServicio = 10000,
   kilometrosProximoServicio = 15000,
   notificado = false,
   activo = true
  ))

  coEvery { dao.updateNotificaciones(any()) } returns 0

  val result = repository.updateNotificaciones(models)

  assertFalse(result)
 }

 @Test
 fun `updateNotificaciones returns false when DAO update returns exception`() = runBlocking {
  val models = listOf(NotificacionModel(
   id = 1L,
   vehiculoPersonalId = 1L,
   reglaMantenimientoId = 1L,
   tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
   kilometrosUltimoServicio = 10000,
   kilometrosProximoServicio = 15000,
   notificado = false,
   activo = true
  ))

  coEvery { dao.updateNotificaciones(any()) } throws RuntimeException("DB error")

  val result = repository.updateNotificaciones(models)

  assertFalse(result)
 }

 @Test
 fun `getNotificacionesByUserToNotify returns mapped list from DAO`() = runBlocking {
  val email = "user@example.com"
  val vehiculoId = 1L
  val daoEntities = listOf(
   NotificacionEntity(
    id = 1L,
    vehiculoPersonalId = 1L,
    reglaMantenimientoId = 1L,
    tipoServicio = TipoServicio.CAMBIO_DE_ACEITE,
    kilometrosUltimoServicio = 10000,
    kilometrosProximoServicio = 15000,
    notificado = false,
    activo = true
   ),
   NotificacionEntity(
    id = 2L,
    vehiculoPersonalId = 1L,
    reglaMantenimientoId = 2L,
    tipoServicio = TipoServicio.CAMBIO_DE_FILTRO_DE_COMBUSTIBLE,
    kilometrosUltimoServicio = 100000,
    kilometrosProximoServicio = 150000,
    notificado = false,
    activo = true
   )
  )
  val expectedModels = daoEntities.map { it.toModel() }

  coEvery { dao.getNotificacionesByUserToNotify(email, vehiculoId) } returns daoEntities

  val result = repository.getNotificacionesByUserToNotify(email, vehiculoId)

  assertEquals(expectedModels, result)
 }

 @Test
 fun `getNotificacionesByUserToNotify returns empty list when DAO throws`() = runBlocking {
  val email = "user@example.com"
  val vehiculoId = 1L

  coEvery { dao.getNotificacionesByUserToNotify(email, vehiculoId) } throws RuntimeException("DB error")

  val result = repository.getNotificacionesByUserToNotify(email, vehiculoId)

  assertTrue(result.isEmpty())
 }
}
