package com.kmp.ktor.model

import com.kmp.ktor.model.Priority.*

class InMemoryTaskRepository : TaskRepository {
    private var tasks = listOf(
        Task("Cleaning", "Clean the house", Low),
        Task("Gardening", "Mow the lawn", Medium),
        Task("Shopping", "Buy the groceries", High),
        Task("Painting", "Paint the fence", Low),
        Task("Cooking", "Cook the dinner", Medium),
        Task("Relaxing", "Take a walk", High),
        Task("Exercising", "Go to the gym", Low),
        Task("Learning", "Read a book", Medium),
        Task("Snoozing", "Go for a nap", High),
        Task("Socializing", "Go to a party", High)
    )


    override fun allTasks(): List<Task> = tasks


    override fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }


    override fun taskByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }


    override fun addOrUpdateTask(task: Task) {
        var notFound = true

        tasks = tasks.map {
            if (it.name == task.name) {
                notFound = false
                task
            } else {
                it
            }
        }
        if (notFound) {
            tasks = tasks.plus(task)
        }
    }


    override fun removeTask(name: String): Boolean {
        val oldTasks = tasks
        tasks = tasks.filterNot { it.name == name }
        return oldTasks.size > tasks.size
    }
}
