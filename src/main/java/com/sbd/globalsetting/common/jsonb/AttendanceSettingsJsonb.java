package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSettingsJsonb {
    private Integer id;

    private BigDecimal workingHoursPerDay;

    private Integer graceTimeMinutes;

    private Integer lateMarkAfterMinutes;

    private BigDecimal maximumWorkingHours;

    private BigDecimal minimumHalfDayHours;

    private BigDecimal minimumFullDayHours;

    private Boolean allowMultipleCheckIn;

    private Boolean allowMultipleCheckOut;

    private Boolean enableGpsValidation;

    private Boolean enablePhotoCapture;

    private Boolean allowManualAttendance;

    private Boolean requireManagerApproval;
}
