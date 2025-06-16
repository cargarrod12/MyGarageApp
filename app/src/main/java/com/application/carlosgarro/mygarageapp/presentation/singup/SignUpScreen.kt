package com.application.carlosgarro.mygarageapp.presentation.singup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.R
import com.application.carlosgarro.mygarageapp.ui.theme.Black
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import com.application.carlosgarro.mygarageapp.ui.theme.SelectedField
import com.application.carlosgarro.mygarageapp.ui.theme.ShapeButton
import com.application.carlosgarro.mygarageapp.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    navigateToHome: () -> Unit = {},
    viewModel: SingUpViewModel= hiltViewModel()
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepetir by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.observeAsState(false)

    var context = LocalContext.current

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = Color.Blue
        )
    }else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Blue, White)))
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(240.dp)
                )
            }

            Text(
                "Email", color = White, fontWeight = FontWeight.Bold, fontSize = 30.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black, // Color de la sombra
                        offset = Offset(4f, 4f), // Desplazamiento de la sombra (X, Y)
                        blurRadius = 8f // Radio de desenfoque
                    )
                )
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField
                )
            )
            Spacer(Modifier.height(30.dp))
            Text(
                "Contraseña",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black, // Color de la sombra
                        offset = Offset(4f, 4f), // Desplazamiento de la sombra (X, Y)
                        blurRadius = 8f // Radio de desenfoque
                    )
                )
            )
            TextField(
                value = password, onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )
            Spacer(Modifier.height(30.dp))
            Text(
                "Repetir Contraseña",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black, // Color de la sombra
                        offset = Offset(4f, 4f), // Desplazamiento de la sombra (X, Y)
                        blurRadius = 8f // Radio de desenfoque
                    )
                )
            )
            TextField(
                value = passwordRepetir, onValueChange = { passwordRepetir = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = UnselectedField,
                    focusedContainerColor = SelectedField
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                }
            )
            var aceptarTerminos by remember { mutableStateOf(false) }
            var mostrarDialogo by remember { mutableStateOf(false) }

            Column() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = aceptarTerminos, onCheckedChange = { aceptarTerminos = it })

                    Text(
                        text = "Acepto los términos y condiciones",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { mostrarDialogo = true },
                    )
                }

                TerminosCondicionesDialog(showDialog = mostrarDialogo) {
                    mostrarDialogo = false
                }

            }
                var valido = password == passwordRepetir && password.isNotEmpty() && email.isNotEmpty() && aceptarTerminos

                if (password != passwordRepetir && password.isNotEmpty() && passwordRepetir.isNotEmpty() && valido) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    border = BorderStroke(2.dp, ShapeButton),
                    colors = ButtonDefaults.buttonColors(White),
                    enabled = valido,
                    onClick = {
                        viewModel.sigUp(email, password, context, { navigateToHome() }, auth)
                    }) {
                    Text(
                        color = Black,
                        text = "Registrarse",
                    )
                }
        }
    }
}
        @Composable
        fun TerminosCondicionesDialog(showDialog: Boolean, onDismiss: () -> Unit) {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text("Términos y Condiciones") },
                    text = {
                        Box(modifier = Modifier.height(300.dp)) { // Define una altura fija
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                Text("""
                        Aviso Importante sobre las Revisiones de Vehículos
                        
                        Nuestra aplicación te ofrece una herramienta de seguimiento y estimación para la gestión de las revisiones de tu vehículo. A través de búsquedas de información intentamos proporcionar recordatorios y sugerencias sobre el mantenimiento que podrías necesitar.

                        Sin embargo, es fundamental tener en cuenta que todas las recomendaciones ofrecidas son estimaciones y no sustituyen la evaluación de un profesional cualificado.

                        Cada vehículo tiene sus propias condiciones de uso, desgaste y necesidades específicas que solo un experto en mecánica puede diagnosticar con precisión. Por lo tanto, te recomendamos que siempre consultes con un especialista certificado antes de tomar decisiones relacionadas con el mantenimiento y seguridad de tu automóvil.

                        Nuestra aplicación está diseñada para ser una ayuda, no una guía definitiva. Úsala como referencia, pero confía en el criterio de profesionales para garantizar el mejor cuidado de tu vehículo.
                        
                        Política de Protección de Datos

                        La privacidad y seguridad de tus datos son fundamentales para nosotros. En nuestra aplicación, nos comprometemos a gestionar tu información de manera responsable y transparente.

                        1. Recopilación de Datos Solo recopilamos los datos estrictamente necesarios para el funcionamiento de la aplicación, como información de usuario y detalles sobre el mantenimiento de tu vehículo.

                        2. Uso de la Información Los datos proporcionados se utilizan exclusivamente para mejorar la experiencia del usuario y optimizar las recomendaciones sobre la gestión de revisiones vehiculares. No compartimos, vendemos ni cedemos tu información a terceros sin tu consentimiento.

                        3. Seguridad Implementamos medidas de seguridad para proteger tu información contra accesos no autorizados, pérdidas o alteraciones. Sin embargo, recuerda que ningún sistema es completamente invulnerable y te recomendamos mantener buenas prácticas de seguridad digital.

                        4. Derechos del Usuario Tienes derecho a acceder, modificar o eliminar tus datos en cualquier momento. Si deseas gestionar tu información personal, puedes hacerlo a través de la configuración de la aplicación o contactándonos.

                        5. Contacto Para cualquier duda sobre la protección de tus datos, puedes comunicarte con nosotros a través de mygarageapp@gmail.com.

                        Nota: Al utilizar nuestra aplicación, aceptas nuestra política de privacidad y el uso de tus datos según lo descrito aquí. Te recomendamos leer estos términos y condiciones regularmente, ya que pueden actualizarse para cumplir con normativas vigentes.
                    """.trimIndent())
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = onDismiss) {
                            Text("Cerrar")
                        }
                    }
                )
            }
        }


