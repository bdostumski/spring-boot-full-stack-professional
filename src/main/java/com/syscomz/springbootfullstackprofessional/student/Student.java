package com.syscomz.springbootfullstackprofessional.student;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity // it is used by JPQL (Java Persistence Query Language)
@Table // takes the class Student and create table with name students, it's using camelCase naming convention
public class Student {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "student_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    @NotBlank // BE validation
    @Column(nullable = false) // database validations
    private String name;
    @Email // BE validation. can have custom regex.
    @Column(nullable = false, unique = true) // database validations
    private String email;
    @NotNull // BE validation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // database validations
    private Gender gender;

    public Student(String name, String email, Gender gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

}
