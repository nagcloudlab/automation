package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    // Arrange
    private Calculator calculator = new Calculator();

    // Act & Assert
    @Test
    public void testAdd() {
        int actual = calculator.add(2, 3);
        int expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtract() {
        int actual = calculator.subtract(5, 3);
        int expected = 2;
        assertEquals(expected, actual);
    }

}
