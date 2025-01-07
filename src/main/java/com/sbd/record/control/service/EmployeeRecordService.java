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

        // Check if employeeDTO is null
        if (employeeDTO == null) {
            log.error("EmployeeDTO is null for RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Employee details are missing", requestId)
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

        //  Role
        Role role = null;
        if (employeeDTO.getRoleDTO() != null) {
            role = roleRepository.findByRoleName(employeeDTO.getRoleDTO().getRoleName());

            if (role == null) {
                role = new Role();
                role.setRoleName(employeeDTO.getRoleDTO().getRoleName());
                role.setRoleId(employeeDTO.getRoleDTO().getRoleId());
                role.setCreatedBy(employeeDTO.getRoleDTO().getCreatedBy());
                role.setCreationDate(employeeDTO.getRoleDTO().getCreationDate());
                role.setStatus(employeeDTO.getRoleDTO().getStatus());
                roleRepository.persist(role);
            }
        }

        //  Department
        Department department = departmentRepository.findByName(employeeDTO.getDepartmentDTO().getName());
        if (department == null) {
            department = new Department();
            department.setName(employeeDTO.getDepartmentDTO().getName());
            departmentRepository.persist(department);
        }

        //  EmployeeDetails
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
        employeeDetails.setDepartment(department);  // Set the department here
        employeeDetailsRepository.persist(employeeDetails);

        //  LeaveType
        LeaveType leaveType = leaveTypeRepository.findByType(employeeDTO.getLeaveTypeDTO().getType());
        if (leaveType == null) {
            leaveType = new LeaveType();
            leaveType.setType(employeeDTO.getLeaveTypeDTO().getType());
            leaveTypeRepository.persist(leaveType);
        }

        //  Leave
        Leave leave = new Leave();
        leave.setLeaveType(leaveType);
        leave.setStartDate(employeeDTO.getLeaveDTO().getStartDate());
        leave.setEndDate(employeeDTO.getLeaveDTO().getEndDate());
        leave.setDepartment(department);
        leave.setReason(employeeDTO.getLeaveDTO().getReason());
        leave.setEmployee(employeeDetails);
        leaveRepository.persist(leave);

        //  EmployeeAttendance
        if (employeeDTO.getEmployeeAttendanceDTO() != null) {
            EmployeeAttendance attendance = new EmployeeAttendance();
            attendance.setEmployee(employeeDetails);
            attendance.setRole(role);
            attendance.setLeave(leave);
            attendance.setDate(employeeDTO.getEmployeeAttendanceDTO().getDate());
            attendance.setCheckinTime(employeeDTO.getEmployeeAttendanceDTO().getCheckinTime());
            attendance.setCheckoutTime(employeeDTO.getEmployeeAttendanceDTO().getCheckoutTime());
            attendance.setWorkingHours(employeeDTO.getEmployeeAttendanceDTO().getWorkingHours());
            attendance.setOvertime(employeeDTO.getEmployeeAttendanceDTO().getOvertime());
            attendance.setShiftDetails(employeeDTO.getEmployeeAttendanceDTO().getShiftDetails());
            attendance.setLocation(employeeDTO.getEmployeeAttendanceDTO().getLocation());
            attendance.setPhoto(employeeDTO.getEmployeeAttendanceDTO().getPhoto());
            attendance.setApprovalStatus(employeeDTO.getEmployeeAttendanceDTO().getApprovalStatus());
            attendance.setStatus(employeeDTO.getEmployeeAttendanceDTO().getStatus());

            //converting departmentId to Long
            Long departmentId = Long.valueOf(employeeDTO.getEmployeeDetailsDTO().getDepartmentId());
            Department attendanceDepartment = departmentRepository.findById(departmentId);
            if (attendanceDepartment != null) {
                attendance.setDepartment(attendanceDepartment);
            } else {
                log.error("Department not found for departmentId: {} - RequestId: {}", departmentId, requestId);
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
                );
            }

            employeeAttendanceRepository.persist(attendance);
        }

        log.info("End creating employee details - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details successfully created", requestId),
                employeeDetails
        );
    }
}
