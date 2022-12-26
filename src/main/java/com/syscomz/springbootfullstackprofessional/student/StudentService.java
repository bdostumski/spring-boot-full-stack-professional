package com.syscomz.springbootfullstackprofessional.student;

import com.syscomz.springbootfullstackprofessional.student.exception.BadRequestException;
import com.syscomz.springbootfullstackprofessional.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class    StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        Boolean isEmailTaken = studentRepository.selectExistsEmail(student.getEmail());
        if (isEmailTaken)
            throw new BadRequestException(String.format("Student with email %s, already exists!", student.getEmail()));

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean isStudentNotExists = !studentRepository.existsById(studentId);
        if (isStudentNotExists)
            throw new StudentNotFoundException(String.format("Student with id %d, does not exists!", studentId));

        studentRepository.deleteById(studentId);
    }
}
