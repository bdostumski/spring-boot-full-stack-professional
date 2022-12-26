package com.syscomz.springbootfullstackprofessional.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// Testing Unit StudentRepositoryTest
// @SpringBootTest(classes = StudentRepositoryTest.class)
@DataJpaTest
class StudentRepositoryTest {
    // We should test only our own custom methods
    // Spring Data JPA methods are already tested for us

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenStudentEmailExists() {
        // given
        String email = "b.dostumski@gmail.com";
        Student student = new Student("Borislav", email, Gender.MALE);
        underTest.save(student);

        //when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenStudentEmailDoesNotExists() {
        // given
        String email = "b.dostumski@gmail.com";

        //when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}