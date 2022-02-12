package es.joseluisgs.controllers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import es.joseluisgs.entities.EmployeesTable
import es.joseluisgs.models.Employee
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseManager {
    private val logger = KotlinLogging.logger {}

    fun init(
        jdbcUrl: String,
        driverClassName: String,
        username: String,
        password: String,
        maximumPoolSize: Int = 10,
        initDatabaseData: Boolean = true
    ) {
        // Aplicamos Hiraki para la conexión a la base de datos
        println("Inicializando conexión a la base de datos")
        val config = HikariConfig()
        config.apply {
            this.jdbcUrl = jdbcUrl
            this.driverClassName = driverClassName
            this.username = username
            this.password = password
            this.maximumPoolSize = maximumPoolSize
        }
        Database.connect(HikariDataSource(config))
        logger.info { "Conexión a la base de datos inicializada" }

        // Otras funciones a realizar

        // Creamos las tablas
        createTables()

        // iniciamos los datos
        if (initDatabaseData) {
            initData()
        }
    }

    /**
     * Función para crear las tablas
     */
    private fun createTables() = transaction {
        addLogger(StdOutSqlLogger) // Para que se vea el log de consultas a la base de datos
        SchemaUtils.create(EmployeesTable)
        logger.info { "Tablas creadas" }
    }

    // *** Esto es opcional porque quiero tener unos datos iniciales en la base de datos ***

    /**
     * Función para inicializar los datos
     */
    private fun initData() {
        // Insertamos los datos.
        // Mira el directorio data
        initDataEmployees()
        logger.info { "Datos creados" }
    }

    private fun initDataEmployees() = transaction {
        val employees = listOf<Employee>(
            Employee(
                1,
                "Pepe Perez",
                "pepe@perez.com",
                "Leganes",
                "https://api.lorem.space/image/face?w=150&h=150"
            ),
            Employee(
                2,
                "Ana Lopez",
                "ana@lopez.es",
                "Madrid",
                "https://api.lorem.space/image/face?w=150&h=150"
            )
        )
        logger.info { "Employees de ejemplo insertados" }
    }
}