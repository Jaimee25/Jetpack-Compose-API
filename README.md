# Documentación del Proyecto TrabajoFinal

## 1. Introducción

Este proyecto es una aplicación Android desarrollada en **Kotlin** con **Jetpack Compose** y otras herramientas modernas.  
Incluye autenticación de usuarios con **Firebase** y gestión de datos con **Firestore**.

## 2. Actividades Principales

### 2.1 MainActivity

La `MainActivity` es la pantalla principal de la aplicación. Su código maneja:

- La navegación dentro de la app.
- El manejo de notificaciones periódicas mediante `Runnable`.
- Acciones principales relacionadas con la API y los datos de usuario.

```kotlin
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

// Código completo en MainActivity.kt
```

### 2.2 LogInActivity

La `LogInActivity` gestiona el inicio de sesión del usuario. Su código maneja:

- La autenticación con Firebase.

- La validación de credenciales y la redirección a `MainActivity` tras un login exitoso.

- El registro de nuevos usuarios.

```kotlin
package com.example.trabajofinal.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Código completo en LogInActivity.kt
```

## 3. API y Consumo de Datos

### 3.1 Configuración de Retrofit

La aplicación usa Retrofit para consumir datos desde una API externa. Se encuentra configurado en `ApiService.kt`.

```kotlin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.example.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

### 3.2 Llamadas a la API

Se realizan llamadas a la API usando coroutines en `Repository.kt`.

```kotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    suspend fun getData(): List<DataModel> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getData()
        }
    }
}
```

## 4. Configuración y Ejecución

Para compilar y ejecutar el proyecto:

- Abre el proyecto en Android Studio.

- Asegúrate de que tienes las dependencias necesarias en build.gradle.kts.

- Configura Firebase en la aplicación siguiendo la documentación oficial.

- Verifica la conectividad con la API.

- Ejecuta la aplicación en un emulador o dispositivo físico.

##

Aqui inserto el enlace de [GitHub](https://github.com/Jaimee25/Jetpack-Compose-API) con el proyecto subido en un repositorio

Autor: Jaime López
