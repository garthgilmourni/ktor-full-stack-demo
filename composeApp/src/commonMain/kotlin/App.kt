import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kmp.ktor.model.Priority
import com.kmp.ktor.model.Priority.*
import com.kmp.ktor.model.Task
import com.kmp.ktor.network.TaskApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun App() {
    App1()
}

@Composable
fun App0() {
    MaterialTheme {
        val client = remember { TaskApi() }
        val tasks = remember { mutableStateOf(emptyList<Task>()) }
        val scope = rememberCoroutineScope()


        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                scope.launch {
                    tasks.value = client.getAllTasks()
                }
            }) {
                Text("Fetch Tasks")
            }
            for (task in tasks.value) {
                Text(task.name)
            }
        }
    }
}

@Composable
fun App1() {
    MaterialTheme {
        val client = remember { TaskApi() }
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            tasks = client.getAllTasks()
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(tasks) { task ->
                TaskCard(
                    task,
                    onDelete = {
                        scope.launch {
                            client.removeTask(it)
                            tasks = client.getAllTasks()
                        }
                    },
                    onUpdate = {
                        // TODO: Add update functionality
                    }
                )
            }
        }
    }
}


//@Composable
//fun TaskCard1(
//    task: Task,
//    onDelete: (Task) -> Unit,
//    onUpdate: (Task) -> Unit
//) {
//    fun pickWeight(priority: Priority) = when (priority) {
//        Low -> FontWeight.SemiBold
//        Medium -> FontWeight.Bold
//        High, Vital -> FontWeight.ExtraBold
//    }
//
//    Card(
//        modifier = Modifier.fillMaxWidth().padding(4.dp),
//        shape = RoundedCornerShape(CornerSize(4.dp))
//    ) {
//        Column(modifier = Modifier.padding(10.dp)) {
//            Text(
//                "${task.name}: ${task.description}",
//                fontSize = 20.sp,
//                fontWeight = pickWeight(task.priority)
//            )
//            Row {
//                OutlinedButton(onClick = { onDelete(task) }) {
//                    Text("Delete")
//                }
//                Spacer(Modifier.width(8.dp))
//                OutlinedButton(onClick = { onUpdate(task) }) {
//                    Text("Update")
//                }
//            }
//        }
//    }
//}


@Composable
fun App2() {
    MaterialTheme {
        val client = remember { TaskApi() }
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        val scope = rememberCoroutineScope()
        var currentTask by remember { mutableStateOf<Task?>(null) }

        LaunchedEffect(Unit) {
            tasks = client.getAllTasks()
        }

        if (currentTask != null) {
            UpdateTaskDialog(
                currentTask!!,
                onConfirm = {
                    scope.launch {
                        client.updateTask(it)
                        tasks = client.getAllTasks()
                    }
                    currentTask = null
                }
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(tasks) { task ->
                TaskCard(
                    task,
                    onDelete = {
                        scope.launch {
                            client.removeTask(it)
                            tasks = client.getAllTasks()
                        }
                    },
                    onUpdate = {
                        currentTask = task
                    }
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onDelete: (Task) -> Unit,
    onUpdate: (Task) -> Unit
) {
    fun pickWeight(priority: Priority) = when (priority) {
        Low -> FontWeight.SemiBold
        Medium -> FontWeight.Bold
        High, Vital -> FontWeight.ExtraBold
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(CornerSize(4.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                "${task.name}: ${task.description}",
                fontSize = 20.sp,
                fontWeight = pickWeight(task.priority)
            )

            Row {
                OutlinedButton(onClick = { onDelete(task) }) {
                    Text("Delete")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = { onUpdate(task) }) {
                    Text("Update")
                }
            }
        }
    }
}

@Composable
fun UpdateTaskDialog(
    task: Task,
    onConfirm: (Task) -> Unit
) {
    var description by remember { mutableStateOf(task.description) }
    var priorityText by remember { mutableStateOf(task.priority.toString()) }
    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White,
        textColor = Color.Blue
    )

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            shape = RoundedCornerShape(CornerSize(4.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Update ${task.name}", fontSize = 20.sp)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    colors = colors
                )
                TextField(
                    value = priorityText,
                    onValueChange = { priorityText = it },
                    label = { Text("Priority") },
                    colors = colors
                )
                OutlinedButton(onClick = {
                    val newTask = Task(
                        task.name,
                        description,
                        try {
                            Priority.valueOf(priorityText)
                        } catch (e: IllegalArgumentException) {
                            Low
                        }
                    )
                    onConfirm(newTask)
                }) {
                    Text("Update")
                }
            }
        }
    }
}