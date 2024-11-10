package com.example.todolist.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class MainScreenRobot(private val composeRule: ComposeTestRule) {
    fun verifyMainScreen() {
        composeRule.onNodeWithText("Todo List").assertExists()
    }

    fun clickAddTodo() {
        composeRule.onNodeWithText("Add TODO").performClick()
    }

    fun verifyErrorMessage() {
        composeRule.onNodeWithText("Failed to add TODO").assertExists()
    }
}