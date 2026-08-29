package com.sbd.globalsetting.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.AttendanceSettingsJsonb;

public interface AttendanceSettingsControl {

    ApiResponse insertAttendanceSettings(
            AttendanceSettingsJsonb attendanceSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;
}
