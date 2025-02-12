package com.example.trabajofinal.api

data class ListaDragonBall(
    val items: List<Personaje>,
    val links: Links,
    val meta: Meta
)