package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "company_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_company_email", columnNames = "email")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompanySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "company_logo", length = 500)
    private String companyLogo;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "gst_number", length = 15)
    private String gstNumber;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(name = "company_address", columnDefinition = "TEXT")
    private String companyAddress;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
