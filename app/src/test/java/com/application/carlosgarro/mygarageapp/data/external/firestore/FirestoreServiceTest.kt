//package com.application.carlosgarro.mygarageapp.data.external.firestore
//
//import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
//import com.application.carlosgarro.mygarageapp.data.local.dao.*
//import com.application.carlosgarro.mygarageapp.data.local.entity.*
//import com.application.carlosgarro.mygarageapp.data.local.relations.VehiculoPersonalCompleto
//import com.google.firebase.firestore.*
//import io.mockk.*
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.test.*
//import org.junit.*
//import org.junit.rules.TestWatcher
//import org.junit.runner.Description
//import org.junit.runner.RunWith
//import org.mockito.junit.MockitoJUnitRunner
//
//@ExperimentalCoroutinesApi
//@RunWith(MockitoJUnitRunner::class)
//class FirestoreServiceTest {
//
// @get:Rule
// val coroutineRule = MainDispatcherRule()
//
// private lateinit var firestore: FirebaseFirestore
// private lateinit var vehiculoPersonalDao: VehiculoPersonalDAO
// private lateinit var vehiculoDao: VehiculoDAO
// private lateinit var notificacionDao: NotificacionDAO
// private lateinit var mantenimientoDao: MantenimientoDAO
// private lateinit var reglaMantenimientoDAO: ReglaMantenimientoDAO
// private lateinit var firestoreService: FirestoreService
//
// @Before
// fun setup() {
//  firestore = mockk(relaxed = true)
//  vehiculoPersonalDao = mockk(relaxed = true)
//  vehiculoDao = mockk(relaxed = true)
//  notificacionDao = mockk(relaxed = true)
//  mantenimientoDao = mockk(relaxed = true)
//  reglaMantenimientoDAO = mockk(relaxed = true)
//
//  firestoreService = FirestoreService(
//   firestore,
//   vehiculoPersonalDao,
//   vehiculoDao,
//   notificacionDao,
//   mantenimientoDao,
//   reglaMantenimientoDAO
//  )
// }
//
// @Test
// fun `eliminarDatosUsuario elimina correctamente un documento`() {
//  val documentRef = mockk<DocumentReference>(relaxed = true)
//  every { firestore.collection("usuario").document("user123") } returns documentRef
//
//  firestoreService.eliminarDatosUsuario("user123")
//
//  verify { documentRef.delete() }
// }
//
// @Test
// fun `syncVehiculosPersonal sube datos si no hay en Firestore`() = runTest {
//  val userId = "user123"
//  val email = "test@example.com"
//
//  val vehiculo = VehiculoPersonalEntity(
//   id = 1,
//   fechaUltModificacion = "2023-10-01",
//  )
//
//  val firestoreVehiculos = mockk<CollectionReference>(relaxed = true)
//  val docSnap = mockk<QuerySnapshot>(relaxed = true)
//  val usuarioDoc = mockk<DocumentReference>(relaxed = true)
//
//  coEvery {
//   vehiculoPersonalDao.getVehiculosPersonalesByUsuario(email, listOf(1,0))
//  } returns listOf(
//   VehiculoPersonalCompleto(
//       vehiculoPersonal = vehiculo,
//       vehiculo = VehiculoEntity(),
//       usuario = UsuarioEntity("prueba", "prueba", clave = "prueba"),
//       mantenimientos = listOf(MantenimientoEntity()),
//       notificaciones = listOf(NotificacionEntity()),
//   )
//  )
//
//  every { firestore.collection("usuarios").document(userId) } returns usuarioDoc
//  every { usuarioDoc.collection("vehiculos") } returns firestoreVehiculos
//  coEvery { firestoreVehiculos.get().await() } returns docSnap
//  every { docSnap.isEmpty } returns true
//  coEvery { firestoreVehiculos.document(any()).set(any()).await() } just Awaits
//
//  firestoreService.synDatosUsuario(userId, email)
//
//  coVerify { firestoreVehiculos.document("1").set(any()) }
// }
//
// @Test
// fun `syncNotificaciones sube datos si no hay en Firestore`() = runTest {
//  val userId = "user123"
//  val email = "test@example.com"
//
//  val notificacion = NotificacionEntity().copy(fechaUltModificacion = "2024-10-01")
//  val notificacionesCol = mockk<CollectionReference>(relaxed = true)
//  val usuarioDoc = mockk<DocumentReference>(relaxed = true)
//  val querySnapshot = mockk<QuerySnapshot>(relaxed = true)
//
//  coEvery { notificacionDao.getNotificacionesByUser(email) } returns listOf(notificacion)
//  every { firestore.collection("usuarios").document(userId) } returns usuarioDoc
//  every { usuarioDoc.collection("notificaciones") } returns notificacionesCol
//  coEvery { notificacionesCol.get().await() } returns querySnapshot
//  every { querySnapshot.isEmpty } returns true
//  coEvery { notificacionesCol.document(any()).set(any()).await() } just Awaits
//
//  firestoreService.synDatosUsuario(userId, email)
//
//  coVerify { notificacionesCol.document("1").set(any()) }
// }
//
//}
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class MainDispatcherRule(
// private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
//) : TestWatcher() {
// override fun starting(description: Description?) {
//  Dispatchers.setMain(dispatcher)
// }
//
// override fun finished(description: Description?) {
//  Dispatchers.resetMain()
// }
//}
