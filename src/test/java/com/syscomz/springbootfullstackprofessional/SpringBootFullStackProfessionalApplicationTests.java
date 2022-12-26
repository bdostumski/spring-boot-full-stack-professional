package com.syscomz.springbootfullstackprofessional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SpringBootFullStackProfessionalApplicationTests {

    Calculator underTest = new Calculator();

    // BDD: behavior-driven development testing style
    // User Journey Story and Given, When and Then
    // Given - the state of the system that will receive the behavior/action
    // When - the behavior/action that happens and causes the result in the end
    // Then - the result caused by the behavior in the state
    @Test
//    @Disabled // Stop this test to be started
    void itShouldAddTwoNumbers() {
        // given
        int numberOne = 20;
        int numberTwo = 30;

        // when
        int result = underTest.add(numberOne, numberTwo);

        // then
        int expected = 50;
        assertThat(result).isEqualTo(expected);
    }

    class Calculator {
        int add(int a, int b) {
            return a + b;
        }
    }

}
