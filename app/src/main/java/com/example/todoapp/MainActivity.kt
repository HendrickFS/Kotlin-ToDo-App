package com.example.todoapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.ui.theme.ToDoAppTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

data class ToDo(
    val id: Int,
    var activity: String,
    var completed: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ToDoApp(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ToDoApp(name: String, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var ToDoList by remember { mutableStateOf(listOf<ToDo>()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemIdToDelete by remember { mutableStateOf(0) }
    var showEditDialog by remember { mutableStateOf(false) }

    val maxLengthActivity = 70

    if (!showDeleteDialog and !showEditDialog) itemIdToDelete = 0

    val gradientBrush = Brush.verticalGradient(colors = listOf(Color(0xFF83D475), Color(0xFF57C84D)))
    val cardBgColor = Color(0xFFC5E8B7)

    Column(
            modifier = Modifier
                .background(gradientBrush)
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text(
                "ToDo List",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            TextField(
                value = input,
                onValueChange = {
                    if ( input.length < maxLengthActivity ) input = it
                },
                placeholder = { Text("Type your activity") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFC5E8B7),
                    unfocusedContainerColor = Color(0xFFC5E8B7)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                maxLines = 3
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Red,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(2.dp, Color.Red),
                    onClick = {
                        input = ""
                    }
                ) {
                    Text("Clear")
                }
                Button(
                    colors = ButtonColors(
                        containerColor = Color(0xFF2EB62C),
                        contentColor = Color(color = 0xFFFFFFFF),
                        disabledContentColor = Color(0xFFFFFFFF),
                        disabledContainerColor = Color(0xFF2EB62C)
                    ),
                    onClick = {
                        if (input > "") {
                            var item = ToDo(
                                id = ToDoList.size + 1,
                                activity = input,
                            )
                            ToDoList += item
                        }
                        input = ""
                    }
                ) {
                    Text("Add")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    "To Do:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyColumn {
                    items(ToDoList) { item ->
                        if (!item.completed) {
                            ToDoItem(
                                item = item,
                                onCheckClick = { checked ->
                                    ToDoList = ToDoList.map {
                                        if (it.id == item.id) it.copy(completed = checked) else it
                                    }
                                },
                                onDeleteClick = {
                                    showDeleteDialog = true
                                    itemIdToDelete = item.id
                                },
                                onEditClick = {
                                    showEditDialog = true
                                    itemIdToDelete = item.id
                                    input = item.activity
                                })
                        }
                    }
                }

                Text(
                    "Done:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                LazyColumn {
                    items(ToDoList) { item ->
                        if (item.completed) {
                            ToDoItem(
                                item = item,
                                onCheckClick = { checked ->
                                    ToDoList = ToDoList.map {
                                        if (it.id == item.id) it.copy(completed = checked) else it
                                    }
                                },
                                onDeleteClick = {
                                    showDeleteDialog = true
                                    itemIdToDelete = item.id
                                },
                                onEditClick = {
                                    showEditDialog = true
                                    itemIdToDelete = item.id
                                    input = item.activity
                                })
                        }
                    }
                }
            }
        }
        if(showDeleteDialog){
            Dialog(
                onDismissRequest = {
                    showDeleteDialog = false
                }
            ) {
                Card (
                    modifier = Modifier
                        .width(300.dp)
                        .height(150.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBgColor
                    )
                ) {
                    Text(
                        text = "Delete item?",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        TextButton(
                            onClick = {
                                var item = ToDoList.filter { it.id == itemIdToDelete }
                                ToDoList -= item
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
        if(showEditDialog){
            Dialog(
                onDismissRequest = {
                    showEditDialog = false
                }
            ) {
                Card (
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBgColor
                    )
                ) {
                    Text(
                        text = "Update activity",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .wrapContentSize(Alignment.Center),
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 30.dp),
                        value = input,
                        onValueChange = {
                            if (input.length < maxLengthActivity) input = it
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextButton(
                            onClick = { showEditDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        TextButton(
                            onClick = {
                                ToDoList = ToDoList.map {
                                    if(it.id == itemIdToDelete) it.copy(activity = input) else it
                                }
                                showEditDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
}

@Composable
fun ToDoItem(item: ToDo,
             onCheckClick: (Boolean) -> Unit,
             onDeleteClick: () -> Unit,
             onEditClick: () -> Unit
             ){
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.completed,
                onCheckedChange = { onCheckClick(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2EB62C)
                )
            )
            Text(
                modifier = Modifier
                    .width(250.dp),
                text = item.activity,
                color = Color.White,
                overflow = TextOverflow.Clip,

            )
        }
        Row {
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoAppPreview() {
    ToDoAppTheme {
        ToDoApp("Android")
    }
}