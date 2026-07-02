package com.sbd.common.Jsonb;

import lombok.Data;

@Data
public class AttendanceSummaryJsonb {
    private Long presentToday;

    private Long absentToday;

    private Long lateArrivals;

    private Long timeShortage;

    private Double attendancePercentage;
}
