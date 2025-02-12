package com.example.trabajofinal.parteApi

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trabajofinal.R
import com.example.trabajofinal.registro.MainActivity

// Función para mostrar una notificación en la aplicación
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun showNotification(context: Context, title: String, content: String) {
    val channelId = "ic_notification"

    // Verifica si la versión de Android requiere canales de notificación
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Dragon Ball Notifications"
        val descriptionText = "Notifications for Dragon Ball searches"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        // Obtiene el servicio de notificación y crea el canal
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Construcción de la notificación
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Comprobar si el permiso de notificación está concedido
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    } else {
        // Si no tiene permisos, los solicita al usuario (solo si el contexto es MainActivity)
        if (context is MainActivity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }
}