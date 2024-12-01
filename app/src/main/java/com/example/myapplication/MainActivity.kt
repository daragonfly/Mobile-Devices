package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TodoListScreen(modifier: Modifier = Modifier) {
    var todoItems by remember { mutableStateOf(listOf<TodoItem>()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val file = File(context.filesDir, "photo.jpg")
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri = uri
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Todo List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        todoItems.forEach { item ->
            TodoItemCard(item = item)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    context as ComponentActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    1
                )
            } else {
                launcher.launch(uri)
            }
        }) {
            Text(text = "Add Todo")
        }
    }
}

@Composable
fun TodoItemCard(item: TodoItem) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Due: ${item.dueDate}", style = MaterialTheme.typography.bodySmall)
            if (item.imageUri != null) {
                Image(
                    painter = rememberImagePainter(data = item.imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    MyApplicationTheme {
        TodoListScreen()
    }
}
