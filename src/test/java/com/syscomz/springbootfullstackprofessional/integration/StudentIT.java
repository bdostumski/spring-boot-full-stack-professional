package com.syscomz.springbootfullstackprofessional.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.syscomz.springbootfullstackprofessional.student.Gender;
import com.syscomz.springbootfullstackprofessional.student.Student;
import com.syscomz.springbootfullstackprofessional.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// IT means integration test, which is important because the maven Failsafe plugin will look for it.
// We need to run integration tests against the database that we're going to run when we deploy to real users. In our case, it is Postgres.
@SpringBootTest
// When we add @TestPropertiesSource we should make maven clean
// After that we should add spring.datasource.driver-class-name=org.postgresql.Driver in aplication-it.properties file, and it is good practice to add it and into our application.properties file and also into application-dev.properties file
@TestPropertySource(locations = "classpath:application-it.properties")
// setup integration tests to use application-it.properties
@AutoConfigureMockMvc // enable us to @Autowired the MockMvc
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @Test
    void canRegisterNewStudent() throws Exception {
        // given
        String name = String.format("%s %s", faker.name().firstName(), faker.name().lastName());
        String email = String.format("%s@syscomz.com", name.replace(" ", "").toLowerCase());

        Student student = new Student(name, email, Gender.MALE);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students)
                .usingRecursiveComparison()
                .ignoringFields("id") // ignore id because it is randomly generated for us
                .comparingOnlyFields("name", "email", "gender");
    }

    @Test
    void canDeleteStudent() throws Exception {
        // given
        String name = String.format("%s %s", faker.name().firstName(), faker.name().lastName());
        String email = String.format("%s@syscomz.com", name.replace(" ", "").toLowerCase());

        Student student = new Student(name, email, Gender.MALE);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        MvcResult getStudentsResult = mockMvc.perform(get("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getStudentsResult
                .getResponse()
                .getContentAsString();

        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<List<Student>>() {}
        );

        long id = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                .map(Student::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("student with email: " + email + " not found"));

        // when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/students/" + id));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(id);
        assertThat(exists).isFalse();
    }


}
