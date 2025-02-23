import kotlinx.serialization.Serializable


@Serializable
object Initial
@Serializable
object Login
@Serializable
object SignUp

@Serializable
data class Home(val name: String)