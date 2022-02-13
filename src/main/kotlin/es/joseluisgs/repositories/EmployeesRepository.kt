package es.joseluisgs.repositories

import es.joseluisgs.entities.Employees
import es.joseluisgs.models.Employee
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object EmployeesRepository : CrudRepository<Employee, Int> {

    fun isEmpty() = transaction {
        Employees.all().empty()
    }

    override fun getAll(limit: Int): List<Employee> = transaction {
        val response = if (limit == 0) Employees.all() else Employees.all().limit(limit)
        return@transaction response.map { it.toEmployee() }
    }

    override fun getById(id: Int): Employee? = transaction {
        Employees.findById(id)?.toEmployee()
    }

    override fun update(id: Int, entity: Employee): Boolean = transaction {
        val employee = Employees.findById(id) ?: return@transaction false
        employee.apply {
            name = entity.name
            email = entity.email
            image = entity.image
        }
        entity.id = id
        return@transaction true
    }

    override fun save(entity: Employee) = transaction {
        entity.id = Employees.new {
            name = entity.name
            email = entity.email
            city = entity.city
            image = entity.image
            createdAt = LocalDateTime.parse(entity.createdAt)
        }.id.value
    }

    override fun delete(id: Int): Boolean = transaction {
        val employee = Employees.findById(id) ?: return@transaction false
        employee.delete()
        return@transaction true
    }
}