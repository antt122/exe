package com.example.demo.entity;

import com.example.demo.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

     String username;
     String password;
     String firstname;
     String lastname;
     LocalDate dob;
     Status status;

        @ManyToMany()
     Set<Role> roles;


}