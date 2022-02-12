package es.joseluisgs.routes

import es.joseluisgs.repositories.EmployeesRepository
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
            val employees = EmployeesRepository.getAll()
            val exists = employees.isNotEmpty()
            val data = mapOf(
                "pageTitle" to "Employees",
                "employees" to employees,
                "exists" to exists
            )
            call.respond(MustacheContent("index.hbs", data))
        }
    }
}