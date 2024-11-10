package com.example.todolist.e2e

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.todolist.MainActivity
import com.example.todolist.robot.DetailsScreenRobot
import com.example.todolist.robot.MainScreenRobot
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TodoListE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var testRepository: TestTodoRepository

    private lateinit var mainRobot: MainScreenRobot
    private lateinit var detailsRobot: DetailsScreenRobot

    @Before
    fun setUp() {
        hiltRule.inject()
        testRepository.clear()
        mainRobot = MainScreenRobot(composeRule)
        detailsRobot = DetailsScreenRobot(composeRule)
    }

    @Test
    fun addTodoSuccess_showsLoadingAndNavigatesBack() {
        // Start at main screen
        mainRobot.verifyMainScreen()

        // Navigate to details
        mainRobot.clickAddTodo()

        // Add todo
        detailsRobot.enterTodoText("Test Todo")
        detailsRobot.clickSave()

        // Verify loading
        detailsRobot.verifyLoadingIndicator()

        // Wait for loading
        composeRule.waitForIdle()

        // Verify back on main screen
        mainRobot.verifyMainScreen()
    }

    @Test
    fun addTodoError_showsErrorAndNavigatesBack() {
        // Start at main screen
        mainRobot.verifyMainScreen()

        // Navigate to details
        mainRobot.clickAddTodo()

        // Add error todo
        detailsRobot.enterTodoText("Error")
        detailsRobot.clickSave()

        // Verify loading
        detailsRobot.verifyLoadingIndicator()

        // Wait for loading
        composeRule.waitForIdle()

        // Verify error on main screen
        mainRobot.verifyErrorMessage()
        mainRobot.verifyMainScreen()
    }
}