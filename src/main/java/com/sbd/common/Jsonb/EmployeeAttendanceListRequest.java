package com.sbd.common.Jsonb;

import com.sbd.common.request.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAttendanceListRequest {
    private Pagination pagination;

}
