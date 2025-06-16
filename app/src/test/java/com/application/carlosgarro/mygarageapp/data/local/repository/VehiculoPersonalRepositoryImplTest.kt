package com.application.carlosgarro.mygarageapp.data.local.repository



import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toEntity
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.toModel
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
import java.time.LocalDate


class VehiculoPersonalRepositoryImplTest {

 @RelaxedMockK
 private lateinit var dao: VehiculoPersonalDAO

 private lateinit var repository: VehiculoPersonalRepositoryImpl

 @Before
 fun onBefore() {
  MockKAnnotations.init(this)
  repository = VehiculoPersonalRepositoryImpl(dao)
 }

 @Test
 fun `saveVehiculoPersonal returns true when insert successful`() = runBlocking {
  val vehiculoModel = VehiculoPersonalModel()
  val entity = vehiculoModel.toEntity()

  coEvery { dao.insert(entity) } returns 1L

  val result = repository.saveVehiculoPersonal(vehiculoModel)

  assertTrue(result)
  coVerify { dao.insert(entity) }
 }

 @Test
 fun `saveVehiculoPersonal returns false when insert fails`() = runBlocking {
  val vehiculoModel = VehiculoPersonalModel()
  val entity = vehiculoModel.toEntity()

  coEvery { dao.insert(entity) } returns -1L

  val result = repository.saveVehiculoPersonal(vehiculoModel)

  assertFalse(result)
 }

 @Test
 fun `saveVehiculoPersonal returns false when exception thrown`() = runBlocking {
  val vehiculoModel = VehiculoPersonalModel()
  val entity = vehiculoModel.toEntity()

  coEvery { dao.insert(entity) } throws RuntimeException("DB error")

  val result = repository.saveVehiculoPersonal(vehiculoModel)

  assertFalse(result)
 }

 @Test
 fun `getVehiculosPersonalesByUsuario returns list when DAO returns data`() = runBlocking {
  val email = "test@example.com"
  val daoList = listOf(
   VehiculoPersonalCompleto(
       vehiculoPersonal = VehiculoPersonalEntity(),
       usuario = UsuarioEntity(
           email = "prueba@prueba.com",
           nombre = "Prueba",
           clave = "prueba123",
       ),
       vehiculo = VehiculoEntity(),
       mantenimientos = listOf(),
       notificaciones = listOf()
   ),
   VehiculoPersonalCompleto(
    vehiculoPersonal = VehiculoPersonalEntity(),
    usuario = UsuarioEntity(
     email = "prueba@prueba.com",
     nombre = "Prueba",
     clave = "prueba123",
    ),
    vehiculo = VehiculoEntity(),
    mantenimientos = listOf(),
    notificaciones = listOf()
   )
  )
  val expected = daoList.map { it.toModel() }

  coEvery { dao.getVehiculosPersonalesByUsuario(email, listOf(0)) } returns daoList

  val result = repository.getVehiculosPersonalesByUsuario(email)

  assertEquals(expected, result)
 }

 @Test
 fun `getVehiculosPersonalesByUsuario returns empty list when exception thrown`() = runBlocking {
  val email = "prueba@prueba.com"

  coEvery { dao.getVehiculosPersonalesByUsuario(email, listOf(0)) } throws RuntimeException("Error")

  val result = repository.getVehiculosPersonalesByUsuario(email)

  assertTrue(result.isEmpty())
 }

 @Test
 fun `getVehiculoPersonalById returns model when DAO returns data`() = runBlocking {
  val id = 1L
  val entity = VehiculoPersonalCompleto(
   vehiculoPersonal = VehiculoPersonalEntity(),
   usuario = UsuarioEntity(
    email = "prueba@prueba.com",
    nombre = "Prueba",
    clave = "prueba123",
   ),
   vehiculo = VehiculoEntity(),
   mantenimientos = listOf(),
   notificaciones = listOf()
  )
  val expected = entity.toModel()

  coEvery { dao.getVehiculoPersonalById(id) } returns entity

  val result = repository.getVehiculoPersonalById(id)

  assertEquals(expected, result)
 }

 @Test
 fun `getVehiculoPersonalById returns null when DAO returns null`() = runBlocking {
  val id = 1L

  coEvery { dao.getVehiculoPersonalById(id) } returns null

  val result = repository.getVehiculoPersonalById(id)

  assertNull(result)
 }

 @Test
 fun `getVehiculoPersonalById returns null when exception thrown`() = runBlocking {
  val id = 1L

  coEvery { dao.getVehiculoPersonalById(id) } throws RuntimeException("Error")

  val result = repository.getVehiculoPersonalById(id)

  assertNull(result)
 }

 @Test
 fun `deleteVehiculoPersonal returns true when delete successful`() = runBlocking {
  val vehiculoId = 10L
  val today = LocalDate.now().toString()

  coEvery { dao.deleteVehiculoPersonal(vehiculoId, today) } returns 1

  val result = repository.deleteVehiculoPersonal(vehiculoId)

  assertTrue(result)
 }

 @Test
 fun `deleteVehiculoPersonal returns false when delete returns 0`() = runBlocking {
  val vehiculoId = 10L
  val today = LocalDate.now().toString()

  coEvery { dao.deleteVehiculoPersonal(vehiculoId, today) } returns 0

  val result = repository.deleteVehiculoPersonal(vehiculoId)

  assertFalse(result)
 }

 @Test
 fun `deleteVehiculoPersonal returns false when exception thrown`() = runBlocking {
  val vehiculoId = 10L
  val today = LocalDate.now().toString()

  coEvery { dao.deleteVehiculoPersonal(vehiculoId, today) } throws RuntimeException("Error")

  val result = repository.deleteVehiculoPersonal(vehiculoId)

  assertFalse(result)
 }
}
