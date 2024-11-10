package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.screen.details.DetailsScreen
import com.example.todolist.ui.screen.main.MainScreen
import com.example.todolist.ui.theme.TodoTheme
import com.example.todolist.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Change status bar color
        window.statusBarColor = getColor(R.color.dark_green) // or any color you want
        setContent {
            TodoTheme {
                val navController = rememberNavController()
                var showError by remember { mutableStateOf<String?>(null) }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Main.route
                ) {
                    composable(Screen.Main.route) {
                        MainScreen(
                            onAddNewTodo = {
                                navController.navigate(Screen.Details.route)
                            }
                        )
                    }
                    composable(Screen.Details.route) {
                        DetailsScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onError = { error ->
                                showError = error
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}