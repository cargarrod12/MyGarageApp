package com.application.carlosgarro.mygarageapp.presentation.historial

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun FormularioNuevaEntrada(
    mantenimiento: MantenimientoModel,
    onChange: () -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var servicio by remember { mutableStateOf(mantenimiento.tipoServicio) }

        var kilometros by remember { mutableStateOf(mantenimiento.kilometrosServicio.toString()) }

        var precio by remember { mutableStateOf(mantenimiento.precio.toString()) }

        Text("Agregar Nueva Entrada", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))


        DropdownSelector("Servicio", TipoServicio.entries, servicio) {
            servicio = it
            mantenimiento.tipoServicio = it
            onChange()
        }


        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = kilometros,
            onValueChange = { it ->
                val filtered = it.filter { it.isDigit() }
                kilometros = (filtered.toIntOrNull() ?: 0).toString()
                mantenimiento.kilometrosServicio = filtered.toIntOrNull() ?: 0
                onChange()
            },
            label = { Text("Kilómetros") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormularioConFecha(
            onFechaChange = {
                mantenimiento.fechaServicio = it
                onChange()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = {
                precio = it
                mantenimiento.precio = it.toDoubleOrNull() ?: 0.0
                onChange()
            },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row {
            Button(
                onClick = {
                    onGuardar()
                    mantenimiento.kilometrosServicio = 0
                    mantenimiento.fechaServicio = LocalDate.now()
                    mantenimiento.tipoServicio = TipoServicio.OTRO
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
            ) {
                Text("Guardar")
            }
        }
    }
}


@Composable
fun <T> DropdownSelector(label: String, options: List<T>, selected: T, onSelected: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected.toString(),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown, contentDescription = null,
                    Modifier.clickable { expanded = true })
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun FormularioConFecha(onFechaChange: (LocalDate) -> Unit = {}) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val calendar = Calendar.getInstance()

    var fechaSeleccionada by remember { mutableStateOf("") }

    // Crear el DatePickerDialog con una restricción de fecha máxima
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                fechaSeleccionada = selectedDate.format(dateFormatter)
                onFechaChange(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Establece el límite máximo como hoy
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Selecciona una fecha")

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
        ) {
            OutlinedTextField(
                value = fechaSeleccionada,
                onValueChange = {},
                label = { Text("Fecha") },
                readOnly = true,
                enabled = false, // evita que el teclado se abra
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
