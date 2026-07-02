package com.sbd.record.control.service;

import com.sbd.common.Jsonb.*;
import com.sbd.common.entity.*;
import com.sbd.common.enums.EmployeeAttendanceActionEnum;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.EmployeeAttendanceMapper;
import com.sbd.common.mapper.EmployeeDetailsMapper;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.Pagination;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeAttendanceControl;
import com.sbd.record.control.EmployeeRecordControl;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class EmployeeAttendanceService implements EmployeeAttendanceControl {

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @Inject
    private AttendanceLockRepository attendanceLockRepository;
    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    private LeaveRepository leaveRepository;

    @Inject
    private LeaveTypeRepository leaveTypeRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RegularizedAttendanceRepository regularizedAttendanceRepository;

    private final EmployeeAttendanceMapper attendanceMapper=EmployeeAttendanceMapper.INSTANCE;

    @Override
    @Transactional
    public ApiResponse createAttendance(ApiRequest<EmployeeAttendanceDTO> apiRequest, String requestId) {
        log.info("Start creating employee attendance - RequestId: {}", requestId);

        EmployeeAttendanceDTO attendanceDTO = apiRequest.getData();

        // Validate that departmentId is not null
        if (attendanceDTO.getDepartmentId() == null) {
            log.error("Department ID is missing in request - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department ID is required", requestId)
            );
        }

        // Fetch Department
        Department department = departmentRepository.findById(attendanceDTO.getDepartmentId());
        if (department == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
            );
        }

        // Validate that roleId is not null
        if (attendanceDTO.getRoleId() == null) {
            log.error("Role ID is missing in request - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role ID is required", requestId)
            );
        }

        // Fetch Role
        Role role = roleRepository.findByRoleId(attendanceDTO.getRoleId());
        if (role == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
            );
        }

        // Fetch Leave
        Leave leave = null;
        if (attendanceDTO.getLeaveId() != null) {
            leave = leaveRepository.findById(attendanceDTO.getLeaveId());
            if (leave == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Leave not found", requestId)
                );
            }
        }

        // Fetch Employee
        EmployeeDetails details = employeeDetailsRepository.findById(attendanceDTO.getEmployeeId());
        if (details == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Employee ID not found", requestId)
            );
        }

        // Check if the attendance for this date already exists
        EmployeeAttendance existingAttendance = employeeAttendanceRepository.findByEmployeeAndDate(
                attendanceDTO.getEmployeeId(), attendanceDTO.getDate());
        if (existingAttendance != null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Attendance already exists for this date", requestId)
            );
        }

        // Create EmployeeAttendance
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(details);
        employeeAttendance.setDepartment(department);
        employeeAttendance.setRole(role);
        employeeAttendance.setLeave(leave);
        employeeAttendance.setDate(attendanceDTO.getDate());
        employeeAttendance.setWorkingHours(attendanceDTO.getWorkingHours());
        employeeAttendance.setOvertime(attendanceDTO.getOvertime());
        employeeAttendance.setShiftDetails(attendanceDTO.getShiftDetails());
        employeeAttendance.setLocation(attendanceDTO.getLocation());
        employeeAttendance.setPhoto(attendanceDTO.getPhoto());
        employeeAttendance.setApprovalStatus(attendanceDTO.getApprovalStatus());
        employeeAttendance.setStatus(attendanceDTO.getStatus());

        // Map Sessions
        if (attendanceDTO.getSessions() != null && !attendanceDTO.getSessions().isEmpty()) {
            for (EmployeeAttendanceSessionDTO sessionDTO : attendanceDTO.getSessions()) {
                EmployeeAttendanceSession session = new EmployeeAttendanceSession();
                session.setAttendance(employeeAttendance);
                session.setCheckIn(sessionDTO.getCheckIn());
                session.setCheckOut(sessionDTO.getCheckOut());
                session.setLocation(sessionDTO.getLocation());

                employeeAttendance.getSessions().add(session);
            }
        }

        // Persist (sessions cascade automatically)
        employeeAttendanceRepository.persist(employeeAttendance);



        log.info("End creating employee attendance - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance successfully created", requestId)

        );
    }

    @Override
    @Transactional
    public ApiResponse updateAttendance(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("Start updating employee attendance - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        EmployeeDTO employeeDTO = apiRequest.getData();

        // Fetch existing attendance record
        EmployeeAttendance employeeAttendance = employeeAttendanceRepository.findByEmployeeAndDate(employeeId, employeeDTO.getEmployeeAttendanceDTO().getDate());
        if (employeeAttendance == null) {
            log.error("Attendance record not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        // Validate and update Department
        if (employeeDTO.getDepartmentDTO() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentDTO().getId());
            if (department == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
                );
            }
            employeeAttendance.setDepartment(department);
        }

        // Validate and update Role
        if (employeeDTO.getRoleDTO() != null) {
            Role role = roleRepository.findByRoleId(employeeDTO.getRoleDTO().getRoleId());
            if (role == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
                );
            }
            employeeAttendance.setRole(role);
        }

        // Validate and update Leave
        if (employeeDTO.getEmployeeAttendanceDTO().getLeaveId() != null) {
            Leave leave = leaveRepository.findById(employeeDTO.getEmployeeAttendanceDTO().getLeaveId());
            if (leave == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Leave not found", requestId)
                );
            }
            employeeAttendance.setLeave(leave);
        }

        // Update attendance details
        EmployeeDTO.EmployeeAttendanceDTO attendanceDTO = employeeDTO.getEmployeeAttendanceDTO();
        employeeAttendance.setDate(attendanceDTO.getDate());
