package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHolidayJsonb {

    private Integer id;

    private String holidayCalendar;

    private String holidayName;

    private LocalDate holidayDate;

    private String holidayType;

    private String reason;
}