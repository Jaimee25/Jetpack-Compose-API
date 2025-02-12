package com.example.trabajofinal.registro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.trabajofinal.parteApi.PersonajesScreenActivity
import com.example.trabajofinal.parteApi.showNotification
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)

        setContent {
            LoginScreen()
        }
        // Verifica si la versión de Android requiere permisos adicionales

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso de notificaciones está concedido
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Solicita el permiso si no está concedido
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    // Manejo del resultado de la solicitud de permisos

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showNotification(this, "Título", "Contenido")
            } else {
                // Si el permiso es denegado, muestra un mensaje de error
                Toast.makeText(applicationContext, "No se pudo conceder el acceso", Toast.LENGTH_SHORT).show()
            }
        }
    }



    @Composable
    fun LoginScreen() {
        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {
                                val userDocRef = db.collection("users").document(user.uid)

                                userDocRef.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        // Obtener valores actuales
                                        val loginCount = document.getLong("loginCount") ?: 0
                                        val lastLoginTimestamp = document.getTimestamp("lastLogin")

                                        // Convertir Timestamp a formato legible
                                        val lastLoginDate = lastLoginTimestamp?.toDate()?.let {
                                            java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).format(it)
                                        } ?: "Nunca"

                                        // Incrementar el contador de inicios de sesión
                                        val newLoginCount = loginCount + 1

                                        // Obtener la fecha actual
                                        val currentTimestamp = Timestamp.now()

                                        // Actualizar Firestore
                                        userDocRef.update(
                                            mapOf(
                                                "loginCount" to newLoginCount,
                                                "lastLogin" to currentTimestamp
                                            )
                                        ).addOnSuccessListener {
                                            // Redirigir a la pantalla de bienvenida
                                            val intent = Intent(context, WelcomeScreenActivity::class.java).apply {
                                                putExtra("lastLogin", lastLoginDate)
                                                putExtra("loginCount", newLoginCount)
                                            }
                                            context.startActivity(intent)
                                        }.addOnFailureListener {
                                            Toast.makeText(context, "Error al actualizar datos: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Usuario no encontrado: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Error al obtener datos: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Credenciales no encontradas: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                context.startActivity(Intent(context, LogInActivity::class.java))
            }) {
                Text("Registrarse")
            }
        }
    }
}