package com.example.trabajofinal.parteApi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trabajofinal.api.Personaje
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.trabajofinal.registro.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.exp

@AndroidEntryPoint
class PersonajesScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonajesScreen()
        }
    }

    @SuppressLint("NotConstructor")
    @Composable
    fun PersonajesScreen(
        viewModel: PersonajesViewModel = hiltViewModel()
    ) {
        val state by viewModel.state.collectAsState()
        Log.d("Cant. items", "${state.size}")
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f) //Le damos peso para que no ocupe_todo el espacio
            ) {
                items(state) { personaje ->
                    PersonajeCard(personaje)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón para salir de la aplicación
                Button(
                    onClick = {
                        finish() // Salir de la aplicación
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Salir")
                }

                Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los botones

                // Botón para ir a MainActivity
                Button(
                    onClick = {
                        val intent = Intent(this@PersonajesScreenActivity, MainActivity::class.java)
                        startActivity(intent) // Navegar a MainActivity
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "MainActivity")
                }
            }
        }
    }

    @Composable
    fun PersonajeCard(
        personaje: Personaje,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember {
            mutableStateOf(false)
        }
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
                .fillMaxWidth()
                .padding(26.dp)
            //.fillMaxSize(),
        ) {
            Column {
                Row {
                    Surface(
                        modifier.size(130.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    ) {
                        AsyncImage(
                            model = personaje.image,
                            contentDescription = personaje.name,
                            contentScale = ContentScale.FillBounds

                            )
                    }
                    Column(
                        modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    ) {
                        Text(text = personaje.name,
                            style = MaterialTheme.typography.titleLarge
                            )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val color = when(personaje.affiliation){
                                "Z Fighter" -> Color.Green
                                "Army of Frieza" -> Color.Red
                                else -> Color.Blue
                            }
                            Box(
                                modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(12.dp)
                            )
                            Text(
                                text = "${personaje.affiliation} - ${personaje.gender}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                    IconButton(onClick = {
                                expanded = !expanded
                    },
                        modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else
                                Icons.Filled.KeyboardArrowDown ,
                            contentDescription = "Mas Informacion"
                        )
                    }
                }
                if (expanded){
                    Row(
                        modifier.padding(16.dp)
                    ) {
                        Column {
                            Text(text = "Raza de Origen",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(text = personaje.race,
                                style = MaterialTheme.typography.bodyLarge
                                )
                            Text(text = personaje.description,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}