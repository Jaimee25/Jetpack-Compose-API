package com.example.trabajofinal.registro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabajofinal.parteApi.PersonajesScreenActivity

class WelcomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lastLogin = intent.getStringExtra("lastLogin") ?: "Nunca"
        val loginCount = intent.getIntExtra("loginCount", 0)

        setContent {
            WelcomeScreen(lastLogin, loginCount)
        }

        // Redirigir después de 30 segundos
        android.os.Handler().postDelayed({
            startActivity(Intent(this, PersonajesScreenActivity::class.java))
            finish()
        }, 10000) // 30 segundos
    }

    @Composable
    fun WelcomeScreen(lastLogin: String, loginCount: Int) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido de nuevo!",
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Último acceso: $lastLogin",
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Número de veces que has iniciado sesión: $loginCount",
                style = MaterialTheme.typography.body1
            )
        }
    }
}