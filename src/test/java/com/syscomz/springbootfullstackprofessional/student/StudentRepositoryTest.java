package com.syscomz.springbootfullstackprofessional.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = StudentRepositoryTest.class)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void itShouldCheckIfStudentExistsByEmail() {
        // given
        String email = "b.dostumski@gmail.com";
        Student student = new Student("Borislav", email, Gender.MALE);
        underTest.save(student);

        //when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isTrue();
    }
}