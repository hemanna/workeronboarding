package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class EmployeeRecordService implements EmployeeRecordControl {

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
    public ApiResponse createEmployeeDetails(ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("Start creating employee details - RequestId: {}", requestId);

        EmployeeDTO employeeDTO = apiRequest.getData();

        // Fetch Department
        Department department = departmentRepository.findById(employeeDTO.getDepartmentDTO().getId());
        if (department == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
            );
        }

        // Fetch Role
        Role role = roleRepository.findByRoleId(employeeDTO.getRoleDTO().getRoleId());
        if (role == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
            );
        }

     // Check if Aadhar number already exists
        EmployeeDetails existingEmployee = employeeDetailsRepository.findByAadharNumber(employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
        if (existingEmployee != null) {
            log.error("Aadhar number already exists for RequestId: {} - Aadhar Number: {}", requestId, employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Aadhar number already exists", requestId)
            );
        }

        // Create EmployeeDetails
        EmployeeDetails employeeDetails = mapEmployeeDetails(employeeDTO, department, role);
        employeeDetailsRepository.persist(employeeDetails);

        log.info("End creating employee details - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details successfully created", requestId),
                employeeDetails
        );
    }

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

    private EmployeeDetails mapEmployeeDetails(EmployeeDTO employeeDTO, Department department, Role role) {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setEmployeeName(employeeDTO.getEmployeeDetailsDTO().getEmployeeName());
        employeeDetails.setGuardianName(employeeDTO.getEmployeeDetailsDTO().getGuardianName());
        employeeDetails.setAadharNumber(employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
        employeeDetails.setPancard(employeeDTO.getEmployeeDetailsDTO().getPancard());
        employeeDetails.setDob(employeeDTO.getEmployeeDetailsDTO().getDob());
        employeeDetails.setGender(employeeDTO.getEmployeeDetailsDTO().getGender());
        employeeDetails.setPhoneNumber(employeeDTO.getEmployeeDetailsDTO().getPhoneNumber());
        employeeDetails.setEmergencyNumber(employeeDTO.getEmployeeDetailsDTO().getEmergencyNumber());
        employeeDetails.setNationality(employeeDTO.getEmployeeDetailsDTO().getNationality());
        employeeDetails.setBloodGroup(employeeDTO.getEmployeeDetailsDTO().getBloodGroup());
        employeeDetails.setAddressLine1(employeeDTO.getEmployeeDetailsDTO().getAddressLine1());
        employeeDetails.setAddressLine2(employeeDTO.getEmployeeDetailsDTO().getAddressLine2());
        employeeDetails.setState(employeeDTO.getEmployeeDetailsDTO().getState());
        employeeDetails.setDistrict(employeeDTO.getEmployeeDetailsDTO().getDistrict());
        employeeDetails.setPostalCode(employeeDTO.getEmployeeDetailsDTO().getPostalCode());
        employeeDetails.setExperience(employeeDTO.getEmployeeDetailsDTO().getExperience());
        employeeDetails.setDateOfJoining(employeeDTO.getEmployeeDetailsDTO().getDateOfJoining());
        employeeDetails.setProfilePic(employeeDTO.getEmployeeDetailsDTO().getProfilePic());
        employeeDetails.setAadharPic(employeeDTO.getEmployeeDetailsDTO().getAadharPic());
        employeeDetails.setPancardPic(employeeDTO.getEmployeeDetailsDTO().getPancardPic());
        employeeDetails.setStatus(employeeDTO.getEmployeeDetailsDTO().getStatus());
        employeeDetails.setApprovalStatus(employeeDTO.getEmployeeDetailsDTO().getApprovalStatus());
        employeeDetails.setDepartment(department);
        employeeDetails.setRole(role);
        return employeeDetails;
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

}
