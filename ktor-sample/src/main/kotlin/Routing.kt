package com.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Task(val id: Int, val content: String, val isDone: Boolean)

@Serializable
data class TaskRequest(val content: String, val isDone: Boolean)

object TaskRepository {
    private val tasks = mutableListOf(
        Task(1, "Learn Ktor", true),
        Task(2, "Build a REST API", false),
        Task(3, "Write Unit Tests", false)
    )

    fun getAll(): List<Task> = tasks

    fun getById(id: Int): Task? = tasks.find { it.id == id }

    fun add(taskRequest: TaskRequest): Task {
        val id = (tasks.maxOfOrNull { it.id } ?: 0) + 1
        val task = Task(id, taskRequest.content, taskRequest.isDone)
        tasks.add(task)
        return task
    }

    fun update(id: Int, updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        return if (index != -1) {
            tasks[index] = updatedTask
            true
        } else {
            false
        }
    }

    fun delete(id: Int): Boolean = tasks.removeIf { it.id == id }
}


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello ธนัญอร ชวณิชย์")
        }

        get("/tasks") {
            call.respond(TaskRepository.getAll())
        }

        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@get
            }
            val task = TaskRepository.getById(id)
            if (task == null) {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            } else {
                call.respond(HttpStatusCode.OK, task)
            }
        }

        post("/tasks") {
            val taskRequest = call.receive<TaskRequest>()
            val task = TaskRepository.add(taskRequest)
            call.respond(HttpStatusCode.Created, task)
        }

        put("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@put
            }
            val taskRequest = call.receive<TaskRequest>()
            val updatedTask = Task(id, taskRequest.content, taskRequest.isDone)
            val isUpdated = TaskRepository.update(id, updatedTask)
            if (isUpdated) {
                call.respond(HttpStatusCode.OK, updatedTask)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }

        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                return@delete
            }
            val isDeleted = TaskRepository.delete(id)
            if (isDeleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Task not found")
            }
        }
    }
}

