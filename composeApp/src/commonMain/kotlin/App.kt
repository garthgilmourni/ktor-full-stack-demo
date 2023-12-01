import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmp.ktor.model.Task
import com.kmp.ktor.network.TaskApi
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TextButton
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val client = remember { TaskApi() }
        val tasks = remember { mutableStateOf(emptyList<Task>()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            tasks.value = client.getAllTasks()
        }

        LazyColumn(modifier = Modifier.height(400.dp)) {
            items(tasks.value) { task ->
                Card(
                    modifier = Modifier.padding(10.dp),
                    shape = RoundedCornerShape(CornerSize(16.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(task.name)
                        Text(task.description)
                        Text(task.priority.name)

                        Row {
                            TextButton(onClick = {
                                scope.launch {
                                    client.removeTask(task)
                                    tasks.value = client.getAllTasks()
                                }
                            }) {
                                Text("Delete Task")
                            }
                            TextButton(onClick = {
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