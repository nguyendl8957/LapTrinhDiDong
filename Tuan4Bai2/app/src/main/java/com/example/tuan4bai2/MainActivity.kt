package com.example.tuan4bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF2F55A2)
private val CanvasColor = Color(0xFFF7F7FB)

// 1. ABSTRACTION
abstract class LibraryItem(val id: String, val title: String) {
    abstract fun canBorrow(): Boolean
    abstract fun typeLabel(): String
}

// 2. INHERITANCE
class Book(id: String, title: String, val author: String) : LibraryItem(id, title) {
    // 3. POLYMORPHISM
    override fun canBorrow() = true
    override fun typeLabel() = "Sách thường"
}

class ReferenceBook(id: String, title: String, val author: String) : LibraryItem(id, title) {
    override fun canBorrow() = false
    override fun typeLabel() = "Sách tham khảo tại chỗ"
}

class Student(val id: String, val name: String) {
    // 4. ENCAPSULATION
    private val borrowedIds = mutableStateListOf<String>()
    fun borrowedBooks(): List<String> = borrowedIds.toList()
    fun hasBook(bookId: String): Boolean = bookId in borrowedIds
    fun borrow(bookId: String) { if (bookId !in borrowedIds) borrowedIds.add(bookId) }
    fun giveBack(bookId: String) { borrowedIds.remove(bookId) }
}

class LibraryManager {
    val books = mutableStateListOf<LibraryItem>(
        Book("B01", "Sách 01", "Tác giả A"),
        Book("B02", "Sách 02", "Tác giả B"),
        ReferenceBook("B03", "Từ điển thư viện", "Nhà xuất bản UTH")
    )
    val students = mutableStateListOf(
        Student("SV01", "Nguyen Van A"),
        Student("SV02", "Tran Thi B"),
        Student("SV03", "Le Minh C")
    )

    fun ownerOf(bookId: String): Student? = students.firstOrNull { it.hasBook(bookId) }

    fun borrow(student: Student, book: LibraryItem): String {
        if (!book.canBorrow()) return "${book.title} chỉ được đọc tại thư viện"
        if (student.hasBook(book.id)) return "Sinh viên đã mượn sách này"
        if (ownerOf(book.id) != null) return "Sách đang được sinh viên khác mượn"
        student.borrow(book.id)
        return "Đã cho ${student.name} mượn ${book.title}"
    }

    fun returnBook(student: Student, book: LibraryItem): String {
        if (!student.hasBook(book.id)) return "Sinh viên chưa mượn sách này"
        student.giveBack(book.id)
        return "Đã trả ${book.title}"
    }

    fun addBook(title: String, author: String, reference: Boolean): String {
        if (title.isBlank()) return "Hãy nhập tên sách"
        val next = "B" + (books.size + 1).toString().padStart(2, '0')
        val item = if (reference) ReferenceBook(next, title.trim(), author.trim())
        else Book(next, title.trim(), author.trim())
        books.add(item)
        return "Đã thêm ${item.title}"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { LibraryApp() } }
    }
}

