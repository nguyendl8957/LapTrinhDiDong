package com.example.tuan6bai1.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.example.tuan6bai1.R
import com.example.tuan6bai1.data.*

enum class AppScreen { HOME, TASKS, DETAIL }
private val StudyBg = Color(0xFFE6EDFF); private val StudyFg = Color(0xFF1F4FD1)
private val LifeBg = Color(0xFFFFEBD9); private val LifeFg = Color(0xFFA24A08)

@Composable private fun AppBottomBar(selected: AppScreen, onHome: () -> Unit, onTasks: () -> Unit) {
    Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).navigationBarsPadding().height(70.dp).padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        NavItem(R.drawable.ic_nav_home, "Trang chủ", selected == AppScreen.HOME, onHome, Modifier.weight(1f))
        NavItem(R.drawable.ic_nav_tasks, "Danh sách", selected == AppScreen.TASKS, onTasks, Modifier.weight(1f))
        Box(Modifier.weight(1f).fillMaxHeight(), Alignment.Center) { Box(Modifier.offset(y = (-16).dp).size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), Alignment.Center) { Icon(painterResource(R.drawable.ic_nav_add), null, tint = Color.White) } }
        NavItem(R.drawable.ic_nav_bell, "Thông báo", false, {}, Modifier.weight(1f))
        NavItem(R.drawable.ic_nav_settings, "Cài đặt", false, {}, Modifier.weight(1f))
    }
}
@Composable private fun NavItem(icon: Int, title: String, active: Boolean, click: () -> Unit, modifier: Modifier) {
    val tint = if (active) MaterialTheme.colorScheme.primary else Color(0xFF6B6B70)
    Column(modifier.clickable(enabled = active || title == "Trang chủ" || title == "Danh sách", onClick = click).padding(vertical = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painterResource(icon), null, Modifier.size(24.dp), tint = tint); Text(title, fontSize = 11.sp, color = tint, fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium)
    }
}

@Composable fun HomeScreen(tasks: List<Task>, onTasks: () -> Unit, onTask: (String) -> Unit, onToggle: (String) -> Unit) {
    val today = tasks.sortedWith(
        compareBy<Task> { it.dueDate }
            .thenBy { it.dueTime }
    )
    val done = today.count { it.done }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(.12f)), Alignment.Center) { Text("T", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                Column(Modifier.weight(1f)) { Text("Hôm nay", color = MaterialTheme.colorScheme.onSurfaceVariant); Text("Chào bạn!", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                Icon(painterResource(R.drawable.ic_home_bell), "Thông báo", Modifier.size(24.dp))
            } }
            item { Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(MaterialTheme.colorScheme.primary).padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("Tiến độ công việc", color = Color.White); Text("$done/${today.size} việc", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White); Text(if (done == today.size) "Xong hết rồi, nghỉ ngơi thôi!" else "Bắt đầu từ việc nhỏ nhất nhé.", color = Color.White) }
                Box(Modifier.size(66.dp), Alignment.Center) { CircularProgressIndicator({ if (today.isEmpty()) 0f else done.toFloat() / today.size }, color = Color.White, trackColor = Color.White.copy(.28f)); Text("${if (today.isEmpty()) 0 else done * 100 / today.size}%", color = Color.White, fontWeight = FontWeight.Bold) }
            } }
            item { Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { CategoryCard("Học tập", tasks.count { it.category == Category.STUDY && !it.done }, R.drawable.ic_home_study, StudyBg, StudyFg, Modifier.weight(1f)); CategoryCard("Cuộc sống", tasks.count { it.category == Category.LIFE && !it.done }, R.drawable.ic_home_life, LifeBg, LifeFg, Modifier.weight(1f)) } }
            item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Danh sách công việc", fontSize = 18.sp, fontWeight = FontWeight.Bold); Text("Xem tất cả", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable(onClick = onTasks).padding(8.dp)) } }
            if (today.isEmpty()) item { Text("Hôm nay chưa có việc nào.") }
            items(today, key = { it.id }) { TaskRow(it, { onTask(it.id) }, { onToggle(it.id) }) }
        }
        AppBottomBar(AppScreen.HOME, {}, onTasks)
    }
}
@Composable private fun CategoryCard(title: String, count: Int, icon: Int, bg: Color, fg: Color, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(bg), Alignment.Center) { Icon(painterResource(icon), null, tint = fg, modifier = Modifier.size(20.dp)) }
        Text(title, fontWeight = FontWeight.SemiBold); Text("$count việc còn lại", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}
