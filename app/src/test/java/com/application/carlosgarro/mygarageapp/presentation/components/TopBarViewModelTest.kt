package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.data.local.LocalDataSource
import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.Awaits
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TopBarViewModelTest {

 @get:Rule
 val instantExecutorRule = InstantTaskExecutorRule()

 private lateinit var viewModel: TopBarViewModel

 @RelaxedMockK
 private lateinit var firebaseAuth: FirebaseAuth

 @RelaxedMockK
 private lateinit var firebaseUser: FirebaseUser

 @RelaxedMockK
 private lateinit var firestoreService: FirestoreService

 @RelaxedMockK
 private lateinit var localDataSource: LocalDataSource

 @RelaxedMockK
 private lateinit var notificacionDAO: NotificacionDAO

 @RelaxedMockK
 private lateinit var vehiculoPersonalDAO: VehiculoPersonalDAO

 @RelaxedMockK
 private lateinit var  mantenimientoDAO: MantenimientoDAO


 private val testDispatcher = StandardTestDispatcher()

 @Before
 fun setup() {
  MockKAnnotations.init(this)
  Dispatchers.setMain(testDispatcher)

  every { localDataSource.notificacionDAO() } returns notificacionDAO
  every { localDataSource.vehiculoPersonalDAO() } returns vehiculoPersonalDAO
  every { localDataSource.mantenimientoDAO() } returns mantenimientoDAO
  every { firebaseAuth.currentUser } returns firebaseUser
  every { firebaseAuth.currentUser?.email } returns "test@example.com"
  every { firebaseUser.email } returns "test@example.com"
  coEvery { firebaseUser.delete() } just Awaits
  every { firebaseUser.uid } returns "userId"
  every { firebaseAuth.currentUser?.uid } returns "userId"

  viewModel = TopBarViewModel(firebaseAuth, localDataSource, firestoreService)
 }


 @After
 fun tearDown() {
  Dispatchers.resetMain()
 }

 @Test
 fun `signOut should call FirebaseAuth signOut`() {
  viewModel.signOut()
  verify { firebaseAuth.signOut() }
 }

 @Test
 fun `deleteUser should delete user data from Firestore and local DB`() = runTest {
  viewModel.deleteUser()
  advanceUntilIdle()

  verify { firestoreService.eliminarDatosUsuario("userId") }
  coVerify { notificacionDAO.deleteNotificacionesByUser("test@example.com") }
  coVerify { vehiculoPersonalDAO.deleteVehiculoPersonalByUser("test@example.com") }
  coVerify { mantenimientoDAO.deleteMantenimientosByUser("test@example.com") }
  coVerify { firebaseAuth.currentUser!!.delete() }
 }

 @Test
 fun `deleteUser does nothing when currentUser is null`() = runTest {
  every { firebaseAuth.currentUser } returns null

  viewModel.deleteUser()
  advanceUntilIdle()

  verify(exactly = 0) { firestoreService.eliminarDatosUsuario(any()) }
  coVerify(exactly = 0) { notificacionDAO.deleteNotificacionesByUser(any()) }
  coVerify(exactly = 0) { vehiculoPersonalDAO.deleteVehiculoPersonalByUser(any()) }
  coVerify(exactly = 0) { mantenimientoDAO.deleteMantenimientosByUser(any()) }
  verify(exactly = 0) { firebaseUser.delete() }
 }
}
