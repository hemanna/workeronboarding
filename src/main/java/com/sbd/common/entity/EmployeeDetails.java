package com.sbd.common.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "aadhar_number", nullable = false, unique = true)
    private String aadharNumber;

    @Column(name = "pancard")
    private String pancard;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "emergency_number")
    private String emergencyNumber;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "profile_pic")
    private String profilePic;

    @Column(name = "aadhar_pic")
    private String aadharPic;

    @Column(name = "pancard_pic")
    private String pancardPic;

    @Column(name = "status")
    private String status;

    @Column(name = "approval_status")
    private String approvalStatus;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;
}
