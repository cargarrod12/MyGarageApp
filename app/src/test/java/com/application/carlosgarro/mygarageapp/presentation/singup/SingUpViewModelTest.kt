//import android.content.Context
//import android.widget.Toast
//import com.application.carlosgarro.mygarageapp.presentation.singup.SingUpViewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthUserCollisionException
//import io.mockk.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.*
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(RobolectricTestRunner::class)
//class SingUpViewModelTest {
//
// private lateinit var viewModel: SingUpViewModel
// private val testDispatcher = StandardTestDispatcher()
//
// private val context = mockk<Context>(relaxed = true)
// private val auth = mockk<FirebaseAuth>(relaxed = true)
//
// @Before
// fun setup() {
//  Dispatchers.setMain(testDispatcher)
//  viewModel = SingUpViewModel(mockk(relaxed = true))
//  mockkStatic(Toast::class)
//  val mockToast = mockk<Toast>(relaxed = true)
//  every { Toast.makeText(any(), any<String>(), any()) } returns mockToast
//  every { mockToast.show() } returns Unit
// }
//
// @After
// fun tearDown() {
//  Dispatchers.resetMain()
//  unmockkAll()
// }
//
//            @Test
//            fun signUp_showsToastOnEmailCollision() = runTest {
//             mockkStatic(Toast::class)  // MOCK estático de Toast
//
//             val email = "test@example.com"
//             val password = "password123"
//             val navigateToHome = mockk<() -> Unit>(relaxed = true)
//
//             val exception = FirebaseAuthUserCollisionException("error", "email exists")
//             val task = mockk<com.google.android.gms.tasks.Task<*>>(relaxed = true)
//             every { task.isSuccessful } returns false
//             every { task.exception } returns exception
//
//             val toastMock = mockk<Toast>(relaxed = true)
//             every { Toast.makeText(any(), any(), Toast.LENGTH_LONG) } returns toastMock
//             every { toastMock.show() } returns Unit
//
//             every {
//              auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(any())
//             } answers {
//              val listener = arg<(com.google.android.gms.tasks.Task<*>) -> Unit>(0)
//              listener(task)  // Invoca el callback con task que tiene excepción
//              auth.createUserWithEmailAndPassword(email, password)
//             }
//
//             viewModel.sigUp(email, password, context, navigateToHome, auth)
//
//             testDispatcher.scheduler.advanceUntilIdle()  // Procesa corrutinas
//
//             verify(exactly = 1) {
//              Toast.makeText(context, "El correo ya está en uso", Toast.LENGTH_LONG)
//              toastMock.show()
//             }
//
//             assertEquals(false, viewModel.isLoading.value)
//
//             unmockkStatic(Toast::class)  // Limpieza de mocks estáticos
//            }
//
//}
