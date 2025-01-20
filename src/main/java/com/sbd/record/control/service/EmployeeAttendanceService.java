package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeAttendanceControl;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
@Slf4j
public class EmployeeAttendanceService implements EmployeeAttendanceControl {

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private EmployeeAttendanceRepository employeeAttendanceRepository;

    @Inject
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    private LeaveRepository leaveRepository;

    @Inject
    private LeaveTypeRepository leaveTypeRepository;

    @Inject
    private RoleRepository roleRepository;


    @Override
    @Transactional
    public ApiResponse createAttendance(ApiRequest<EmployeeDTO.EmployeeAttendanceDTO> apiRequest, String requestId) {
        log.info("Start creating employee attendance - RequestId: {}", requestId);

        EmployeeDTO.EmployeeAttendanceDTO attendanceDTO = apiRequest.getData();

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
        EmployeeAttendance employeeAttendance = mapEmployeeAttendance(attendanceDTO, details, department, role, leave);
        employeeAttendanceRepository.persist(employeeAttendance);

        log.info("End creating employee attendance - RequestId: {}", requestId);

        // Return response with the created employee attendance
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance successfully created", requestId),
                employeeAttendance
        );
    }

    @Override
    @Transactional
    public ApiResponse updateAttendance(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("Start updating employee attendance - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        EmployeeDTO employeeDTO = apiRequest.getData();

        // Fetch existing attendance record for the employee and date
        EmployeeAttendance employeeAttendance = employeeAttendanceRepository.findByEmployeeAndDate(employeeId, employeeDTO.getEmployeeAttendanceDTO().getDate());
        if (employeeAttendance == null) {
            log.error("Attendance record not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        // Validate and update Department if provided
        if (employeeDTO.getDepartmentDTO() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentDTO().getId());
            if (department == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
                );
            }
            employeeAttendance.setDepartment(department);
        }

        // Validate and update Role if provided
        if (employeeDTO.getRoleDTO() != null) {
            Role role = roleRepository.findByRoleId(employeeDTO.getRoleDTO().getRoleId());
            if (role == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
                );
            }
            employeeAttendance.setRole(role);
        }

        // Validate and update Leave if provided
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
        employeeAttendance.setCheckinTime(attendanceDTO.getCheckinTime());
        employeeAttendance.setCheckoutTime(attendanceDTO.getCheckoutTime());
        employeeAttendance.setWorkingHours(attendanceDTO.getWorkingHours());
        employeeAttendance.setOvertime(attendanceDTO.getOvertime());
        employeeAttendance.setShiftDetails(attendanceDTO.getShiftDetails());
        employeeAttendance.setLocation(attendanceDTO.getLocation());
        employeeAttendance.setPhoto(attendanceDTO.getPhoto());
        employeeAttendance.setApprovalStatus(attendanceDTO.getApprovalStatus());
        employeeAttendance.setStatus(attendanceDTO.getStatus());

        // Persist updated attendance record
        employeeAttendanceRepository.persist(employeeAttendance);

        log.info("End updating employee attendance - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee attendance successfully updated", requestId),
                employeeAttendance
        );
    }


    private EmployeeAttendance mapEmployeeAttendance(EmployeeDTO.EmployeeAttendanceDTO attendanceDTO,
                                                     EmployeeDetails employeeDetails, Department department,
                                                     Role role, Leave leave) {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        employeeAttendance.setEmployee(employeeDetails);
        employeeAttendance.setRole(role);
        employeeAttendance.setLeave(leave);
        employeeAttendance.setDate(attendanceDTO.getDate());
        employeeAttendance.setCheckinTime(attendanceDTO.getCheckinTime());
        employeeAttendance.setCheckoutTime(attendanceDTO.getCheckoutTime());
        employeeAttendance.setWorkingHours(attendanceDTO.getWorkingHours());
        employeeAttendance.setOvertime(attendanceDTO.getOvertime());
        employeeAttendance.setShiftDetails(attendanceDTO.getShiftDetails());
        employeeAttendance.setLocation(attendanceDTO.getLocation());
        employeeAttendance.setPhoto(attendanceDTO.getPhoto());
        employeeAttendance.setApprovalStatus(attendanceDTO.getApprovalStatus());
        employeeAttendance.setStatus(attendanceDTO.getStatus());
        employeeAttendance.setDepartment(department);
        return employeeAttendance;
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceById(Long attendanceId, String requestId) {
        log.info("Start fetching attendance details - RequestId: {}, AttendanceId: {}", requestId, attendanceId);

        // Fetch the attendance record by ID
        EmployeeAttendance attendance = employeeAttendanceRepository.findById(attendanceId);
        if (attendance == null) {
            log.error("Attendance record not found - RequestId: {}, AttendanceId: {}", requestId, attendanceId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Attendance record not found", requestId)
            );
        }

        // Return the attendance details
        log.info("End fetching attendance details - RequestId: {}, AttendanceId: {}", requestId, attendanceId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance record fetched successfully", requestId),
                attendance
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAllAttendance(String requestId) {
        log.info("Start fetching all attendance records - RequestId: {}", requestId);

        // Fetch all attendance records
        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.listAll();
        if (attendanceList == null || attendanceList.isEmpty()) {
            log.warn("No attendance records found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found", requestId)
            );
        }


        log.info("End fetching all attendance records - RequestId: {}", requestId);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceList
        );
    }

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

        // If no attendance records found
        if (attendanceList == null || attendanceList.isEmpty()) {
            log.warn("No attendance records found for date: {} - RequestId: {}", date, requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No attendance records found for the given date", requestId)
            );
        }

        log.info("End fetching employee attendance by date - RequestId: {}, Date: {}", requestId, date);

        // Return the list of attendance records
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Attendance records fetched successfully", requestId),
                attendanceList
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAttendanceByMonth(String month, String requestId) {
        log.info("Start fetching employee attendance for month - RequestId: {}, Month: {}", requestId, month);

        // Parse the input month (expected format: yyyy-MM)
        LocalDate startDate;
        try {
            // Parse the input string into LocalDate (representing the first day of the month)
            startDate = LocalDate.parse(month + "-01"); // Adding "-01" to represent the first day
        } catch (Exception e) {
            log.error("Invalid month format - RequestId: {}, Month: {}", requestId, month);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid month format", requestId)
            );
        }

        // Calculate the end date of the month (the last day of the month)
        LocalDate endDate = startDate.plusMonths(1).minusDays(1); // Subtract one day to get the last day of the month

        // Fetch attendance records for the specified month
        List<EmployeeAttendance> attendanceList = employeeAttendanceRepository.findByMonth(startDate, endDate);

        // If no attendance records are found
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



}
