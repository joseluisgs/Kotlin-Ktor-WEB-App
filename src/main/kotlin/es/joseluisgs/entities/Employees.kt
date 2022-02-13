package es.joseluisgs.entities

import es.joseluisgs.models.Employee
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

// Tabla de Empleados
object EmployeesTable : IntIdTable() {
    val name = varchar("name", 100).index()
    val email = varchar("email", 50).index()
    val city = varchar("city", 50)
    val image = varchar("image", 100)
    val createdAt = datetime("created_at")
}

// Clase que mapea la tabla de Empleados
class Employees(id: EntityID<Int>) : IntEntity(id) {
    // Sobre qué tabla me estoy trabajando
    companion object : IntEntityClass<Employees>(EmployeesTable)

    var name by EmployeesTable.name
    var email by EmployeesTable.email
    var city by EmployeesTable.city
    var image by EmployeesTable.image
    var createdAt by EmployeesTable.createdAt

    fun toEmployee(): Employee {
        return Employee(
            id.value,
            name,
            email,
            city,
            image,
            createdAt.toString()
        )
    }
}