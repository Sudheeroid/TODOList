package com.example.todolist.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.viewmodel.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onAddNewTodo: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Dismiss"
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = colorResource(R.color.primary_green),
                title = { Text("Todo List", color = Color.White) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewTodo,
                backgroundColor = colorResource(id = R.color.primary_green),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colorResource(id = R.color.light_gray))
        ) {
            // Add search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                placeholder = {
                    Text(
                        text = "Search",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.primary_green),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = colorResource(R.color.primary_green)
                ),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true
            )

            // Check if the filtered list is empty
            val filteredTodos = uiState.todos.filter {
                it.text.contains(searchQuery, ignoreCase = true)
            }

            if (filteredTodos.isEmpty()) {
                // Show empty state message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Press the + button to add a TODO item",
                        color = Color.Gray,
                        style = MaterialTheme.typography.body1
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTodos, key = { it.id }) { todo ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = { dismissValue ->
                                if (dismissValue == DismissValue.DismissedToStart) {
                                    viewModel.deleteTodo(todo)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = {
                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.EndToStart -> Color.Red
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Icon",
                                        tint = Color.White
                                    )
                                }
                            },
                            dismissContent = {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = 2.dp,
                                    backgroundColor = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = todo.text,
                                            color = Color.Black,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


