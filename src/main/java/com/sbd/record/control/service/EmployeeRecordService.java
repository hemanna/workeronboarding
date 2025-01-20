package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeRecordControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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


    @Override
    @Transactional
    public ApiResponse updateEmployeeDetails(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("Start updating employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        EmployeeDTO employeeDTO = apiRequest.getData();

        // Fetch existing employee details
        EmployeeDetails existingEmployee = employeeDetailsRepository.findById(employeeId);
        if (existingEmployee == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Validate and update department
        if (employeeDTO.getDepartmentDTO() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentDTO().getId());
            if (department == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
                );
            }
            existingEmployee.setDepartment(department);
        }

        // Validate and update role if provided
        if (employeeDTO.getRoleDTO() != null) {
            Role role = roleRepository.findByRoleId(employeeDTO.getRoleDTO().getRoleId());
            if (role == null) {
                return new ApiResponse(
                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
                );
            }
            existingEmployee.setRole(role);
        }

        // Update employee details fields from the DTO
        if (employeeDTO.getEmployeeDetailsDTO() != null) {
            EmployeeDTO.EmployeeDetailsDTO detailsDTO = employeeDTO.getEmployeeDetailsDTO();
            existingEmployee.setEmployeeName(detailsDTO.getEmployeeName());
            existingEmployee.setGuardianName(detailsDTO.getGuardianName());
            existingEmployee.setAadharNumber(detailsDTO.getAadharNumber());
            existingEmployee.setPancard(detailsDTO.getPancard());
            existingEmployee.setDob(detailsDTO.getDob());
            existingEmployee.setGender(detailsDTO.getGender());
            existingEmployee.setPhoneNumber(detailsDTO.getPhoneNumber());
            existingEmployee.setEmergencyNumber(detailsDTO.getEmergencyNumber());
            existingEmployee.setNationality(detailsDTO.getNationality());
            existingEmployee.setBloodGroup(detailsDTO.getBloodGroup());
            existingEmployee.setAddressLine1(detailsDTO.getAddressLine1());
            existingEmployee.setAddressLine2(detailsDTO.getAddressLine2());
            existingEmployee.setState(detailsDTO.getState());
            existingEmployee.setDistrict(detailsDTO.getDistrict());
            existingEmployee.setPostalCode(detailsDTO.getPostalCode());
            existingEmployee.setExperience(detailsDTO.getExperience());
            existingEmployee.setDateOfJoining(detailsDTO.getDateOfJoining());
            existingEmployee.setProfilePic(detailsDTO.getProfilePic());
            existingEmployee.setAadharPic(detailsDTO.getAadharPic());
            existingEmployee.setPancardPic(detailsDTO.getPancardPic());
            existingEmployee.setStatus(detailsDTO.getStatus());
            existingEmployee.setApprovalStatus(detailsDTO.getApprovalStatus());
        }

        // Persist the updated entity
        employeeDetailsRepository.persist(existingEmployee);

        log.info("End updating employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details successfully updated", requestId),
                existingEmployee
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchEmployeeById(Long employeeId, String requestId) {
        log.info("Start fetching employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        // Fetch the employee details by ID
        EmployeeDetails employeeDetails = employeeDetailsRepository.findById(employeeId);
        if (employeeDetails == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Return the employee details
        log.info("End fetching employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details fetched successfully", requestId),
                employeeDetails
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAllEmployees(String requestId) {
        log.info("Start fetching all employee details - RequestId: {}", requestId);

        // Fetch all employee details as a list
        List<EmployeeDetails> employees = employeeDetailsRepository.findAll().list();
        if (employees == null || employees.isEmpty()) {
            log.warn("No employee records found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No employee records found", requestId)
            );
        }

        // Return the list of employee details
        log.info("End fetching all employee details - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details fetched successfully", requestId),
                employees
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchPendingApprovals(String requestId) {
        log.info("Start fetching employees with pending approvals - RequestId: {}", requestId);

        // Fetch all employees with "Pending" approval status
        List<EmployeeDetails> pendingApprovals = employeeDetailsRepository.find("approvalStatus", "Pending").list();
        if (pendingApprovals == null || pendingApprovals.isEmpty()) {
            log.warn("No pending approvals found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No pending approvals found", requestId)
            );
        }

        // Return the list of employees with pending approvals
        log.info("End fetching employees with pending approvals - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Pending approvals fetched successfully", requestId),
                pendingApprovals
        );
    }



}
