package com.example.todolist.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class DetailsScreenRobot(private val composeRule: ComposeTestRule) {
    fun enterTodoText(text: String) {
        composeRule.onNodeWithText("Todo text").performTextInput(text)
    }

    fun clickSave() {
        composeRule.onNodeWithText("Add TODO").performClick()
    }

    fun verifyLoadingIndicator() {
        composeRule.onNodeWithTag("loading_indicator").assertExists()
    }
}