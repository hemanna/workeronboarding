package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "company_holidays")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompanyHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    @Column(name = "reason")
    private String reason;

}
