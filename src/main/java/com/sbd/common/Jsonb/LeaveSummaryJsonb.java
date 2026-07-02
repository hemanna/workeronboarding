package com.sbd.common.Jsonb;

import lombok.Data;

@Data
public class LeaveSummaryJsonb {
    private Long totalLeaveRequests;

    private Long approvedLeaves;

    private Long pendingLeaves;

    private Long rejectedLeaves;

    private Long regularization;
}
