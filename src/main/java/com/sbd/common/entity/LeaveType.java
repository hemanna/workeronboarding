package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "leave_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "leaveType")
    private Set<Leave> leaves;
}
