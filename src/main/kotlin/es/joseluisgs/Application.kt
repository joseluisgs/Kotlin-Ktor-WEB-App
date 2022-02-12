package es.joseluisgs

import com.github.mustachejava.DefaultMustacheFactory
import es.joseluisgs.controllers.DataBaseManager
import es.joseluisgs.models.User
import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    val presentacion = environment.config.propertyOrNull("mensajes.presentacion")?.getString() ?: "Hola Kotlin"
    val mode = environment.config.property("ktor.environment").getString()

    // Base de datos
    initDataBase()

    // Iniciamos Mustache
    initMustache()

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
        get("/user") {
            val user = User(1, "Pepe", "Perez")
            val data = mapOf("user" to user, "pageTitle" to "User")
            call.respond(MustacheContent("user.hbs", data))
        }
    }
}

/**
 * Iniciamos la configuración de Base de Datos
 */
private fun Application.initDataBase() {
    DataBaseManager.init(
        environment.config.property("database.jdbcUrl").getString(),
        environment.config.property("database.driverClassName").getString(),
        environment.config.property("database.username").getString(),
        environment.config.property("database.password").getString(),
        environment.config.property("database.maximumPoolSize").getString().toInt()
    )
}

/**
 * Inicializamos el motor de plantillas
 */
private fun Application.initMustache() {
    install(Mustache) {
        // Indicamos donde está el directorio de plantillas
        mustacheFactory = DefaultMustacheFactory("templates")
    }
}