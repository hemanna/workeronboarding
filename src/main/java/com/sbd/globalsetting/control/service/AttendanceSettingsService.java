package com.sbd.globalsetting.control.service;

import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.globalsetting.common.entity.AttendanceSettings;
import com.sbd.globalsetting.common.enums.AttendanceSettingsActionEnum;
import com.sbd.globalsetting.common.jsonb.AttendanceSettingsJsonb;
import com.sbd.globalsetting.common.repository.AttendanceSettingsRepository;
import com.sbd.globalsetting.control.AttendanceSettingsControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class AttendanceSettingsService
        implements AttendanceSettingsControl {

    @Inject
    AttendanceSettingsRepository attendanceSettingsRepository;

    @Override
    @Transactional
    public ApiResponse insertAttendanceSettings(
            AttendanceSettingsJsonb attendanceSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AttendanceSettingsActionEnum
                        .INSERT_ATTENDANCE_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        log.info("Request Id : {} | Attendance Settings Request : {}",
                correlationId,
                attendanceSettingsJsonb
        );

        validateRequest(attendanceSettingsJsonb);

        AttendanceSettings attendanceSettings =
                new AttendanceSettings();

        attendanceSettings.setWorkingHoursPerDay(
                attendanceSettingsJsonb.getWorkingHoursPerDay()
        );

        attendanceSettings.setGraceTimeMinutes(
                attendanceSettingsJsonb.getGraceTimeMinutes()
        );

        attendanceSettings.setLateMarkAfterMinutes(
                attendanceSettingsJsonb.getLateMarkAfterMinutes()
        );

        attendanceSettings.setMaximumWorkingHours(
                attendanceSettingsJsonb.getMaximumWorkingHours()
        );

        attendanceSettings.setMinimumHalfDayHours(
                attendanceSettingsJsonb.getMinimumHalfDayHours()
        );

        attendanceSettings.setMinimumFullDayHours(
                attendanceSettingsJsonb.getMinimumFullDayHours()
        );

        /*
         * JSONB Boolean
         *      ↓
         * Database TINYINT
         *
         * true  -> 1
         * false -> 0
         */
        attendanceSettings.setAllowMultipleCheckIn(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getAllowMultipleCheckIn()
                )
        );

        attendanceSettings.setAllowMultipleCheckOut(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getAllowMultipleCheckOut()
                )
        );

        attendanceSettings.setEnableGpsValidation(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getEnableGpsValidation()
                )
        );

        attendanceSettings.setEnablePhotoCapture(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getEnablePhotoCapture()
                )
        );

        attendanceSettings.setAllowManualAttendance(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getAllowManualAttendance()
                )
        );

        attendanceSettings.setRequireManagerApproval(
                booleanToByte(
                        attendanceSettingsJsonb
                                .getRequireManagerApproval()
                )
        );

        attendanceSettings.setCreatedAt(
                LocalDateTime.now()
        );

        attendanceSettings.setUpdatedAt(
                LocalDateTime.now()
        );

        attendanceSettingsRepository.persist(attendanceSettings);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AttendanceSettingsActionEnum
                        .INSERT_ATTENDANCE_SETTINGS
                        .getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }

    /**
     * Convert frontend Boolean into MySQL TINYINT.
     *
     * true  = 1
     * false = 0
     */
    private Byte booleanToByte(Boolean value) {

        return Boolean.TRUE.equals(value)
                ? (byte) 1
                : (byte) 0;
    }

    /**
     * Basic Attendance Settings validation.
     */
    private void validateRequest(
            AttendanceSettingsJsonb request
    ) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "Attendance Settings request cannot be null"
            );
        }

        if (request.getWorkingHoursPerDay() == null) {

            throw new IllegalArgumentException(
                    "Working Hours Per Day is required"
            );
        }

        if (request.getWorkingHoursPerDay().signum() <= 0) {

            throw new IllegalArgumentException(
                    "Working Hours Per Day must be greater than zero"
            );
        }

        if (request.getGraceTimeMinutes() == null) {

            throw new IllegalArgumentException(
                    "Grace Time Minutes is required"
            );
        }

        if (request.getGraceTimeMinutes() < 0) {

            throw new IllegalArgumentException(
                    "Grace Time Minutes cannot be negative"
            );
        }

        if (request.getLateMarkAfterMinutes() == null) {

            throw new IllegalArgumentException(
                    "Late Mark After Minutes is required"
            );
        }

        if (request.getLateMarkAfterMinutes() < 0) {

            throw new IllegalArgumentException(
                    "Late Mark After Minutes cannot be negative"
            );
        }

        if (request.getMaximumWorkingHours() == null) {

            throw new IllegalArgumentException(
                    "Maximum Working Hours is required"
            );
        }

        if (request.getMinimumHalfDayHours() == null) {

            throw new IllegalArgumentException(
                    "Minimum Half Day Hours is required"
            );
        }

        if (request.getMinimumFullDayHours() == null) {

            throw new IllegalArgumentException(
                    "Minimum Full Day Hours is required"
            );
        }

        if (request.getMinimumHalfDayHours()
                .compareTo(request.getMinimumFullDayHours()) > 0) {

            throw new IllegalArgumentException(
                    "Minimum Half Day Hours cannot be greater than Minimum Full Day Hours"
            );
        }

        if (request.getMinimumFullDayHours()
                .compareTo(request.getMaximumWorkingHours()) > 0) {

            throw new IllegalArgumentException(
                    "Minimum Full Day Hours cannot be greater than Maximum Working Hours"
            );
        }
    }
}
