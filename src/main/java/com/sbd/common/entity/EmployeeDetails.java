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

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "aadhaar_number", unique = true)
    private String aadhaarNumber;

    @Column(name = "pan_card")
    private String panCard;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "emergency_number")
    private String emergencyNumber;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Lob
    @Column(name = "aadhaar_pic")
    private byte[] aadhaarPic;

    @Lob
    @Column(name = "pancard_pic")
    private byte[] pancardPic;

    @Column(name = "status")
    private String status;

    @Column(name = "approval_status")
    private String approvalStatus;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
}
