package com.application.carlosgarro.mygarageapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel

@Composable
fun MisCochesTab(padding: PaddingValues, registros: List<VehiculoPersonalModel>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        // Tabla de datos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Vehiculo", modifier = Modifier.weight(1f))
            Text("Año", modifier = Modifier.weight(1f))
            Text("Kilometros", modifier = Modifier.weight(1f))
            Text("", modifier = Modifier.weight(1f))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(registros) { vehiculo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        vehiculo.modelo.marca.name + " " + vehiculo.modelo.modelo.name,
                        modifier = Modifier.weight(1f)
                    )
                    Text(vehiculo.anyo.toString(), modifier = Modifier.weight(1f))
                    Text("${vehiculo.kilometros} KM", modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Detalles",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Mostrar detalles */ }
                    )
                }
            }
        }
    }
}