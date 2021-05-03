package com.example.todolist

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName

internal class MainActivityMock : MainActivity() {

}

@DisplayName("Test base function of this application")
class ToDoTest {

    @Test
    fun test_test() {
        assertEquals(1, 1)
    }
}