package com.example.tuan3bai1

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import java.util.Calendar

private val Blue = Color(0xFF2196F3)
private val PaleBlue = Color(0xFFBDE1FA)
private val Ink = Color(0xFF243044)

// 9 nhom UI, tong cong 30 thanh phan nho.
data class UiGroup(val title: String, val examples: List<Pair<String, String>>)

private val groups = listOf(
    UiGroup("Display", listOf("Text" to "Displays styled text", "Image" to "Displays an image")),
    UiGroup("Input", listOf("TextField" to "Enter text", "PasswordField" to "Secure input", "Switch" to "On/off control", "Slider" to "Choose a number", "DatePicker" to "Choose a date")),
    UiGroup("Selection", listOf("Button" to "Trigger an action", "Checkbox" to "Multiple selection", "RadioButton" to "Single selection", "DropdownMenu" to "Choose from a menu")),
    UiGroup("Navigation", listOf("NavigationBar" to "Bottom destinations", "TabRow" to "Switch tabs", "NavigationDrawer" to "Open side navigation")),
    UiGroup("Content", listOf("LazyColumn" to "Scrollable list", "Card" to "Content container", "HorizontalDivider" to "Separate sections")),
    UiGroup("Feedback", listOf("Snackbar" to "Temporary message", "AlertDialog" to "Confirmation dialog", "CircularProgressIndicator" to "Loading spinner", "LinearProgressIndicator" to "Progress bar")),
    UiGroup("Layout", listOf("Column" to "Vertical layout", "Row" to "Horizontal layout", "Box" to "Layered layout")),
    UiGroup("Actions", listOf("FloatingActionButton" to "Primary floating action", "IconButton" to "Icon action", "FilledTonalButton" to "Secondary action")),
    UiGroup("Data Display", listOf("Badge" to "Status indicator", "ListItem" to "Structured information", "AssistChip" to "Compact action"))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { UiExplorerApp() } }
    }
}

@Composable
fun UiExplorerApp() {
    var page by rememberSaveable { mutableStateOf("welcome") }
    var selected by rememberSaveable { mutableStateOf("Text") }
    fun back() { page = if (page == "detail") "list" else "welcome" }
    BackHandler(enabled = page != "welcome") { back() }
    when (page) {
        "welcome" -> WelcomePage { page = "list" }
        "list" -> CatalogPage(onOpen = { selected = it; page = "detail" })
        "detail" -> DetailPage(selected, onBack = { page = "list" })
    }
}

@Composable
fun PageHeader(title: String, onBack: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().height(58.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) Text("‹", fontSize = 32.sp, color = Blue, modifier = Modifier.clickable { onBack() }.padding(end = 12.dp))
        Text(title, color = Blue, fontSize = 19.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        if (onBack != null) Spacer(Modifier.width(22.dp))
    }
    HorizontalDivider(color = Color(0xFFE9EDF2))
}

@Composable
fun WelcomePage(onStart: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.White).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        Icon(painterResource(android.R.drawable.ic_menu_manage), contentDescription = "Compose icon", modifier = Modifier.size(110.dp), tint = Blue)
        Spacer(Modifier.height(24.dp))
        Text("Jetpack Compose", fontSize = 25.sp, color = Ink, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Jetpack Compose is an modern UI toolkit for\nbuilding native Android applications using\na declarative programming approach", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(Modifier.weight(1f))
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue)) {
            Text("I'm ready", fontSize = 17.sp)
        }
    }
}