//        employeeAttendance.setCheckinTime(attendanceDTO.getCheckinTime());
//        employeeAttendance.setCheckoutTime(attendanceDTO.getCheckoutTime());
        employeeAttendance.setWorkingHours(attendanceDTO.getWorkingHours());
        employeeAttendance.setOvertime(attendanceDTO.getOvertime());
        employeeAttendance.setShiftDetails(attendanceDTO.getShiftDetails());
        employeeAttendance.setLocation(attendanceDTO.getLocation());
        employeeAttendance.setPhoto(attendanceDTO.getPhoto());
        employeeAttendance.setApprovalStatus(attendanceDTO.getApprovalStatus());
        employeeAttendance.setStatus(attendanceDTO.getStatus());
        employeeAttendanceRepository.persist(employeeAttendance);

        log.info("End updating employee attendance - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance successfully updated", requestId),
                employeeAttendance
        );
    }


//    private EmployeeAttendance mapEmployeeAttendance(EmployeeDTO.EmployeeAttendanceDTO attendanceDTO,
//                                                     EmployeeDetails employeeDetails, Department department,
//                                                     Role role, Leave leave) {
//        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
//        employeeAttendance.setEmployee(employeeDetails);
//        employeeAttendance.setRole(role);
//        employeeAttendance.setLeave(leave);
//        employeeAttendance.setDate(attendanceDTO.getDate());
////        employeeAttendance.setCheckinTime(attendanceDTO.getCheckinTime());
////        employeeAttendance.setCheckoutTime(attendanceDTO.getCheckoutTime());
//        employeeAttendance.setWorkingHours(attendanceDTO.getWorkingHours());
//        employeeAttendance.setOvertime(attendanceDTO.getOvertime());
//        employeeAttendance.setShiftDetails(attendanceDTO.getShiftDetails());
//        employeeAttendance.setLocation(attendanceDTO.getLocation());
//        employeeAttendance.setPhoto(attendanceDTO.getPhoto());
//        employeeAttendance.setApprovalStatus(attendanceDTO.getApprovalStatus());
//        employeeAttendance.setStatus(attendanceDTO.getStatus());
//        employeeAttendance.setDepartment(department);
//        return employeeAttendance;
//    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceById(Long attendanceId, String requestId) {
        log.info("Start fetching attendance details - RequestId: {}, AttendanceId: {}", requestId, attendanceId);

        // Fetch the attendance  by ID
        EmployeeAttendance attendance = employeeAttendanceRepository.findById(attendanceId);
        if (attendance == null) {
            log.error("Attendance record not found - RequestId: {}, AttendanceId: {}", requestId, attendanceId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        log.info("End fetching attendance details - RequestId: {}, AttendanceId: {}", requestId, attendanceId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance record fetched successfully", requestId),
                attendance
        );
    }


    @Override
    @Transactional
    public ApiResponse fetchAllAttendance(
            String correlationId,
            ApiRequest<EmployeeAttendanceListRequest> apiRequest)
            throws BusinessException {

        log.info( LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.Attendance_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue());

        Pagination pagination = apiRequest.getPagination();

        List<EmployeeAttendance> attendanceList =
                employeeAttendanceRepository.listAllAttendance(
                        pagination.getPageIndex(),
                        pagination.getPageSize()
                );

        if (attendanceList == null || attendanceList.isEmpty()) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        List<EmployeeAttendanceDTO> responseList =
                EmployeeAttendanceMapper.INSTANCE.toDTOList(attendanceList);

        log.info( LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.Attendance_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue() );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                responseList
        );
    }



    //    @Override
