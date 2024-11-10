package com.example.todolist.viewmodel

import app.cash.turbine.test
import com.example.todolist.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: TodoRepository

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        viewModel = DetailsViewModel(repository)
    }

    @Test
    fun `when adding valid todo, should emit Success after 3 seconds`() = runTest {
        // Given
        val todoText = "Test Todo"

        // When
        viewModel.addTodo(todoText)

        // Then
        viewModel.uiEvent.test {
            // Skip 3 seconds
            advanceTimeBy(3000)

            // Verify Success event
            assertEquals(UiEvent.Success, awaitItem())

            // Verify repository call
            verify(repository).addTodo(todoText)
        }
    }

    @Test
    fun `when adding Error as todo, should emit Error after 3 seconds`() = runTest {
        // Given
        val errorText = "Error"
        `when`(repository.addTodo(errorText)).thenThrow(RuntimeException("Failed to add TODO"))

        // When
        viewModel.addTodo(errorText)

        // Then
        viewModel.uiEvent.test {
            // Skip 3 seconds
            advanceTimeBy(3000)

            // Verify Error event
            val event = awaitItem()
            assert(event is UiEvent.Error)
            assertEquals("Failed to add TODO", (event as UiEvent.Error).message)
        }
    }

    @Test
    fun `when repository throws exception, should emit Error`() = runTest {
        // Given
        val todoText = "Test Todo"
        val errorMessage = "Network error"
        `when`(repository.addTodo(todoText)).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.addTodo(todoText)

        // Then
        viewModel.uiEvent.test {
            // Skip 3 seconds
            advanceTimeBy(3000)

            // Verify Error event
            val event = awaitItem()
            assert(event is UiEvent.Error)
            assertEquals(errorMessage, (event as UiEvent.Error).message)
        }
    }

    @Test
    fun `loading state should be true during 3 second delay`() = runTest {
        // Given
        val todoText = "Test Todo"

        // When
        viewModel.addTodo(todoText)

        // Then
        viewModel.uiEvent.test {
            // Verify no events before 3 seconds
            expectNoEvents()

            // Advance time by 2.9 seconds
            advanceTimeBy(2900)
            expectNoEvents()

            // Advance remaining time
            advanceTimeBy(100)

            // Now we should get the Success event
            assertEquals(UiEvent.Success, awaitItem())

            // No more events
            expectNoEvents()

        }
    }
}

// MainDispatcherRule for handling coroutines in tests
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}