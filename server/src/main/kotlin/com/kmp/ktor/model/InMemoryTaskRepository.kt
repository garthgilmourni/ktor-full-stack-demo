package com.kmp.ktor.model

class InMemoryTaskRepository : TaskRepository {
    private var tasks = listOf(
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
        var notFound = true

        tasks = tasks.map {
            if(it.name == task.name) {
                notFound = false
                task
            } else {
                it
            }
        }
        if(notFound) {
            tasks = tasks.plus(task)
        }
    }


    override fun removeTask(name: String): Boolean {
        val oldTasks = tasks
        tasks = tasks.filterNot { it.name == name }
        return oldTasks.size > tasks.size
    }
}