//    public ApiResponse listAssets(String correlationId, ApiRequest<AssetListRequest> apiRequest) throws BusinessException {
//
//        log.info( LogEnum.ACTIVITY.getValue(),
//                correlationId,
//                AssetActionEnum.ASSET_LIST.getValue(),
//                LogEnum.LogMessage.STARTED.getValue() );
//
//        // Validate the data
//        AssetListRequest assetListRequest = apiRequest.getData();
//        if (assetListRequest == null) {
//            throw new BusinessException(
//                    Response.Status.BAD_REQUEST.getStatusCode(),
//                    correlationId,
//                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
//            );
//        }
//
//        // Validate pagination
//        Pagination pagination = assetListRequest.getPagination();
//        if (pagination == null) {
//            throw new BusinessException(
//                    Response.Status.BAD_REQUEST.getStatusCode(),
//                    correlationId,
//                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
//            );
//        }
//
//        String type = assetListRequest.getType();
//
//        // Fetch assets based on type filter
//        List<Asset> assets;
//        if (type != null && !type.equalsIgnoreCase("ALL")) {
//
//            assets = assetRepository.listByType(type, pagination.getPageIndex(), pagination.getPageSize());
//
//            // LIKE search
//            if (assets.isEmpty()) {
//                assets = assetRepository.listByTypeLike(type, pagination.getPageIndex(), pagination.getPageSize());
//            }
//
//        } else {
//            // Fetch all assets
//            assets = assetRepository.listAllAssets(pagination.getPageIndex(), pagination.getPageSize());
//        }
//
//        if (assets == null || assets.isEmpty()) {
//            return new ApiResponse(
//                    new Status(
//                            Response.Status.NO_CONTENT.getStatusCode(),
//                            StatusCodeEnum.NO_CONTENT.getValue(),
//                            correlationId
//                    )
//            );
//        }
//
//        // Convert to DTOs
//        List<AssetDTO> assetDTOs = AssetMapper.INSTANCE.toDTOList(assets);
//
//        log.info( LogEnum.ACTIVITY.getValue(),
//                correlationId,
//                AssetActionEnum.ASSET_LIST.getValue(),
//                LogEnum.LogMessage.ENDED.getValue() );
//
//        return new ApiResponse(
//                new Status(
//                        Response.Status.OK.getStatusCode(),
//                        StatusCodeEnum.SUCCESS.getValue(),
//                        correlationId
//                ),
//                assetDTOs
//        );
//    }
    @Override
    @Transactional
    public ApiResponse fetchAttendanceByDate(String date, String requestId) {
        log.info("Start fetching employee attendance by date - RequestId: {}, Date: {}", requestId, date);

        // Convert the date string to LocalDate
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (Exception e) {
            log.error("Invalid date format - RequestId: {}, Date: {}", requestId, date);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid date format", requestId)
            );
        }

        // Fetch attendance records for the specified date
        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.findByDate(parsedDate);

        if (attendanceList == null || attendanceList.isEmpty()) {
            log.warn("No attendance records found for date: {} - RequestId: {}", date, requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found for the given date", requestId)
            );
        }

        log.info("End fetching employee attendance by date - RequestId: {}, Date: {}", requestId, date);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceList
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceByMonth(String month, String requestId) {
        log.info("Start fetching employee attendance for month - RequestId: {}, Month: {}", requestId, month);

        LocalDate startDate;
        try {
            startDate = LocalDate.parse(month + "-01");
        } catch (Exception e) {
            log.error("Invalid month format - RequestId: {}, Month: {}", requestId, month);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid month format", requestId)
            );
        }

        // Calculate the end date of the month
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.findByMonth(startDate, endDate);

        if (attendanceList == null || attendanceList.isEmpty()) {
            log.warn("No attendance records found for the month: {} - RequestId: {}", month, requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found for the given month", requestId)
            );
        }

        log.info("End fetching employee attendance for month - RequestId: {}, Month: {}", requestId, month);

        // Return the list of attendance records for the month
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceList
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendancePending(String requestId) {
        log.info("Start fetching pending attendance approvals - RequestId: {}", requestId);

        // Fetch all attendance with "Pending" approval status
        List<EmployeeAttendance> pendingApprovals = employeeAttendanceRepository.find("approvalStatus", "Pending").list();
        if (pendingApprovals == null || pendingApprovals.isEmpty()) {
            log.warn("No pending approvals found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No pending approvals found", requestId)
            );
        }
        // Return the list of employees with pending approvals
        log.info("End fetching attendance with pending approvals - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Pending approvals fetched successfully", requestId),
                pendingApprovals
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceByRange(String fromDate, String toDate, String requestId) {
        log.info("Start fetching employee attendance by date range - RequestId: {}, From: {}, To: {}", requestId, fromDate, toDate);

        //  the input dates
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(fromDate);
            endDate = LocalDate.parse(toDate);
        } catch (Exception e) {
            log.error("Invalid date format - RequestId: {}, From: {}, To: {}", requestId, fromDate, toDate);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid date format", requestId)
            );
        }

        // Validate that fromDate is before or equal to toDate
        if (startDate.isAfter(endDate)) {
            log.error("Invalid date range - From date is after To date - RequestId: {}, From: {}, To: {}", requestId, fromDate, toDate);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid date range: fromDate should be before or equal to toDate", requestId)
            );
        }

        // Fetch attendance records within the given range
        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.findByDateRange(startDate, endDate);

        // If no attendance records found
        if (attendanceList == null || attendanceList.isEmpty()) {
            log.warn("No attendance records found between {} and {} - RequestId: {}", fromDate, toDate, requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found for the given date range", requestId)
            );
        }

        log.info("End fetching employee attendance by date range - RequestId: {}, From: {}, To: {}", requestId, fromDate, toDate);

        // Return the list of attendance records
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceList
        );
    }

    @Override
    @Transactional
    public ApiResponse deleteAttendance(Long id, String requestId) {
        log.info("Start deleting employee attendance - RequestId: {}, AttendanceId: {}", requestId, id);

        // Fetch the attendance record by ID
        EmployeeAttendance attendance = employeeAttendanceRepository.findById(id);
        if (attendance == null) {
            log.error("Attendance record not found - RequestId: {}, AttendanceId: {}", requestId, id);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        // Delete the attendance record
        employeeAttendanceRepository.delete(attendance);

        log.info("Successfully deleted employee attendance - RequestId: {}, AttendanceId: {}", requestId, id);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance successfully deleted", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchStatusById(Long employeeId, String requestId) {
        log.info("Start fetch employee attendance - RequestId: {}", requestId);

        Optional<Map<String, Object>> attendanceData = employeeAttendanceRepository.getAttendanceForCurrentMonth(employeeId);

        EmployeeAttendanceResponseDTO responseDTO = attendanceData.map(attendanceMapper::toDto)
                .orElse(new EmployeeAttendanceResponseDTO(employeeId, 0, 0, 0, 0, 0));

        log.info("End fetch employee attendance - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance fetched successfully", requestId),
                responseDTO
        );
    }


    @Override
    @Transactional
    public ApiResponse fetchAttendanceByYearAndMonth(String year, String month, String requestId) {
        log.info("Start fetching attendance - RequestId: {}, Year: {}, Month: {}", requestId, year, month);

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(year + "-01-01");
            endDate = LocalDate.parse(year + "-12-31");

            if (month != null) {
                startDate = LocalDate.parse(year + "-" + month + "-01");
                endDate = startDate.plusMonths(1).minusDays(1);
            }
        } catch (Exception e) {
            log.error("Invalid date format - RequestId: {}, Year: {}, Month: {}", requestId, year, month);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid year or month format", requestId)
            );
        }

        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.findByMonth(startDate, endDate);

        if (attendanceList == null || attendanceList.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found", requestId)
            );
        }


        List<EmployeeDTO.EmployeeAttendanceDTO> attendanceDTOList = attendanceList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());


        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceDTOList
        );
    }


    private EmployeeDTO.EmployeeAttendanceDTO mapToDTO(EmployeeAttendance attendance) {
        EmployeeDTO.EmployeeAttendanceDTO dto = new EmployeeDTO.EmployeeAttendanceDTO();

        dto.setId(attendance.getId());
        dto.setEmployeeId(attendance.getEmployee().getId());
        dto.setDepartmentId(attendance.getDepartment() != null ? attendance.getDepartment().getId() : null);
        dto.setRoleId(attendance.getRole() != null ? attendance.getRole().getRoleId() : null);
        dto.setLeaveId(attendance.getLeave() != null ? attendance.getLeave().getId() : null);

        dto.setDate(attendance.getDate());
//        dto.setCheckinTime(attendance.getCheckinTime());
//        dto.setCheckoutTime(attendance.getCheckoutTime());
        dto.setWorkingHours(attendance.getWorkingHours());
        dto.setOvertime(attendance.getOvertime());
        dto.setShiftDetails(attendance.getShiftDetails());
        dto.setLocation(attendance.getLocation());
        dto.setPhoto(attendance.getPhoto());
        dto.setApprovalStatus(attendance.getApprovalStatus());
        dto.setStatus(attendance.getStatus());

        return dto;
    }

    @Override
    @Transactional
    public ApiResponse createRegularization(Integer employeeId, ApiRequest<EmployeeAttendanceRegularizationJsonb> apiRequest, String requestId) {
        log.info("Start createRegularization - RequestId: {}", requestId);

        EmployeeAttendanceRegularizationJsonb dto = apiRequest.getData();

        EmployeeAttendance attendance = employeeAttendanceRepository.findById(dto.getAttendanceId());
        if (attendance == null) {
            log.error("Attendance record not found - RequestId: {}, EmployeeId: {}", requestId, dto.getEmployeeId());
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        // Use employeeId from URL
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Create Regularization Request
        EmployeeAttendanceRegularization reg = new EmployeeAttendanceRegularization();
        reg.setAttendance(attendance);
        reg.setEmployee(employee);
        reg.setDate(dto.getDate());
        reg.setCurrentStatus(dto.getCurrentStatus());
        reg.setNewCheckin(dto.getNewCheckin());
        reg.setNewCheckout(dto.getNewCheckout());
        reg.setNewLocation(dto.getNewLocation());
        reg.setReason(dto.getReason());
        reg.setStatus("PENDING");


        regularizedAttendanceRepository.persist(reg);

        log.info("Regularization request created - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Regularization request submitted successfully", requestId)
        );
    }



    @Override
    @Transactional
    public ApiResponse checkIn(Integer employeeId, ApiRequest<EmployeeAttendanceSessionDTO> apiRequest, String requestId) {
        log.info("Start Check-In - RequestId: {}", requestId);

        EmployeeAttendanceSessionDTO sessionDTO = apiRequest.getData();

        // 1. Validate employee
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Explicitly fetch role & department like in createAttendance
        Department department = employee.getDepartment() != null
                ? departmentRepository.findById(employee.getDepartment().getId())
                : null;
        Role role = employee.getRole() != null
                ? roleRepository.findByRoleId(employee.getRole().getId())
                : null;

        if (department == null || role == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(),
                            "Employee is missing department or role", requestId)
            );
        }

        //Find or create today's attendance
        LocalDate today = LocalDate.now();
        EmployeeAttendance attendance = employeeAttendanceRepository.findByEmployeeAndDate(employeeId, today);

        if (attendance == null) {
            attendance = new EmployeeAttendance();
            attendance.setEmployee(employee);
            attendance.setDepartment(department);
            attendance.setRole(role);
            attendance.setDate(today);
            attendance.setStatus("Present");
            attendance.setApprovalStatus("Pending");
            attendance.setShiftDetails("General");
            attendance.setLocation("Unknown");
            employeeAttendanceRepository.persist(attendance);
        }

        // 3. Create session
        EmployeeAttendanceSession session = new EmployeeAttendanceSession();
        session.setAttendance(attendance);
        session.setCheckIn(sessionDTO.getCheckIn() != null ? sessionDTO.getCheckIn() : LocalTime.now());
        session.setLocation(sessionDTO.getLocation() != null ? sessionDTO.getLocation() : "Unknown");

        attendance.getSessions().add(session);

        log.info("End Check-In - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Check-In successful", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse checkOut(Integer employeeId,
                                ApiRequest<EmployeeAttendanceSessionDTO> apiRequest,
                                String requestId) {

        log.info("Start Check-Out - RequestId: {}", requestId);

        EmployeeAttendanceSessionDTO sessionDTO = apiRequest.getData();

        //Validate employee
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(),
                            "Employee not found", requestId)
            );
        }

        //Find today's attendance
        LocalDate today = LocalDate.now();
        EmployeeAttendance attendance =
                employeeAttendanceRepository.findByEmployeeAndDate(employeeId, today);

        if (attendance == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(),
                            "No attendance record found for today", requestId)
            );
        }

        // Find the last session where checkOut IS NULL → current active session
        EmployeeAttendanceSession session = attendance.getSessions()
                .stream()
                .filter(s -> s.getCheckOut() == null)
                .reduce((first, second) -> second)   // last session
                .orElse(null);

        if (session == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(),
                            "No active check-in session found", requestId)
            );
        }

        // Set checkout time
        session.setCheckOut(
                sessionDTO.getCheckOut() != null
                        ? sessionDTO.getCheckOut()
                        : LocalTime.now()
        );

        // CALCULATE TOTAL WORKING HOURS
        Double totalHours =
                employeeAttendanceRepository.calculateWorkingHours(
                        attendance.getId()
                );

        // UPDATE employee_attendance TABLE
        employeeAttendanceRepository.updateWorkingHours(
                attendance.getId(),
                totalHours
        );

        log.info(
                "Working hours updated: {} | attendanceId: {}",
                totalHours,
                attendance.getId()
        );

        log.info("End Check-Out - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(),
                        "Check-Out successful", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse lockAttendance(ApiRequest<AttendanceLockJsonb> apiRequest, String correlationId) throws BusinessException {
        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.ATTENDANCE_LOCK.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        AttendanceLockJsonb request = apiRequest.getData();
        Integer month = request.getMonth();
        Integer year = request.getYear();

        log.info("correlationId={} | Locking attendance for month={} year={}",
                correlationId, month, year);

        // Check attendance exists
        Long attendanceCount = attendanceLockRepository.countAttendance(month, year);

        if (attendanceCount == 0) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    ),
                    "No attendance found for given month/year"
            );
        }

        // Check already locked
        Long lockedCount = attendanceLockRepository.countLocked(month, year);

        if (lockedCount > 0) {
            return new ApiResponse(
                    new Status(
                            Response.Status.CONFLICT.getStatusCode(),
                            "ALREADY_LOCKED",
                            correlationId
                    ),
                    "Attendance already locked for this month/year"
            );
        }

        // Insert lock
        int lockedRows = attendanceLockRepository.insertLock(month, year);

        // Update attendance status
        int updatedRows = attendanceLockRepository.updateAttendanceStatus(month, year);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.ATTENDANCE_LOCK.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                "Attendance Locked Successfully | Employees: " + lockedRows +
                        " | Updated Records: " + updatedRows
        );
    }

    @Override
    @Transactional
    public ApiResponse submitAttendance(
            ApiRequest<AttendanceSubmitJsonb> apiRequest,
            String correlationId)
            throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.SUBMIT_ATTENDANCE.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        AttendanceSubmitJsonb request =
                apiRequest.getData();

        if (request == null ||
                request.getAttendances() == null ||
                request.getAttendances().isEmpty()) {

            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    ),
                    "No attendance data found"
            );
        }

        LocalDate attendanceDate = LocalDate.now();

        int savedCount = 0;

        for (AttendanceMarkingJsonb item :
                request.getAttendances()) {

            EmployeeAttendance existing =
                    employeeAttendanceRepository.findByEmployeeAndDate(
                            item.getEmployeeId(),
                            attendanceDate
                    );

            if (existing != null) {
                continue;
            }

            EmployeeDetails employee =
                    employeeDetailsRepository.findById(
                            item.getEmployeeId()
                    );

            if (employee == null) {
                continue;
            }

            EmployeeAttendance attendance =
                    new EmployeeAttendance();

            attendance.setEmployee(employee);

            attendance.setDepartment(
                    employee.getDepartment()
            );

            attendance.setRole(
                    employee.getRole()
            );

            attendance.setDate(
                    attendanceDate
            );

            attendance.setStatus(
                    item.getStatus()
            );

            attendance.setApprovalStatus(
                    "PENDING"
            );

            attendance.setShiftDetails(
                    "GENERAL"
            );

            attendance.setLocation(
                    "OFFICE"
            );

            employeeAttendanceRepository.saveAttendance(
                    attendance
            );

            savedCount++;
        }

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.SUBMIT_ATTENDANCE.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                "Attendance Submitted Successfully. Records Saved : "
                        + savedCount
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceSummary(
            String correlationId)
            throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                "ATTENDANCE SUMMARY",
                LogEnum.LogMessage.STARTED.getValue()
        );

        LocalDate today = LocalDate.now();

        AttendanceSummaryJsonb response =
                new AttendanceSummaryJsonb();

        response.setPresentToday(
                employeeAttendanceRepository
                        .getPresentToday(today)
        );

        response.setAbsentToday(
                employeeAttendanceRepository
                        .getAbsentToday(today)
        );

        response.setLateArrivals(
                employeeAttendanceRepository
                        .getLateArrivals(today)
        );

        response.setTimeShortage(
                employeeAttendanceRepository
                        .getTimeShortage(today)
        );

        response.setAttendancePercentage(
                employeeAttendanceRepository
                        .getAttendancePercentage(today)
        );

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                "ATTENDANCE SUMMARY",
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(

                new Status(

                        Response.Status.OK.getStatusCode(),

                        StatusCodeEnum.SUCCESS.getValue(),

                        correlationId
                ),

                response
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchMonthlyAttendanceTrend(
            ApiRequest<AttendanceTrendRequestJsonb> apiRequest,
            String correlationId)
            throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                "MONTHLY ATTENDANCE TREND",
                LogEnum.LogMessage.STARTED.getValue()
        );

        AttendanceTrendRequestJsonb request =
                apiRequest.getData();

        List<Object[]> result =
                employeeAttendanceRepository
                        .fetchMonthlyAttendanceTrend(
                                request.getYear(),
                                request.getMonth()
                        );

        if (result == null || result.isEmpty()) {

            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        List<AttendanceTrendJsonb> trendList =
                new ArrayList<>();

        for (Object[] row : result) {

            AttendanceTrendJsonb trend =
                    new AttendanceTrendJsonb();

            trend.setDay(
                    ((Number) row[0]).intValue()
            );

            trend.setAttendancePercentage(
                    ((Number) row[1]).doubleValue()
            );

            trendList.add(trend);
        }

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                "MONTHLY ATTENDANCE TREND",
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                trendList
        );
    }

    @Override
    @Transactional
    public ApiResponse updateRegularizationStatus(Integer regularizationId, String newStatus, String requestId) {
        log.info("Start updating regularization status - RequestId: {}, RegularizationId: {}, NewStatus: {}",
                requestId, regularizationId, newStatus);

        // Validate status
        if (newStatus == null ||
                (!"PENDING".equalsIgnoreCase(newStatus) &&
                        !"APPROVED".equalsIgnoreCase(newStatus) &&
                        !"REJECTED".equalsIgnoreCase(newStatus))) {
            log.error("Invalid status - RequestId: {}, RegularizationId: {}, Status: {}", requestId, regularizationId, newStatus);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid status value", requestId)
            );
        }

        // Fetch the regularization record
        EmployeeAttendanceRegularization reg = regularizedAttendanceRepository.findByRegularizationId(regularizationId);
        if (reg == null) {
            log.error("Regularization request not found - RequestId: {}, RegularizationId: {}", requestId, regularizationId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Regularization request not found", requestId)
            );
        }

        // Prevent changing status if already APPROVED or REJECTED
        if ("APPROVED".equalsIgnoreCase(reg.getStatus()) || "REJECTED".equalsIgnoreCase(reg.getStatus())) {
            log.warn("Cannot change status of already finalized request - RequestId: {}, RegularizationId: {}, CurrentStatus: {}",
                    requestId, regularizationId, reg.getStatus());
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(),
                            "Cannot change status of already approved/rejected request", requestId)
            );
        }

        // Update the status and approvedAt timestamp
        reg.setStatus(newStatus.toUpperCase());
        if ("APPROVED".equalsIgnoreCase(newStatus) || "REJECTED".equalsIgnoreCase(newStatus)) {
            reg.setApprovedAt(LocalDateTime.now());
            // Optionally: reg.setApprovedBy(currentUserId);
        }

        // No need to call persist() for an existing entity; Panache tracks changes automatically
        log.info("Successfully updated regularization status - RequestId: {}, RegularizationId: {}, NewStatus: {}",
                requestId, regularizationId, newStatus);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Regularization status updated to " + newStatus, requestId)
        );
    }

    @Transactional
    public ApiResponse fetchAllRegularizationAttendance(String requestId) {
        log.info("Start fetching all Regularization Attendance records - RequestId: {}", requestId);

        List<EmployeeAttendanceRegularization> regs =
                regularizedAttendanceRepository.listAllRegularizationAttendance();

        List<EmployeeAttendanceRegularizationResponseDto> dtos = regs.stream().map(reg -> {
            EmployeeAttendanceRegularizationResponseDto dto = new EmployeeAttendanceRegularizationResponseDto();
            dto.setId(reg.getId());
            dto.setAttendanceId(reg.getAttendance().getId());
            dto.setEmployeeId(reg.getEmployee().getId());
            dto.setEmployeeName(reg.getEmployee().getEmployeeName());
            dto.setStatus(reg.getStatus());
            dto.setDate(reg.getDate());
            dto.setCurrentStatus(reg.getCurrentStatus());
            dto.setNewCheckin(reg.getNewCheckin());
            dto.setNewCheckout(reg.getNewCheckout());
            dto.setNewLocation(reg.getNewLocation());
            dto.setReason(reg.getReason());
            return dto;
        }).collect(Collectors.toList());

        log.info("End fetching all Regularization Attendance records - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Fetched regularizations successfully ", requestId),
        dtos);
    }

    @Override
    public ApiResponse fetchOvertimeAttendance(
            String correlationId,
            ApiRequest<EmployeeAttendanceListRequest> apiRequest)
            throws BusinessException {
        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.OVERTIME_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        Pagination pagination = apiRequest.getData().getPagination();

        List<EmployeeAttendance> attendanceList =
                employeeAttendanceRepository.listOvertimeForCurrentMonth(
                        pagination.getPageIndex(),
                        pagination.getPageSize()
                );

        if (attendanceList == null || attendanceList.isEmpty()) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        List<EmployeeAttendanceDTO> responseList =
                EmployeeAttendanceMapper.INSTANCE.toDTOList(attendanceList);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                EmployeeAttendanceActionEnum.OVERTIME_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                responseList
        );

    }


}
















