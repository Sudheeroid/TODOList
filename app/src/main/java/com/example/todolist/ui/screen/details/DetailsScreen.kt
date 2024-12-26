package com.example.todolist.ui.screen.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.viewmodel.DetailsViewModel
import com.example.todolist.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onError: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.Success -> {
                    isLoading = false
                    onNavigateBack()
                }

                is UiEvent.Error -> {
                    isLoading = false
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    onError(event.message)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_gray))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Details Screen",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green)
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter Task Details") },
                    placeholder = { Text("E.g., Buy groceries") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 16.sp,color = Color.Black),

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.dark_green),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.dark_green),
                        backgroundColor = colorResource(id = R.color.light_gray)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading,
                    singleLine = true
                )

                Button(
                    onClick = {
                        isLoading = true
                        viewModel.addTodo(text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.dark_green),
                        contentColor = Color.White
                    ),
                    enabled = text.isNotBlank() && !isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Add to TODO List",
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}
