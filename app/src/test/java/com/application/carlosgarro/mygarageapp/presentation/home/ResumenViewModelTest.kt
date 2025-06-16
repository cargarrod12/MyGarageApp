package com.application.carlosgarro.mygarageapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoRepository
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ResumenViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private lateinit var resumenViewModel: ResumenViewModel

 @RelaxedMockK
 private lateinit var saveVehiculoPersonal: saveVehiculoPersonalUseCase

 @RelaxedMockK
 private lateinit var getVehiculoPersonalByUser: getVehiculoPersonalByUserUseCase

 @RelaxedMockK
 private lateinit var firebaseAuth: FirebaseAuth

 @RelaxedMockK
 private lateinit var vehiculoRepository: VehiculoRepository

 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setup() {
  MockKAnnotations.init(this)
  Dispatchers.setMain(testDispatcher)
  resumenViewModel = ResumenViewModel(
   saveVehiculoPersonal,
   getVehiculoPersonalByUser,
   firebaseAuth,
   vehiculoRepository
  )
 }


 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `cargaVehiculos should update listaVehiculoPersonal when successful`() = runTest {
  val email = "test@example.com"
  val vehiculosMock = listOf(VehiculoPersonalModel(usuarioEmail = email))

  every { firebaseAuth.currentUser?.email } returns email
  coEvery { getVehiculoPersonalByUser(email) } returns flowOf(Resource.Success(vehiculosMock))

  resumenViewModel.cargaVechiculos()

  testDispatcher.scheduler.advanceUntilIdle()

  assertEquals(vehiculosMock, resumenViewModel.listaVehiculoPersonal.value)
  assertFalse(resumenViewModel.isLoading.value!!)
 }

 @Test
 fun `cargaVehiculos should set empty list on error`() = runTest {
  val email = "test@example.com"

  every { firebaseAuth.currentUser?.email } returns email
  coEvery { getVehiculoPersonalByUser(email) } returns flowOf(Resource.Error("Error al obtener vehículos"))

  resumenViewModel.cargaVechiculos()
  testDispatcher.scheduler.advanceUntilIdle()
  assertTrue(resumenViewModel.listaVehiculoPersonal.value!!.isEmpty())
  assertFalse(resumenViewModel.isLoading.value!!)
 }

 @Test
 fun `addVehiculoPersonal should call saveVehiculoPersonal and refresh data`() = runTest {
//  val email = "test@example.com"
  val vehiculoMock = VehiculoPersonalModel(id = 2L, usuarioEmail = "test@example.com")

  coEvery { firebaseAuth.currentUser?.email } returns "test@example.com"
  coEvery { saveVehiculoPersonal(any()) } returns flowOf(Resource.Success(true))
  coEvery { getVehiculoPersonalByUser("test@example.com") } returns flowOf(
   Resource.Success(
    listOf(
     vehiculoMock
    )
   )
  )

  resumenViewModel.addVehiculoPersonal(vehiculoMock)
  testDispatcher.scheduler.advanceUntilIdle()
  coVerify { saveVehiculoPersonal(vehiculoMock.copy(usuarioEmail = "test@example.com")) }
  coVerify { getVehiculoPersonalByUser("test@example.com") }
  assertEquals(listOf(vehiculoMock), resumenViewModel.listaVehiculoPersonal.value)
 }

}
