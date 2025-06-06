package com.application.carlosgarro.mygarageapp.presentation.singup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.carlosgarro.mygarageapp.R
import com.application.carlosgarro.mygarageapp.ui.theme.Black
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import com.application.carlosgarro.mygarageapp.ui.theme.SelectedField
import com.application.carlosgarro.mygarageapp.ui.theme.ShapeButton
import com.application.carlosgarro.mygarageapp.ui.theme.UnselectedField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@Composable
fun SignUpScreen(auth: FirebaseAuth, navigateToHome: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepetir by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var context = LocalContext.current

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
                modifier = Modifier.size(250.dp)
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

        var valido = password == passwordRepetir && password.isNotEmpty() && email.isNotEmpty()

        if (password != passwordRepetir && password.isNotEmpty() && passwordRepetir.isNotEmpty() && valido) {
            Text(
                text = "Las contraseñas no coinciden",
                color = Color.Red,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


        Spacer(Modifier.height(30.dp))
        Button(
            border = BorderStroke(2.dp, ShapeButton),
            colors = ButtonDefaults.buttonColors(White),
            enabled = valido,
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToHome()
                            Log.i("REGISTRO", "REGISTRO OK")
                        } else {
                            when (val exception = task.exception) {
                                is FirebaseAuthUserCollisionException -> {
                                    // El email ya está en uso
                                    Log.i("REGISTRO", "El correo ya está en uso")
                                    Toast.makeText(context, "El correo ya está en uso", Toast.LENGTH_LONG).show()
                                }
                                is FirebaseAuthWeakPasswordException -> {
                                    // La contraseña es muy débil
                                    Log.i("REGISTRO", "Contraseña débil")
                                    Toast.makeText(context, "La contraseña es demasiado débil", Toast.LENGTH_LONG).show()
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    // Email con formato inválido
                                    Log.i("REGISTRO", "Email inválido")
                                    Toast.makeText(context, "El email no es válido", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    // Otro error
                                    Log.i("REGISTRO", "Error desconocido: ${exception?.message}")
                                    Toast.makeText(context, "Error realizar el registro. Por favor, intntelo de nuevo más tarde", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
            }) {
            Text(
                color = Black,
                text = "Registrarse",
            )
        }
    }

}