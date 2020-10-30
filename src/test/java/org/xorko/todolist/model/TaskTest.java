package org.xorko.todolist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    private Task taskUnderTest;

    @BeforeEach
    void setUp() {
        taskUnderTest = new Task("name", LocalDate.of(2020, 1, 1));
    }

    @Test
    void testGetName() {
        // Setup

        // Run the test
        final String result = taskUnderTest.getName();

        // Verify the results
        assertThat(result).isEqualTo("name");
    }

    @Test
    void testIsDone() {
        // Setup

        // Run the test
        final boolean result = taskUnderTest.isDone();

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testGetDate() {
        // Setup

        // Run the test
        final LocalDate result = taskUnderTest.getDate();

        // Verify the results
        assertThat(result).isEqualTo(LocalDate.of(2020, 1, 1));
    }
}
