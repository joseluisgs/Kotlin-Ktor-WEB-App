package es.joseluisgs.routes

import es.joseluisgs.entities.UserSession
import es.joseluisgs.models.Employee
import es.joseluisgs.models.Notification
import es.joseluisgs.repositories.EmployeesRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

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
            lateinit var userSession: UserSession
            try {
                userSession = call.sessions.get<UserSession>()!!
                call.sessions.set(userSession.copy(count = userSession.count + 1))
            } catch (e: Exception) {
                call.sessions.set(UserSession(id = "123abc", count = 1))
                userSession = call.sessions.get<UserSession>()!!
            }

            val employees = EmployeesRepository.getAll()
            val exists = employees.isNotEmpty()
            val data = mapOf(
                "pageTitle" to "Empleados",
                "employees" to employees,
                "exists" to exists,
                "count" to userSession.count
            )
            call.respond(MustacheContent("index.hbs", data))
        }
        get("/login") {
            call.sessions.set(UserSession(id = "123abc", count = 1))
            call.respondRedirect("/")
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/")
        }
    }
    route("/delete") {
        get {
            // Obtenemos el id
            val id = call.request.queryParameters["id"]
            if (id != null) {
                EmployeesRepository.delete(id.toInt())
                val employees = EmployeesRepository.getAll()
                val exists = employees.isNotEmpty()
                val notification = Notification("is-danger", "Se ha eliminado correctamente el empleado con ID: $id")
                val data = mapOf(
                    "pageTitle" to "Employees",
                    "employees" to employees,
                    "exists" to exists,
                    "notification" to notification
                )
                call.respondTemplate("index.hbs", data)
                // Si queremos forzar el cambio de URL
                // call.respondRedirect("/")
            }
        }
    }

    route("/employee") {
        // Si me llaman con get
        get {
            val action = (call.request.queryParameters["action"] ?: "new")
            when (action) {
                "new" -> {
                    val data = mapOf(
                        "pageTitle" to "Nuevo",
                        "action" to action
                    )
                    call.respond(MustacheContent("employee.hbs", data))
                }
                "edit" -> {
                    val id = call.request.queryParameters["id"]
                    if (id != null) {
                        val employee = EmployeesRepository.getById(id.toInt())
                        val data = mapOf(
                            "pageTitle" to "Editar ",
                            "employee" to employee,
                            "action" to action
                        )
                        call.respond(MustacheContent("employee.hbs", data))
                    }
                }
            }
        }
        // sai me llegan los datos del formulario
        post {
            val postParameters: Parameters = call.receiveParameters()
            val action = postParameters["action"] ?: "new"
            when (action) {
                "new" -> {
                    val employee = Employee(
                        postParameters["name"] ?: "",
                        postParameters["email"] ?: "",
                        postParameters["city"] ?: ""
                    )
                    EmployeesRepository.save(employee)
                    val employees = EmployeesRepository.getAll()
                    val exists = employees.isNotEmpty()
                    val notification =
                        Notification("is-success", "Se ha guardado correctamente el empleado ${employee.name}")
                    val data = mapOf(
                        "pageTitle" to "Employees",
                        "notification" to notification,
                        "employees" to employees,
                        "exists" to exists
                    )
                    // Si queremos forzar el cambio de URL
                    //call.respondRedirect("/")
                    call.respondTemplate("index.hbs", data)
                }
                "edit" -> {
                    val id = postParameters["id"]
                    println("id: $id")
                    if (id != null) {
                        val employee = Employee(
                            id.toInt(),
                            postParameters["name"] ?: "",
                            postParameters["email"] ?: "",
                            postParameters["city"] ?: ""
                        )
                        EmployeesRepository.update(id.toInt(), employee)
                        val employees = EmployeesRepository.getAll()
                        val exists = employees.isNotEmpty()
                        val notification =
                            Notification("is-warning", "Se ha actualizado correctamente el empleado ${employee.name}")
                        val data = mapOf(
                            "pageTitle" to "Employees",
                            "notification" to notification,
                            "employees" to employees,
                            "exists" to exists
                        )
                        call.respondTemplate("index.hbs", data)
                        // Si queremos forzar el cambio de URL
                        // call.respondRedirect("/")
                    }
                }
            }
        }
    }

}