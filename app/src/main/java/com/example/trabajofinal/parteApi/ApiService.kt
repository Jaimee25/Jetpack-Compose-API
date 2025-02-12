package com.example.trabajofinal.parteApi

import com.example.trabajofinal.api.ListaDragonBall
import retrofit2.http.GET


interface ApiService {
    @GET("characters")
    suspend fun getPersonajes(): ListaDragonBall
}