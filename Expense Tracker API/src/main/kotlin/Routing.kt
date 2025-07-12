package com.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
    data class Transaction(
        val id: Int, val description: String,
        val amount: Double, val type: String, val date: String, val categoryId: Int
    )

    @Serializable
    data class Category(val id: Int, val name: String)

@Serializable
data class CategorySummary(
    val category: String,
    val total: Double
)

@Serializable
data class YearlySummary(
    val year: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
)

@Serializable
data class YearlyCategorySummary(
    val year: Int,
    val byCategory: List<CategorySummary>
)

object ReportService {
    fun generateYearlySummary(year: Int): YearlySummary {
        val transactions = Data.transactions.filter {
            val txDate = LocalDateTime.parse(it.date + "T00:00:00")
            txDate.year == year
        }
        val totalIncome = transactions.filter { it.type == "income" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "expense" }.sumOf { it.amount }
        val balance = totalIncome - totalExpense
        return YearlySummary(year, totalIncome, totalExpense, balance)
    }

    fun generateYearlyCategoryBreakdown(year: Int): YearlyCategorySummary {
        val categoryMap = Data.categories.associateBy { it.id }
        val expenses = Data.transactions.filter {
            val txDate = LocalDateTime.parse(it.date + "T00:00:00")
            txDate.year == year && it.type == "expense"
        }
        val byCategory = expenses
            .groupBy { it.categoryId }
            .mapNotNull { (catId, txns) ->
                categoryMap[catId]?.name?.let { CategorySummary(it, txns.sumOf { it.amount }) }
            }
        return YearlyCategorySummary(year, byCategory)
    }
}


object Data {
        val categories = mutableListOf(
            Category(id = 1, name = "อาหาร"),
            Category(id = 2, name = "เดินทาง"),
            Category(id = 3, name = "รายได้"),
        )

        val transactions = mutableListOf(
            Transaction(
                id = 1,
                description = "ข้าวมันไก่",
                amount = 50.0,
                type = "expense",
                date = "2024-12-01",
                categoryId = 1
            ),
            Transaction(
                id = 2,
                description = "ค่า BTS",
                amount = 30.0,
                type = "expense",
                date = "2024-12-01",
                categoryId = 2
            ),
            Transaction(
                id = 3,
                description = "เงินเดือน",
                amount = 30000.0,
                type = "income",
                date = "2024-12-01",
                categoryId = 3
            ),
            Transaction(
                id = 4,
                description = "ก๋วยเตี๋ยว",
                amount = 45.0,
                type = "expense",
                date = "2024-12-02",
                categoryId = 1
            ),
        )
    }

fun Application.configureRouting() {
    routing {
        route("/transactions") {
            get {
                call.respond(Data.transactions)
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val transaction = Data.transactions.find { it.id == id }
                if (transaction == null) {
                    call.respond(HttpStatusCode.NotFound, "Transaction not found")
                } else {
                    call.respond(transaction)
                }

            }
            post {
                val input = call.receive<Transaction>()
                val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val newTransaction = input.copy(
                    id = (Data.transactions.maxOfOrNull { it.id } ?: 0) + 1,
                    date = now
                )
                Data.transactions.add(newTransaction)
                call.respond(HttpStatusCode.Created, newTransaction)

            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val index = Data.transactions.indexOfFirst { it.id == id }
                if (index == -1) {
                    call.respond(HttpStatusCode.NotFound, "Transaction not found")
                    return@put
                }

                val updatedTransaction = call.receive<Transaction>().copy(id = id)
                Data.transactions[index] = updatedTransaction
                call.respond(updatedTransaction)

            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }

                val removed = Data.transactions.removeIf { it.id == id }
                if (removed) {
                    call.respond(HttpStatusCode.OK, "Transaction deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Transaction not found")
                }
            }

        }

        route("/categories") {
            get {
                call.respond(Data.categories)
            }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val category = Data.categories.find { it.id == id }
                if (category == null) {
                    call.respond(HttpStatusCode.NotFound, "Category not found")
                } else {
                    call.respond(category)
                }

            }
            post {
                val newCategory = call.receive<Category>().copy(id = (Data.categories.maxOfOrNull { it.id } ?: 0) + 1)
                Data.categories.add(newCategory)
                call.respond(HttpStatusCode.Created, newCategory)

            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val index = Data.categories.indexOfFirst { it.id == id }
                if (index == -1) {
                    call.respond(HttpStatusCode.NotFound, "Category not found")
                    return@put
                }

                val updatedCategory = call.receive<Category>().copy(id = id)
                Data.categories[index] = updatedCategory
                call.respond(updatedCategory)

            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }

                val removed = Data.categories.removeIf { it.id == id }
                if (removed) {
                    call.respond(HttpStatusCode.OK, "Category deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Category not found")
                }
            }

        }
        route("/reports") {
                get("/summary") {
                    val year = call.request.queryParameters["year"]?.toIntOrNull() ?: 2024
                    val report = ReportService.generateYearlySummary(year)
                    call.respond(report)
                }

                get("/by-category") {
                    val year = call.request.queryParameters["year"]?.toIntOrNull() ?: 2024
                    val report = ReportService.generateYearlyCategoryBreakdown(year)
                    call.respond(report)
                }
            }
        }
    }
