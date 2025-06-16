package com.application.carlosgarro.mygarageapp.presentation.mapa

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.application.carlosgarro.mygarageapp.data.external.maps.response.ReviewsResponse


@Composable
fun InfoTaller(
    placeId: String,
    nombre: String,
    direccion: String,
    abierto: Boolean,
    rating: Double?,
    fotoUrl: String?,
    reviews: List<ReviewsResponse>,
    modifier: Modifier = Modifier,
    context: Context
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 400.dp)
            .padding(8.dp)
    ) {
        item {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    if (fotoUrl != null) {
                        AsyncImage(
                            model = fotoUrl,
                            contentDescription = "Foto del taller",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin imagen",
                                color = Color.DarkGray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = nombre, style = MaterialTheme.typography.titleMedium)
                        Text(text = direccion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (rating != null) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                                Text(text = " $rating", style = MaterialTheme.typography.bodyMedium)
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = if (abierto) "Abierto ahora" else "Cerrado",
                                color = if (abierto) Color(0xFF4CAF50) else Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.BottomEnd,
                            ) {
                                Text(
                                    text = "Dejar una reseña",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://search.google.com/local/writereview?placeid=$placeId"))
                                            context.startActivity(intent)
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (reviews.isEmpty()) {
                Text(
                    text = "No hay opiniones disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                return@item
            }
            Text(
                text = "Opiniones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        items(reviews) { review ->
            ReviewItem(review)
        }
    }
}


@Composable
fun ReviewItem(review: ReviewsResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = review.authorName?:"", style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(review.rating ?: 0) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                review.time?.let { Text(text = it, style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            review.text?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
        }
    }
}