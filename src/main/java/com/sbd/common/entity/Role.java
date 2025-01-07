package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;


    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "role")
    private Set<EmployeeDetails> employees;

    @OneToMany(mappedBy = "role")
    private Set<EmployeeAttendance> attendances;
}
