package com.application.carlosgarro.mygarageapp.presentation.initial


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.carlosgarro.mygarageapp.R
import com.application.carlosgarro.mygarageapp.ui.theme.BackgroundButton
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import com.application.carlosgarro.mygarageapp.ui.theme.Gray
import com.application.carlosgarro.mygarageapp.ui.theme.ShapeButton

@Preview
@Composable
fun InitialScreen(navigateToLogin: () -> Unit = {}, navigateToSignUp: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Blue, Color.White))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground_icon),
            contentDescription = "Logo",
            modifier = Modifier.size(350.dp)
        )
        Text(
            text = "Gestiona tus Revisiones",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )
        Text(
            text = "My Garage App",
            color = Color.White,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            )
        )
        Spacer(modifier = Modifier.weight(3f))
        Button(
            onClick = {navigateToSignUp()},
            modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .background(Color.Transparent)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            content = {
                Text(
                    text = "Registrarse",
                    color = Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

            },
            )
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Iniciar sesión",
            color = Blue,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { navigateToLogin() },
            textDecoration = TextDecoration.Underline,
            style = TextStyle(
                shadow = Shadow(
                    color = Gray,
                    offset = Offset(2f, 2f),
                )
            )
        )
        Spacer(modifier = Modifier.weight(10f))

    }

}

@Composable
fun CustomButton(modifier: Modifier, painter: Painter, title: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
            .clip(CircleShape)
            .background(BackgroundButton)
            .border(2.dp, ShapeButton, CircleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .padding(start = 16.dp)
                .size(16.dp)
        )
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
