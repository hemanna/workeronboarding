package com.sbd.globalsetting.common.entity;

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
public class CompanyHolidays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "holiday_calendar", nullable = false, length = 100)
    private String holidayCalendar;

    @Column(name = "holiday_name", nullable = false, length = 255)
    private String holidayName;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "holiday_type", nullable = false, length = 100)
    private String holidayType;

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;
}