@Composable
fun LibraryApp() {
    val manager = remember { LibraryManager() }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedStudent by remember { mutableIntStateOf(0) }
    var selectedBookIds by remember { mutableStateOf(setOf<String>()) }
    var message by remember { mutableStateOf("") }
    var addDialog by remember { mutableStateOf(false) }
    val student = manager.students[selectedStudent]

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                listOf("Quản lý", "DS Sách", "Sinh viên").forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text(listOf("⌂", "▤", "♟")[index], fontSize = 23.sp) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            Text("Hệ thống\nQuản lý Thư viện", fontSize = 25.sp,
                lineHeight = 29.sp, fontWeight = FontWeight.Bold,
                color = Color.Black, textAlign = TextAlign.Center)
            Spacer(Modifier.height(30.dp))

            when (selectedTab) {
                0 -> {
                    Text("Sinh viên", modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    var expanded by remember { mutableStateOf(false) }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = student.name, onValueChange = {}, readOnly = true,
                            modifier = Modifier.weight(1f), singleLine = true
                        )
                        Spacer(Modifier.width(9.dp))
                        Box {
                            Button(onClick = { expanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Navy)) {
                                Text("Thay đổi")
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                manager.students.forEachIndexed { index, s ->
                                    DropdownMenuItem(text = { Text(s.name) }, onClick = {
                                        selectedStudent = index
                                        selectedBookIds = emptySet()
                                        expanded = false
                                    })
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(25.dp))
                    Text("Danh sách sách", modifier = Modifier.fillMaxWidth(),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f)
                            .background(Color(0xFFDDDDDD), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        items(manager.books, key = { it.id }) { book ->
                            val owner = manager.ownerOf(book.id)
                            Surface(color = Color.White, shape = RoundedCornerShape(22.dp),
                                shadowElevation = 2.dp) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = book.id in selectedBookIds,
                                        onCheckedChange = { checked ->
                                            selectedBookIds = if (checked) selectedBookIds + book.id
                                            else selectedBookIds - book.id
                                        },
                                        enabled = book.canBorrow() && owner == null
                                    )
                                    Column(Modifier.weight(1f)) {
                                        Text(book.title, fontWeight = FontWeight.Medium)
                                        Text(when {
                                            !book.canBorrow() -> "Chỉ đọc tại chỗ"
                                            owner != null -> "Đang mượn: ${owner.name}"
                                            else -> "Có thể mượn"
                                        }, fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (selectedBookIds.isEmpty()) {
                                addDialog = true
                            } else {
                                val messages = selectedBookIds.mapNotNull { id ->
                                    manager.books.firstOrNull { it.id == id }?.let {
                                        manager.borrow(student, it)
                                    }
                                }
                                selectedBookIds = emptySet()
                                message = messages.joinToString("\n")
                            }
                        },
                        modifier = Modifier.width(180.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Navy)
                    ) { Text(if (selectedBookIds.isEmpty()) "Thêm" else "Mượn sách") }
                }
                1 -> {
                    Text("Danh sách tất cả sách", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(12.dp))
                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(manager.books, key = { it.id }) { book ->
                            Surface(color = CanvasColor, shape = RoundedCornerShape(12.dp)) {
                                Column(Modifier.fillMaxWidth().padding(14.dp)) {
                                    Text("${book.id} · ${book.title}", fontWeight = FontWeight.Bold)
                                    Text(book.typeLabel(), color = Navy)
                                    Text(manager.ownerOf(book.id)?.let { "Đang mượn: ${it.name}" }
                                        ?: if (book.canBorrow()) "Có sẵn" else "Không cho mượn",
                                        fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                    Button(onClick = { addDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy)) {
                        Text("+ Thêm sách")
                    }
                }
                2 -> {
                    Text("Danh sách sinh viên và sách đã mượn",
                        fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(12.dp))
                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(manager.students, key = { it.id }) { s ->
                            Surface(color = CanvasColor, shape = RoundedCornerShape(12.dp)) {
                                Column(Modifier.fillMaxWidth().padding(13.dp)) {
                                    Text("${s.id} – ${s.name}", fontWeight = FontWeight.Bold)
                                    val borrowed = s.borrowedBooks()
                                    if (borrowed.isEmpty()) {
                                        Text("Chưa mượn sách", color = Color.Gray)
                                    } else {
                                        borrowed.forEach { id ->
                                            val book = manager.books.firstOrNull { it.id == id }
                                            if (book != null) {
                                                Row(verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()) {
                                                    Text(book.title, modifier = Modifier.weight(1f))
                                                    TextButton(onClick = {
                                                        message = manager.returnBook(s, book)
                                                    }) { Text("Trả sách") }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (message.isNotBlank()) {
                Text(message, fontSize = 12.sp, color = Navy,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp))
            }
            Spacer(Modifier.height(12.dp))
        }
    }

    if (addDialog) {
        var title by remember { mutableStateOf("") }
        var author by remember { mutableStateOf("") }
        var reference by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { addDialog = false },
            title = { Text("Thêm sách mới") },
            text = {
                Column {
                    OutlinedTextField(title, { title = it }, label = { Text("Tên sách") })
                    OutlinedTextField(author, { author = it }, label = { Text("Tác giả") })
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(reference, { reference = it })
                        Text("Sách tham khảo (không cho mượn)")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val response = manager.addBook(title, author, reference)
                    message = response
                    if (title.isNotBlank()) addDialog = false
                }) { Text("Lưu") }
            },
            dismissButton = { TextButton(onClick = { addDialog = false }) { Text("Hủy") } }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryPreview() { MaterialTheme { LibraryApp() } }
