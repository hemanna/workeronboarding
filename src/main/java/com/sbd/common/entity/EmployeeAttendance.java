package com.sbd.common.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employee_attendance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeDetails employee;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "working_hours")
    private BigDecimal workingHours;

    @Column(name = "overtime")
    private BigDecimal overtime;

    @Column(name = "shift_details", nullable = false)
    private String shiftDetails;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "photo")
    private String photo;

    @Column(name = "approval_status", nullable = false)
    private String approvalStatus;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "leave_id", referencedColumnName = "id")
    private Leave leave;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmployeeAttendanceSession> sessions = new ArrayList<>();

}