@Composable
fun CatalogPage(onOpen: (String) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        PageHeader("UI Components List")
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp)) {
            groups.forEach { group ->
                Spacer(Modifier.height(10.dp))
                Text(group.title, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(bottom = 8.dp))
                group.examples.forEach { (name, desc) ->
                    Column(
                        Modifier.fillMaxWidth().padding(bottom = 8.dp).background(PaleBlue, RoundedCornerShape(9.dp))
                            .clickable { onOpen(name) }.padding(13.dp)
                    ) {
                        Text(name, fontWeight = FontWeight.Bold, color = Ink)
                        Text(desc, fontSize = 12.sp, color = Ink)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailPage(name: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        PageHeader("$name Detail", onBack)
        // Khung detail cuon duoc, mot so demo tu quan ly scroll rieng
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Interactive example: $name", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(28.dp))
            key(name) { ComponentExample(name) }
        }
    }
}

@Composable
private fun DemoTile(label: String, color: Color = Blue, modifier: Modifier = Modifier) {
    Box(modifier.height(52.dp).background(color, RoundedCornerShape(9.dp)).padding(5.dp), contentAlignment = Alignment.Center) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentExample(name: String) {
    val context = LocalContext.current
    var text by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var value by rememberSaveable { mutableFloatStateOf(40f) }
    var clicks by rememberSaveable { mutableIntStateOf(0) }
    var chosen by rememberSaveable { mutableStateOf("Option A") }
    var menuExpanded by remember { mutableStateOf(false) }
    var dateLabel by rememberSaveable { mutableStateOf("Chưa chọn ngày") }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var dialogOpen by remember { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("Chưa xác nhận") }
    var snackbarText by remember { mutableStateOf("") }
    var chipSelected by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    when (name) {
        "Text" -> Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("The quick Brown", color = Color(0xFFB67D2B), fontSize = 26.sp)
            Text("fox jumps over", fontWeight = FontWeight.Bold, fontSize = 23.sp)
            Text("the lazy dog", textDecoration = TextDecoration.Underline, fontSize = 21.sp)
        }
        "Image" -> Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Image(painter = painterResource(android.R.drawable.ic_menu_gallery), contentDescription = "Sample Android image", modifier = Modifier.size(140.dp))
            Text("Image() displays a drawable resource", textAlign = TextAlign.Center)
        }
        "TextField" -> Column {
            OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Thông tin nhập") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp)); Text("Bạn đã nhập: $text", color = Blue)
        }
        "PasswordField" -> Column {
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mật khẩu") },
                visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp)); Text("Đã nhập ${password.length} ký tự")
        }
        "Switch" -> Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (active) "Đang bật" else "Đang tắt", modifier = Modifier.weight(1f))
            Switch(checked = active, onCheckedChange = { active = it })
        }
        "Slider" -> Column {
            Text("Giá trị: ${value.toInt()}", fontSize = 22.sp)
            Slider(value = value, onValueChange = { value = it }, valueRange = 0f..100f)
        }
        "DatePicker" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                val calendar = Calendar.getInstance()
                DatePickerDialog(context, { _, year, month, day -> dateLabel = "$day/${month + 1}/$year" },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }) { Text("Chọn ngày") }
            Spacer(Modifier.height(12.dp)); Text(dateLabel)
        }
        "Button" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { clicks++ }) { Text("Click me!") }
            Text("Đã nhấn $clicks lần")
        }
        "Checkbox" -> Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = active, onCheckedChange = { active = it })
            Text(if (active) "Đã chọn" else "Chưa chọn")
        }
        "RadioButton" -> Column {
            listOf("Option A", "Option B", "Option C").forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { chosen = option }) {
                    RadioButton(selected = chosen == option, onClick = { chosen = option })
                    Text(option)
                }
            }
            Text("Đã chọn: $chosen", color = Blue)
        }
        "DropdownMenu" -> Box {
            OutlinedButton(onClick = { menuExpanded = true }) { Text("$chosen  ▾") }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                listOf("Option A", "Option B", "Option C").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { chosen = option; menuExpanded = false })
                }
            }
        }
        "NavigationBar" -> Column {
            NavigationBar {
                listOf("Home", "Search", "Profile").forEachIndexed { index, label ->
                    NavigationBarItem(selected = selectedTab == index, onClick = { selectedTab = index },
                        icon = { Text(listOf("⌂", "⌕", "●")[index], fontSize = 20.sp) }, label = { Text(label) })
                }
            }
            Text("Trang đang chọn: ${listOf("Home", "Search", "Profile")[selectedTab]}")
        }
        "TabRow" -> Column {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                listOf("Tab 1", "Tab 2", "Tab 3").forEachIndexed { index, label ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(label) })
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Nội dung Tab ${selectedTab + 1}")
        }
        "NavigationDrawer" -> Column {
            Button(onClick = { scope.launch { drawerState.open() } }) { Text("Mở Navigation Drawer") }
            Spacer(Modifier.height(12.dp))
            // Drawer duoc gioi han trong khu vuc minh hoa
            ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
                ModalDrawerSheet {
                    Text("Menu", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Bold)
                    listOf("Home", "Settings", "About").forEach { destination ->
                        NavigationDrawerItem(label = { Text(destination) }, selected = chosen == destination,
                            onClick = { chosen = destination; scope.launch { drawerState.close() } })
                    }
                }
            }) {
                Surface(Modifier.fillMaxWidth().height(110.dp), color = PaleBlue, shape = RoundedCornerShape(12.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("Đã chọn: $chosen") }
                }
            }
        }
        "LazyColumn" -> Column {
            Text("Danh sách 10 dòng", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth().height(260.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items((1..10).toList()) { n -> DemoTile("Item $n", modifier = Modifier.fillMaxWidth()) }
            }
        }
        "Card" -> Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text("Student Card", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Tên: Nguyễn Văn An")
                Text("Lớp: CNTT")
            }
        }
        "HorizontalDivider" -> Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Section 1")
            HorizontalDivider()
            Text("Section 2")
            HorizontalDivider(thickness = 3.dp, color = Blue)
            Text("Section 3")
        }
        "Snackbar" -> Box(Modifier.fillMaxWidth().height(200.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { snackbarText = "Lưu thành công!"; scope.launch { snackbarHostState.showSnackbar(snackbarText) } }) { Text("Show Snackbar") }
                Text("Nhấn để xem thông báo ngắn", color = Color.Gray)
            }
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
        "AlertDialog" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { dialogOpen = true }) { Text("Open Dialog") }
            Text(dialogMessage)
            if (dialogOpen) AlertDialog(onDismissRequest = { dialogOpen = false }, title = { Text("Xác nhận") },
                text = { Text("Bạn có muốn tiếp tục không?") },
                confirmButton = { TextButton(onClick = { dialogMessage = "Đã đồng ý"; dialogOpen = false }) { Text("Đồng ý") } },
                dismissButton = { TextButton(onClick = { dialogOpen = false }) { Text("Hủy") } })
        }
        "CircularProgressIndicator" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(); Spacer(Modifier.height(12.dp)); Text("Đang tải...")
        }
        "LinearProgressIndicator" -> Column(Modifier.fillMaxWidth()) {
            Text("Tiến độ ${value.toInt()}%")
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Slider(value = value, onValueChange = { value = it }, valueRange = 0f..100f)
        }
        "Column" -> Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(4) { DemoTile("Column ${it + 1}", modifier = Modifier.fillMaxWidth()) }
        }
        "Row" -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(4) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { n -> DemoTile("${n + 1}", if (n == 1) Blue else Color(0xFF94B8ED), Modifier.weight(1f)) }
                }
            }
        }
        "Box" -> Box(Modifier.fillMaxWidth().height(180.dp).background(PaleBlue, RoundedCornerShape(12.dp))) {
            DemoTile("Top left", modifier = Modifier.align(Alignment.TopStart).width(120.dp))
            DemoTile("Center", Color(0xFF496ED1), Modifier.align(Alignment.Center).width(120.dp))
            DemoTile("Bottom right", Color(0xFF265399), Modifier.align(Alignment.BottomEnd).width(140.dp))
        }
        "FloatingActionButton" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FloatingActionButton(onClick = { clicks++ }) { Text("+", fontSize = 27.sp) }
            Spacer(Modifier.height(14.dp)); Text("Đã thêm $clicks lần")
        }
        "IconButton" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { active = !active }) {
                Icon(painterResource(if (active) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off),
                    contentDescription = "Favorite", tint = Color(0xFFEDAC32), modifier = Modifier.size(32.dp))
            }
            Text(if (active) "Đã yêu thích" else "Chưa yêu thích")
        }
        "FilledTonalButton" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledTonalButton(onClick = { clicks++ }) { Text("Tonal Action") }
            Text("Lượt nhấn: $clicks")
        }
        "Badge" -> Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Tin nhắn", fontSize = 19.sp)
            Badge { Text("5") }
        }
        "ListItem" -> ListItem(headlineContent = { Text("Nguyễn Văn An") },
            supportingContent = { Text("Sinh viên CNTT") }, leadingContent = { Text("👤") },
            trailingContent = { Text("›", color = Blue, fontSize = 26.sp) })
        "AssistChip" -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AssistChip(onClick = { chipSelected = !chipSelected }, label = { Text(if (chipSelected) "Đã chọn ✓" else "Chọn bộ lọc") })
            Text(if (chipSelected) "Bộ lọc đang bật" else "Bộ lọc đang tắt")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() { MaterialTheme { WelcomePage {} } }

@Preview(showBackground = true)
@Composable
fun CatalogPreview() { MaterialTheme { CatalogPage {} } }

@Preview(showBackground = true)
@Composable
fun TextDetailPreview() { MaterialTheme { DetailPage("Text", {}) } }
