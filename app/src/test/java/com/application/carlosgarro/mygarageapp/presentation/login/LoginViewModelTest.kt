package com.application.carlosgarro.mygarageapp.presentation.login

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.data.local.dao.UsuarioDAO
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

 @get:Rule
 val instantTaskExecutorRule = InstantTaskExecutorRule()

 private lateinit var loginViewModel: LoginViewModel
 @RelaxedMockK
 private lateinit var usuarioDAO: UsuarioDAO
 @RelaxedMockK
 private lateinit var firestoreService: FirestoreService
 @MockK
 private lateinit var firebaseAuth: FirebaseAuth
 @RelaxedMockK
 private lateinit var firebaseUser: FirebaseUser
 @RelaxedMockK
 private lateinit var context: Context
 private val navigateToHome: () -> Unit = mockk(relaxed = true)

 private val testDispatcher = StandardTestDispatcher()

 @get:Rule
 val instantExecutorRule = InstantTaskExecutorRule()

 @Before
 fun setup() {
    MockKAnnotations.init(this)
  loginViewModel = LoginViewModel(usuarioDAO, firestoreService)
  Dispatchers.setMain(testDispatcher) // Use Unconfined for testing
 }

 @After
    fun tearDown() {
    Dispatchers.resetMain() // Reset the main dispatcher after tests
    }

 @Test
 fun `login with valid credentials should insert user and navigate to home`() = runTest {
  val email = "test@example.com"
  val password = "password123"
  val userId = "mockUserId"

  // Mock FirebaseUser
  every { firebaseUser.uid } returns userId

  // Mock Task<AuthResult>
  val authResult: AuthResult = mockk(relaxed = true)
  every { authResult.user } returns firebaseUser

  val mockTask = mockk<com.google.android.gms.tasks.Task<AuthResult>>(relaxed = true)

  every { mockTask.isSuccessful } returns true
  every { mockTask.result } returns authResult

  every { mockTask.addOnCompleteListener(any()) } answers {
   firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>()
    .onComplete(mockTask)
   mockTask
  }

  every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns mockTask

  coEvery { usuarioDAO.getUsuarioByEmail(email) } returns null
  coEvery { firestoreService.synDatosUsuario(userId, email) } just Runs

  loginViewModel.login(firebaseAuth, email, password, context, navigateToHome)

  testDispatcher.scheduler.advanceUntilIdle()

  coVerify { usuarioDAO.getUsuarioByEmail(email) }
  coVerify { firestoreService.synDatosUsuario(userId, email) }
  verify { navigateToHome.invoke() }
 }



// @Test
// fun `login with invalid credentials should show error`() = runTest {
//  val email = "wrong@example.com"
//  val password = "wrongPassword"
//
//  val exception = FirebaseAuthInvalidCredentialsException("ERROR", "Invalid credentials")
//
//  val mockTask = mockk<com.google.android.gms.tasks.Task<AuthResult>>(relaxed = true)
//  every { mockTask.isSuccessful } returns false
//  every { mockTask.exception } returns exception
//
//  every { mockTask.addOnCompleteListener(any()) } answers {
//   firstArg<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>()
//    .onComplete(mockTask)
//   mockTask
//  }
//
//  every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns mockTask
//
//  loginViewModel.login(firebaseAuth, email, password, context, navigateToHome)
//
//  testDispatcher.scheduler.runCurrent()
//
//  assertEquals(false, loginViewModel.isLoading.value)
// }

}
