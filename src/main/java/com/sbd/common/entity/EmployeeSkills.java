package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_skills")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeDetails employee;

    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false)
    private Skill skill;
}
