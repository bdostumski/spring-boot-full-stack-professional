package com.syscomz.springbootfullstackprofessional.student;

import com.syscomz.springbootfullstackprofessional.student.exception.BadRequestException;
import com.syscomz.springbootfullstackprofessional.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


// Testing Unit StudentServiceTest
class StudentServiceTest {

    @Mock // Mock already tested unit - we didn't need to test it again, so we mock it
    private StudentRepository studentRepository;
    private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // initialize all the @Mock 's in this class
        underTest = new StudentService(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close(); // close the resource after each test
    }

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();
        // then
        verify(studentRepository).findAll();
    }

    // @Disabled // disable test
    @Test
    void canAddStudent() {
        // given
        Student student = new Student("Borislav", "b.dostumski@gmail.com", Gender.MALE);

        // when
        underTest.addStudent(student);

        // then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student("Borislav", "b.dostumski@gmail.com", Gender.MALE);

        // Mock that the email of the student already exists
        // to get in the if the statement of the method addStudent(), and throw an exception
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(Boolean.TRUE);

        // when
        // then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("Student with email %s, already exists!", student.getEmail()));

        // checks that the student repository is never called to save any data in it
        verify(studentRepository, never()).save(any());
    }

    // @Disabled // disable test
    @Test
    void canDeleteStudent() {
        // given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(true);
        // when
        underTest.deleteStudent(id);

        // then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteStudentNotFound() {
        // given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining(String.format("Student with id %d, does not exists!", id));

        verify(studentRepository, never()).deleteById(any());
    }
}