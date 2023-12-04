package com.kmp.ktor.model

class InMemoryTaskRepository : TaskRepository {
    private val tasks = mutableListOf(
        Task("Cleaning", "Clean the house", Priority.Low),
        Task("Gardening", "Mow the lawn", Priority.Medium),
        Task("Shopping", "Buy the groceries", Priority.High),
        Task("Painting", "Paint the fence", Priority.Medium)
    )


    override fun allTasks(): List<Task> = tasks


    override fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }


    override fun taskByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }


    override fun addTask(task: Task) {
        tasks.removeIf {
            it.name == task.name
        }
        tasks.add(task)
    }


    override fun removeTask(name: String): Boolean {
        return tasks.removeIf { it.name == name }
    }
}