@Composable private fun TaskRow(task: Task, onClick: () -> Unit, onToggle: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(44.dp).clickable(onClick = onToggle), Alignment.Center) { Box(Modifier.size(26.dp).clip(CircleShape).then(if (task.done) Modifier.background(MaterialTheme.colorScheme.primary) else Modifier.border(2.dp, Color(0xFFC7C7CC), CircleShape)), Alignment.Center) { if (task.done) Icon(painterResource(R.drawable.ic_home_check), null, tint = Color.White, modifier = Modifier.size(14.dp)) } }
        Column(Modifier.weight(1f).clickable(onClick = onClick).padding(vertical = 6.dp)) { Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None); Text("${if (task.category == Category.STUDY) "Học tập" else "Cuộc sống"}  ·  ${task.dueTime}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        Icon(painterResource(R.drawable.ic_home_chevron), null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}

@Composable fun TasksScreen(tasks: List<Task>, onHome: () -> Unit, onTask: (String) -> Unit, onToggle: (String) -> Unit) {
    var query by remember { mutableStateOf("") }; var filter by remember { mutableStateOf("Tất cả") }
    val shown = tasks.filter { it.title.contains(query, true) && (filter == "Tất cả" || (filter == "Học tập") == (it.category == Category.STUDY)) }
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Text("Danh sách", fontSize = 28.sp, fontWeight = FontWeight.Bold) }
            item { Row(Modifier.fillMaxWidth().height(46.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE9E9EE)).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(painterResource(R.drawable.ic_tasks_search), null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.width(8.dp)); BasicTextField(query, { query = it }, Modifier.weight(1f), singleLine = true, decorationBox = { inner -> if (query.isEmpty()) Text("Tìm công việc...", color = MaterialTheme.colorScheme.onSurfaceVariant); inner() }) } }
            item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Tất cả", "Học tập", "Cuộc sống").forEach { label -> FilterChip(selected = filter == label, onClick = { filter = label }, label = { Text(label) }) } } }
            item { SectionHeader("CẦN LÀM") }
            val pending = shown.filterNot { it.done }
            if (pending.isEmpty()) item { Text("Không tìm thấy công việc nào.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(pending, key = { "p${it.id}" }) { TaskRow(it, { onTask(it.id) }, { onToggle(it.id) }) }
            val completed = shown.filter { it.done }
            if (completed.isNotEmpty()) item { SectionHeader("ĐÃ XONG") }
            items(completed, key = { "d${it.id}" }) { TaskRow(it, { onTask(it.id) }, { onToggle(it.id) }) }
        }
        AppBottomBar(AppScreen.TASKS, onHome, {})
    }
}
@Composable private fun SectionHeader(title: String) { Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant) }

@Composable fun TaskDetailScreen(task: Task, onBack: () -> Unit, onToggle: () -> Unit, onSubtasks: (List<Subtask>) -> Unit, onDelete: () -> Unit) {
    var confirmDelete by remember { mutableStateOf(false) }
    if (confirmDelete) AlertDialog(onDismissRequest = { confirmDelete = false }, title = { Text("Xóa công việc?") }, text = { Text("Bạn có chắc muốn xóa không?") }, confirmButton = { TextButton(onClick = onDelete) { Text("Xóa", color = Color(0xFFC4321F)) } }, dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Hủy") } })
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) { Row(Modifier.clickable(onClick = onBack).padding(8.dp), verticalAlignment = Alignment.CenterVertically) { Icon(painterResource(R.drawable.ic_detail_back), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)); Text("Danh sách", color = MaterialTheme.colorScheme.primary) } }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(task.title, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { AssistChip(onClick = {}, label = { Text(if (task.category == Category.STUDY) "Học tập" else "Cuộc sống") }); AssistChip(onClick = {}, label = { Text("Ưu tiên ${when(task.priority) { "high" -> "Cao"; "low" -> "Thấp"; else -> "Vừa" }}") }) }
            DetailInfo("Hạn chót", "${task.dueDate} · ${task.dueTime}", R.drawable.ic_detail_calendar)
            DetailInfo("Nhắc nhở", "${task.dueTime} (15 phút trước)", R.drawable.ic_detail_bell)
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp)) { Text("Ghi chú", fontWeight = FontWeight.SemiBold); Spacer(Modifier.height(8.dp)); Text(task.note.ifBlank { "Chưa có ghi chú" }, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Việc nhỏ", fontWeight = FontWeight.Bold); Text("${task.subtasks.count { it.done }}/${task.subtasks.size}") }
            LinearProgressIndicator({ if (task.subtasks.isEmpty()) 0f else task.subtasks.count { it.done }.toFloat() / task.subtasks.size }, Modifier.fillMaxWidth())
            task.subtasks.forEach { sub -> Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface).clickable { onSubtasks(task.subtasks.map { if (it.id == sub.id) it.copy(done = !it.done) else it }) }.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Checkbox(sub.done, onCheckedChange = { onSubtasks(task.subtasks.map { item -> if (item.id == sub.id) item.copy(done = it) else item }) }); Text(sub.title, textDecoration = if (sub.done) TextDecoration.LineThrough else TextDecoration.None) } }
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick = onToggle, Modifier.fillMaxWidth()) { Text(if (task.done) "Đánh dấu chưa hoàn thành" else "Đánh dấu hoàn thành") }; OutlinedButton(onClick = { confirmDelete = true }, Modifier.fillMaxWidth()) { Text("Xóa công việc", color = Color(0xFFC4321F)) } }
    }
}
@Composable private fun DetailInfo(title: String, value: String, icon: Int) { Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(painterResource(icon), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)); Spacer(Modifier.width(12.dp)); Column { Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp); Text(value, fontWeight = FontWeight.Medium) } } }
