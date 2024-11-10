package com.example.todolist.ui.screen.details

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(
            modifier = Modifier.padding(bottom = 40.dp,top = 40.dp),
            text = "Details Screen",
            fontSize = 20.sp,
            color = colorResource(id = R.color.dark_green)
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter Details") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.dark_green),
                unfocusedBorderColor = colorResource(id = R.color.dark_green),
                cursorColor = colorResource(id = R.color.dark_green)
            ),
            enabled = !isLoading
        )

        Button(
            onClick = {
                isLoading = true
                viewModel.addTodo(text)
            },
            modifier = Modifier.fillMaxWidth() .padding(top = 40.dp),
            enabled = text.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.dark_green),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Add to TODO list",
                    color = Color.White
                )
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.dark_green),
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}