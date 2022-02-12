package es.joseluisgs.models

import java.time.LocalDateTime

data class Employee(
    var id: Int,
    val name: String,
    val email: String,
    val city: String,
    val image: String,
    val createdAt: String = LocalDateTime.now().toString()
)