package es.joseluisgs.routes

import io.ktor.application.*
import io.ktor.mustache.*
import io.ktor.response.*
import io.ktor.routing.*

// Aplicamos las rutas a la aplicacion, extendiendo su routing
fun Application.employeesRoutes() {
    routing {
        employeesRoutes()
    }
}

fun Route.employeesRoutes() {
    // Desde el raiz
    route("/") {
        // GET /rest/customers/
        get {
            val data = mapOf("pageTitle" to "User")
            call.respond(MustacheContent("index.hbs", data))
        }
    }
}