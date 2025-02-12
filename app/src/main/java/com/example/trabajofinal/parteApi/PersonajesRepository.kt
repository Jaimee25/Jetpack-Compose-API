package com.example.trabajofinal.parteApi

import com.example.trabajofinal.api.ListaDragonBall
import javax.inject.Inject

class PersonajesRepository @Inject constructor(
    private val personajesApi: ApiService
) {

    suspend fun getPersonajes() : ListaDragonBall{
        return personajesApi.getPersonajes()
    }
}