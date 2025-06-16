package com.application.carlosgarro.mygarageapp.data.local.repository

import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.toModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VehiculoRepositoryImplTest{

  @RelaxedMockK
  private lateinit var dao: VehiculoDAO
    private lateinit var repository: VehiculoRepositoryImpl

  @Before
  fun onBefore() {
   MockKAnnotations.init(this)
    repository = VehiculoRepositoryImpl(dao)
  }


 @Test
 fun `getAllVehiculos returns mapped list when DAO returns data`() = runBlocking {
  val daoEntities = listOf(
    VehiculoEntity(
     id = 1L,
     marca = MarcaVehiculo.HONDA,
     modelo = ModeloVehiculo.CIVIC,
    ),
    VehiculoEntity(
     id = 2L,
     marca = MarcaVehiculo.TOYOTA,
     modelo = ModeloVehiculo.COROLLA)
  )
  val expectedModels = daoEntities.map { it.toModel() }

  coEvery { dao.getAllVehiculos() } returns daoEntities

  val result = repository.getAllVehiculos()

  assertEquals(expectedModels, result)
 }

 @Test
 fun `getAllVehiculos returns empty list when DAO throws exception`() = runBlocking {
  coEvery { dao.getAllVehiculos() } throws RuntimeException("Error DAO")

  val result = repository.getAllVehiculos()

  assertTrue(result.isEmpty())
 }


 }