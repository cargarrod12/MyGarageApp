package com.application.carlosgarro.mygarageapp.presentation.home

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.domain.model.vehiculo.VehiculoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.SelectorImagen
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import com.application.carlosgarro.mygarageapp.ui.theme.Red


@Composable
fun FormularioNuevoCoche(
    viewModel: ResumenViewModel,
    vehiculo: VehiculoPersonalModel,
    listaVehiculos: List<VehiculoModel>,
    imagenUri: Uri?,
    onChange: () -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setImagenDesdeUri(context, it) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agregar Nuevo Vehiculo", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        var modelo by remember { mutableStateOf(vehiculo.modelo) }
        var anyo by remember { mutableStateOf(vehiculo.anyo.toString()) }
        var kilometros by remember { mutableStateOf(vehiculo.kilometros.toString()) }
        var estado by remember { mutableStateOf(vehiculo.estado) }

//        Button(
//            onClick = { launcher.launch("image/*") },
//            colors = ButtonDefaults.buttonColors(containerColor = Blue),
//        ) {
//            Text("Seleccionar Imagen")
//        }
//
//        imagenUri?.let { Uri ->
//            Image(
//                painter = rememberAsyncImagePainter(Uri),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(200.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
//        }
        SelectorImagen(
            imagenSeleccionada = viewModel.imagenSeleccionada.value,
            onSeleccionarImagen = { launcher.launch("image/*") }
        )


        Log.i("FormularioNuevoCoche", "$listaVehiculos")
        VehiculoModelDropdownMenu(
            vehiculos = listaVehiculos,
            onVehiculoSelected = {
                modelo = it
                vehiculo.modelo = it }
        )


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = anyo,
            onValueChange = {
                anyo = it
                vehiculo.anyo = it.toIntOrNull() ?: 0
                onChange()
            },
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = kilometros,
            onValueChange = {
                kilometros = it
                vehiculo.kilometros = it.toIntOrNull() ?: 0
                onChange()
            },
            label = { Text("Kilómetros") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownSelector("Estado", EstadoVehiculo.values().toList(), estado) {
            estado = it
            vehiculo.estado = it
            onChange()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    onGuardar()
                    vehiculo.anyo = 0
                    vehiculo.kilometros = 0
                    vehiculo.estado = EstadoVehiculo.NUEVO
                },
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
            ) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onCancelar()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Red)
            ) {
                Text("Cancelar")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoModelDropdownMenu(
    vehiculos: List<VehiculoModel>,
    onVehiculoSelected: (VehiculoModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedVehiculo by remember { mutableStateOf<VehiculoModel?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedVehiculo?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecciona Modelo") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),

            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            vehiculos.forEach { vehiculo ->
                DropdownMenuItem(
                    text = { Text(vehiculo.toString()) },
                    onClick = {
                        selectedVehiculo = vehiculo
                        onVehiculoSelected(vehiculo)
                        expanded = false
                    }
                )
            }
        }
    }
}

