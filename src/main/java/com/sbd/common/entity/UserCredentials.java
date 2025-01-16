package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "user_credentials")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeDetails employee;


}

