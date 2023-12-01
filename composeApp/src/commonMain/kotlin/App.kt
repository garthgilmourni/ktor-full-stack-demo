import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.kmp.ktor.model.Task
import com.kmp.ktor.network.TaskApi
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
                Card(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    shape = RoundedCornerShape(CornerSize(4.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("${task.name}: ${task.description}")
                        Text(task.priority.name, fontStyle = FontStyle.Italic)

                        Row {
                            Button(onClick = {
                                scope.launch {
                                    client.removeTask(task)
                                    tasks = client.getAllTasks()
                                }
                            }) {
                                Text("Delete Task")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    client.updateTask(task)
                                }
                            }) {
                                Text("Update Task")
                            }
                        }
                    }
                }
            }
        }
    }
}