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
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(state.error) {
        state.error?.let { error ->
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
                title = { Text("Todo List") },
                actions = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = viewModel::updateSearchQuery,
                        placeholder = {
                            Text(
                                text = "Search",
                                color = Color.White
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                        shape = RoundedCornerShape(50),

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White
                        ),

                        textStyle = TextStyle(
                            color = colorResource(R.color.white)
                        ),
                        singleLine = true
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewTodo) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo", tint = Color.Black)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.todos.isEmpty() -> {
                    Text(
                        text = "Press the + button to add a TODO item",
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.todos) { todo ->
                            val dismissState = rememberDismissState()

                            SwipeToDismiss(
                                state = dismissState,
                                background = {
                                    val color = colorResource(id = R.color.dark_green)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color)
                                            .padding(16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Todo",
                                            tint = Color.White
                                        )
                                    }
                                },
                                dismissContent = {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = 1.dp,
                                        backgroundColor = Color.White
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
                                            IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete Todo",
                                                    tint = colorResource(id = R.color.dark_green)
                                                )
                                            }
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(dismissState.isDismissed(DismissDirection.EndToStart)) {
                                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                                    viewModel.deleteTodo(todo) // Call delete method
                                }
                            }


                            /* Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = 1.dp,
                                backgroundColor = Color.White
                            ) {
                                Text(
                                    text = todo.text,
                                    color = Color.Black,
                                    modifier = Modifier.padding(16.dp)
                                )
                                IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
                                }
                            }*/
                        }
                    }
                }
            }
        }
    }
}

