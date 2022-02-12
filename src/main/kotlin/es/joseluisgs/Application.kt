package es.joseluisgs

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val presentacion = environment.config.propertyOrNull("mensajes.presentacion")?.getString() ?: "Hola Kotlin"
    val mode = environment.config.property("ktor.environment").getString()

    // Comenzamos a registrar las rutas
    initRoutes(presentacion, mode)
    
}

/**
 * Rutas principales del Servicio
 */
private fun Application.initRoutes(presentacion: String, mode: String) {
    routing {
        // Entrada en la api
        get("/test") {
            call.respondText("$presentacion en modo $mode")
        }
    }
}
