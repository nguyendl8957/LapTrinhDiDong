
package com.example.tuan6bai1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tuan6bai1.data.Task
import com.example.tuan6bai1.data.TaskRepository
import com.example.tuan6bai1.data.toTask
import com.example.tuan6bai1.ui.screens.*
import com.example.tuan6bai1.ui.theme.Tuan6bai1Theme

import android.widget.Toast
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Tuan6bai1Theme {
                val repository = remember { TaskRepository() }
                val tasks = remember { mutableStateListOf<Task>() }

                var isLoading by remember { mutableStateOf(true) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var reloadKey by remember { mutableIntStateOf(0) }

                var screen by remember { mutableStateOf(AppScreen.HOME) }
                var previousScreen by remember {
                    mutableStateOf(AppScreen.HOME)
                }
                var selectedId by remember { mutableStateOf("") }

                // Gọi API khi mở app hoặc nhấn Thử lại
                LaunchedEffect(reloadKey) {
                    isLoading = true
                    errorMessage = null

                    try {
                        val result = repository.getTasks()
                        tasks.clear()
                        tasks.addAll(result.map { it.toTask() })
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Không thể tải dữ liệu"
                    } finally {
                        isLoading = false
                    }
                }

                LaunchedEffect(screen, selectedId) {
                    if (screen == AppScreen.DETAIL) {
                        try {
                            val detail = repository
                                .getTaskById(selectedId.toInt())
                                .toTask()

                            val index = tasks.indexOfFirst {
                                it.id == selectedId
                            }

                            if (index >= 0) {
                                tasks[index] = detail
                            }
                        } catch (e: kotlinx.coroutines.CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            android.util.Log.e(
                                "UTH_API",
                                "GET chi tiết thất bại",
                                e
                            )
                        }
                    }
                }

                fun openDetail(id: String, from: AppScreen) {
                    selectedId = id
                    previousScreen = from
                    screen = AppScreen.DETAIL
                }


                val scope = rememberCoroutineScope()
                var isMutating by remember { mutableStateOf(false) }

                fun toggleTask(id: String) {
                    if (isMutating) return

                    val index = tasks.indexOfFirst { it.id == id }
                    if (index < 0) return

                    val newDone = !tasks[index].done

                    scope.launch {
                        isMutating = true
                        try {
                            val success = repository.updateTaskDone(
                                id = id.toInt(),
                                done = newDone
                            )

                            if (success) {
                                val currentIndex = tasks.indexOfFirst {
                                    it.id == id
                                }
                                if (currentIndex >= 0) {
                                    tasks[currentIndex] =
                                        tasks[currentIndex].copy(done = newDone)
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    "PATCH đã nhận phản hồi thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "PATCH bị máy chủ từ chối",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@MainActivity,
                                "Lỗi PATCH: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            isMutating = false
                        }
                    }
                }

                fun deleteTask(id: String) {
                    if (isMutating) return

                    scope.launch {
                        isMutating = true
                        try {
                            val success = repository.deleteTask(id.toInt())

                            if (success) {
                                tasks.removeAll { it.id == id }
                                screen = previousScreen

                                Toast.makeText(
                                    this@MainActivity,
                                    "DELETE đã nhận phản hồi thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "DELETE bị máy chủ từ chối",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@MainActivity,
                                "Lỗi DELETE: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            isMutating = false
                        }
                    }
                }


                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Không tải được công việc")
                            Spacer(Modifier.height(8.dp))
                            Text(errorMessage ?: "")
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { reloadKey++ }) {
                                Text("Thử lại")
                            }
                        }
                    }

                    else -> {
                        when (screen) {
                            AppScreen.HOME -> HomeScreen(
                                tasks = tasks,
                                onTasks = { screen = AppScreen.TASKS },
                                onTask = {
                                    openDetail(it, AppScreen.HOME)
                                },
                                onToggle = { id -> toggleTask(id) }
                            )

                            AppScreen.TASKS -> TasksScreen(
                                tasks = tasks,
                                onHome = { screen = AppScreen.HOME },
                                onTask = {
                                    openDetail(it, AppScreen.TASKS)
                                },
                                onToggle = { id -> toggleTask(id) }
                            )

                            AppScreen.DETAIL -> {
                                val task = tasks.firstOrNull {
                                    it.id == selectedId
                                }

                                if (task == null) {
                                    LaunchedEffect(selectedId) {
                                        screen = previousScreen
                                    }
                                } else {
                                    TaskDetailScreen(
                                        task = task,
                                        onBack = {
                                            screen = previousScreen
                                        },
                                        onToggle = { toggleTask(selectedId) },
                                        onSubtasks = { subtasks ->
                                            val i = tasks.indexOfFirst {
                                                it.id == selectedId
                                            }
                                            if (i >= 0) {
                                                tasks[i] = tasks[i].copy(
                                                    subtasks = subtasks
                                                )
                                            }
                                        },
                                        onDelete = { deleteTask(selectedId) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
