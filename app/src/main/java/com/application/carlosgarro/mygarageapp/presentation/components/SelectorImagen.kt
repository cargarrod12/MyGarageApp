package com.application.carlosgarro.mygarageapp.presentation.components


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun SelectorImagen(
    imagenSeleccionada: ByteArray?,
    onSeleccionarImagen: () -> Unit
) {
    val borderColor = if (imagenSeleccionada == null) Color.Gray else  Color.Transparent
    val backgroundColor = if (imagenSeleccionada == null) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSeleccionarImagen() }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (imagenSeleccionada != null) {
            val bitmap = BitmapFactory.decodeByteArray(imagenSeleccionada, 0, imagenSeleccionada.size)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
                Text("Seleccionar imagen", color = Color.Gray)
            }
        }
    }
}