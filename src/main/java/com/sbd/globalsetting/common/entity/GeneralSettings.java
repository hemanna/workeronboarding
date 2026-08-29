package com.sbd.globalsetting.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "general_settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GeneralSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "company_code", length = 100)
    private String companyCode;

    @Column(name = "organization_type", length = 100)
    private String organizationType;

    @Column(name = "currency", length = 50)
    private String currency;

    @Column(name = "time_zone", length = 100)
    private String timeZone;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "date_format", length = 50)
    private String dateFormat;

    @Column(name = "time_format", length = 50)
    private String timeFormat;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
