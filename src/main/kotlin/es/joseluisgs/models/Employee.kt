package es.joseluisgs.models

import java.time.LocalDateTime

data class Employee(
    var id: Int,
    val name: String,
    val email: String,
    val city: String,
    val image: String,
    val createdAt: String = LocalDateTime.now().toString()
) {
    constructor(name: String, email: String, city: String) : this(
        0,
        name,
        email,
        city,
        "https://api.lorem.space/image/face?w=150&h=150"
    )

    constructor(id: Int, name: String, email: String, city: String) : this(
        id,
        name,
        email,
        city,
        "https://api.lorem.space/image/face?w=150&h=150"
    )
}