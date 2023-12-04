import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.ktor.model.Priority
import com.kmp.ktor.model.Priority.*
import com.kmp.ktor.model.Task
import com.kmp.ktor.network.TaskApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun App() {
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
                        scope.launch {
                            client.updateTask(it)
                        }
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
    fun pickColor(priority: Priority) = when(priority) {
        Low -> Color.Black
        Medium -> Color.Blue
        High, Vital -> Color.Red
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(CornerSize(4.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                "${task.name}: ${task.description}",
                fontSize = 20.sp,
                color = pickColor(task.priority)
            )

            Row {
                Button(onClick = { onDelete(task) }) {
                    Text("Delete Task")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onUpdate(task) }) {
                    Text("Update Task")
                }
            }
        }
    }
}