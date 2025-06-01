import kotlinx.serialization.Serializable


@Serializable
object Initial
@Serializable
object Login
@Serializable
object SignUp

@Serializable
data class Home(val name: String)

@Serializable
data class Vehiculo(val id: Long)

@Serializable
data class Historial(val id: Long, val vehiculo: String)


@Serializable
data class Notificacion(val id: Long, val vehiculo: String)

@Serializable
object Mapa


@Serializable
data class EditarVehiculo(val id: Long)