package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.EmployeeDetailsMapper;
import com.sbd.common.mapper.LeaveMapper;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.EmployeeDetailsRequest;
import com.sbd.common.request.UserCredentialsDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.EmployeeRecordControl;
import io.netty.handler.codec.http.multipart.FileUpload;
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

    @Inject
    private UserCredentialsRepository userCredentialsRepository;




    @Transactional
    public ApiResponse createEmployeeDetails(EmployeeDetailsRequest employeeDetailsRequest, String requestId) throws BusinessException {


        // Fetch Role
        Role role = roleRepository.findByRoleId(employeeDetailsRequest.getRoleId());
        if (role == null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
            );
        }

        // Fetch Department
        Department department = departmentRepository.findById(Long.valueOf(employeeDetailsRequest.getDepartmentId()));
        if (department == null) {
            throw new BusinessException("Leave type not found for ID: " + employeeDetailsRequest.getDepartmentId());
        }

        // Create EmployeeDetails entity
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setEmployeeName(employeeDetailsRequest.getEmployeeName());
        employeeDetails.setGuardianName(employeeDetailsRequest.getGuardianName());
        employeeDetails.setAadhaarNumber(employeeDetailsRequest.getAadhaarNumber());
        employeeDetails.setPanCard(employeeDetailsRequest.getPanCard());
        employeeDetails.setDob(employeeDetailsRequest.getDob());
        employeeDetails.setGender(employeeDetailsRequest.getGender());
        employeeDetails.setPhoneNumber(employeeDetailsRequest.getPhoneNumber());
        employeeDetails.setEmergencyNumber(employeeDetailsRequest.getEmergencyNumber());
        employeeDetails.setNationality(employeeDetailsRequest.getNationality());
        employeeDetails.setBloodGroup(employeeDetailsRequest.getBloodGroup());
        employeeDetails.setAddressLine1(employeeDetailsRequest.getAddressLine1());
        employeeDetails.setAddressLine2(employeeDetailsRequest.getAddressLine2());
        employeeDetails.setRole(role);
        employeeDetails.setState(employeeDetailsRequest.getState());
        employeeDetails.setDistrict(employeeDetailsRequest.getDistrict());
        employeeDetails.setPostalCode(employeeDetailsRequest.getPostalCode());
        employeeDetails.setExperience(employeeDetailsRequest.getExperience());
        employeeDetails.setDateOfJoining(employeeDetailsRequest.getDateOfJoining());
        employeeDetails.setStatus(employeeDetailsRequest.getStatus());
        employeeDetails.setEmail(employeeDetailsRequest.getEmail());
        employeeDetails.setSkillType(employeeDetailsRequest.getSkillType());
        employeeDetails.setPassword("employee");
        employeeDetails.setApprovalStatus("Pending");
        employeeDetails.setDepartment(department);

        // Save Profile, Aadhaar, and Pancard Pics (if present)
        if (employeeDetailsRequest.getProfilePic() != null) {
            employeeDetails.setProfilePic(employeeDetailsRequest.getProfilePic());
        }
        if (employeeDetailsRequest.getAadhaarPic() != null) {
            employeeDetails.setAadhaarPic(employeeDetailsRequest.getAadhaarPic());
        }
        if (employeeDetailsRequest.getPancardPic() != null) {
            employeeDetails.setPancardPic(employeeDetailsRequest.getPancardPic());
        }


        // Persist EmployeeDetails entity
        employeeDetailsRepository.persist(employeeDetails);
        employeeDetailsRepository.flush();

        // Convert Entity to DTO using MapStruct
        EmployeeDTO.EmployeeDetailsDTO employeeDTO = EmployeeDetailsMapper.INSTANCE.toDTO(employeeDetails);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details created successfully", requestId)

        );

    }



//    @Override
//    @Transactional
//    public ApiResponse createEmployeeDetails(ApiRequest<EmployeeDTO> apiRequest, String requestId) {
//        log.info("Start creating employee details - RequestId: {}", requestId);
//
//        EmployeeDTO employeeDTO = apiRequest.getData();
//
//        // Validate RoleDTO presence before proceeding
//        if (employeeDTO.getRoleDTO() == null) {
//            log.error("RoleDTO is missing in the request - RequestId: {}", requestId);
//            return new ApiResponse(
//                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role information is required", requestId)
//            );
//        }
//
//        // Fetch Department
//        Department department = departmentRepository.findById(employeeDTO.getDepartmentDTO().getId());
//        if (department == null) {
//            return new ApiResponse(
//                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
//            );
//        }
//
//        // Fetch Role
//        Role role = roleRepository.findByRoleId(employeeDTO.getRoleDTO().getRoleId());
//        if (role == null) {
//            return new ApiResponse(
//                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
//            );
//        }
//
//        // Check if Aadhar number already exists
//        EmployeeDetails existingEmployee = employeeDetailsRepository.findByAadharNumber(employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
//        if (existingEmployee != null) {
//            log.error("Aadhar number already exists for RequestId: {} - Aadhar Number: {}", requestId, employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
//            return new ApiResponse(
//                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Aadhar number already exists", requestId)
//            );
//        }
//
//        // Create EmployeeDetails
//        EmployeeDetails employeeDetails = mapEmployeeDetails(employeeDTO, department, role);
//        employeeDetailsRepository.persist(employeeDetails);
//
//        // Create UserCredentials
//        UserCredentialsDTO userCredentialsDTO = employeeDTO.getUserCredentialsDTO();
//        if (userCredentialsDTO == null) {
//            log.error("UserCredentialsDTO is missing in the request - RequestId: {}", requestId);
//            return new ApiResponse(
//                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "User credentials information is required", requestId)
//            );
//        }
//
//        UserCredentials userCredentials = new UserCredentials();
//        userCredentials.setUsername(userCredentialsDTO.getUsername());
//        userCredentials.setPassword("employee");
//        userCredentials.setEmployee(employeeDetails);
//        userCredentialsRepository.persist(userCredentials);
//
//
//
//        log.info("End creating employee details - RequestId: {}", requestId);
//        return new ApiResponse(
//                new Status(Response.Status.OK.getStatusCode(), "Employee details successfully created", requestId)
//        );
//    }
//
//
//
//
//    private EmployeeDetails mapEmployeeDetails(EmployeeDTO employeeDTO, Department department, Role role) {
//        EmployeeDetails employeeDetails = new EmployeeDetails();
//        employeeDetails.setEmployeeName(employeeDTO.getEmployeeDetailsDTO().getEmployeeName());
//        employeeDetails.setGuardianName(employeeDTO.getEmployeeDetailsDTO().getGuardianName());
//        employeeDetails.setAadharNumber(employeeDTO.getEmployeeDetailsDTO().getAadharNumber());
//        employeeDetails.setPancard(employeeDTO.getEmployeeDetailsDTO().getPancard());
//        employeeDetails.setDob(employeeDTO.getEmployeeDetailsDTO().getDob());
//        employeeDetails.setGender(employeeDTO.getEmployeeDetailsDTO().getGender());
//        employeeDetails.setPhoneNumber(employeeDTO.getEmployeeDetailsDTO().getPhoneNumber());
//        employeeDetails.setEmergencyNumber(employeeDTO.getEmployeeDetailsDTO().getEmergencyNumber());
//        employeeDetails.setNationality(employeeDTO.getEmployeeDetailsDTO().getNationality());
//        employeeDetails.setBloodGroup(employeeDTO.getEmployeeDetailsDTO().getBloodGroup());
//        employeeDetails.setAddressLine1(employeeDTO.getEmployeeDetailsDTO().getAddressLine1());
//        employeeDetails.setAddressLine2(employeeDTO.getEmployeeDetailsDTO().getAddressLine2());
//        employeeDetails.setState(employeeDTO.getEmployeeDetailsDTO().getState());
//        employeeDetails.setDistrict(employeeDTO.getEmployeeDetailsDTO().getDistrict());
//        employeeDetails.setPostalCode(employeeDTO.getEmployeeDetailsDTO().getPostalCode());
//        employeeDetails.setExperience(employeeDTO.getEmployeeDetailsDTO().getExperience());
//        employeeDetails.setDateOfJoining(employeeDTO.getEmployeeDetailsDTO().getDateOfJoining());
//       // employeeDetails.setProfilePic(employeeDTO.getEmployeeDetailsDTO().getProfilePic());
//      //  employeeDetails.setAadharPic(employeeDTO.getEmployeeDetailsDTO().getAadharPic());
//      //  employeeDetails.setPancardPic(employeeDTO.getEmployeeDetailsDTO().getPancardPic());
//        employeeDetails.setStatus(employeeDTO.getEmployeeDetailsDTO().getStatus());
//        employeeDetails.setApprovalStatus("Pending");
//        employeeDetails.setDepartment(department);
//        employeeDetails.setRole(role);
//        return employeeDetails;
//    }
//
//
//    @Override
//    @Transactional
//    public ApiResponse updateEmployeeDetails(Integer employeeId, ApiRequest<EmployeeDTO> apiRequest, String requestId) {
//        log.info("Start updating employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
//
//        EmployeeDTO employeeDTO = apiRequest.getData();
//
//        // Fetch existing employee details
//        EmployeeDetails existingEmployee = employeeDetailsRepository.findById(employeeId);
//        if (existingEmployee == null) {
//            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
//            return new ApiResponse(
//                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
//            );
//        }
//
//        // Check for duplicate Aadhar number
//        String aadharNumber = employeeDTO.getEmployeeDetailsDTO().getAadharNumber();
//        if (aadharNumber != null && !aadharNumber.equals(existingEmployee.getAadharNumber())) {
//            EmployeeDetails existingAadharEmployee = employeeDetailsRepository.findByAadharNumber(aadharNumber);
//            if (existingAadharEmployee != null) {
//                log.error("Duplicate Aadhar Number - RequestId: {}, AadharNumber: {}", requestId, aadharNumber);
//                return new ApiResponse(
//                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Aadhar Number already exists", requestId)
//                );
//            }
//        }
//
//        // Validate and update department  from employeeDTO
//        if (employeeDTO.getEmployeeDetailsDTO().getDepartmentId() != null) {
//            Department department = departmentRepository.findById(employeeDTO.getEmployeeDetailsDTO().getDepartmentId());
//            if (department == null) {
//                log.error("Department not found - RequestId: {}, DepartmentId: {}", requestId, employeeDTO.getEmployeeDetailsDTO().getDepartmentId());
//                return new ApiResponse(
//                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Department not found", requestId)
//                );
//            }
//            existingEmployee.setDepartment(department);
//        }
//
//        // Validate and update role  from employeeDTO
//        if (employeeDTO.getEmployeeDetailsDTO().getRoleId() != null) {
//            Role role = roleRepository.findByRoleId(employeeDTO.getEmployeeDetailsDTO().getRoleId());
//            if (role == null) {
//                log.error("Role not found - RequestId: {}, RoleId: {}", requestId, employeeDTO.getEmployeeDetailsDTO().getRoleId());
//                return new ApiResponse(
//                        new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Role not found", requestId)
//                );
//            }
//            existingEmployee.setRole(role);
//        }
//
//        // Update other employee details from the DTO
//        EmployeeDTO.EmployeeDetailsDTO detailsDTO = employeeDTO.getEmployeeDetailsDTO();
//        existingEmployee.setEmployeeName(detailsDTO.getEmployeeName());
//        existingEmployee.setGuardianName(detailsDTO.getGuardianName());
//        existingEmployee.setAadharNumber(detailsDTO.getAadharNumber());
//        existingEmployee.setPancard(detailsDTO.getPancard());
//        existingEmployee.setDob(detailsDTO.getDob());
//        existingEmployee.setGender(detailsDTO.getGender());
//        existingEmployee.setPhoneNumber(detailsDTO.getPhoneNumber());
//        existingEmployee.setEmergencyNumber(detailsDTO.getEmergencyNumber());
//        existingEmployee.setNationality(detailsDTO.getNationality());
//        existingEmployee.setBloodGroup(detailsDTO.getBloodGroup());
//        existingEmployee.setAddressLine1(detailsDTO.getAddressLine1());
//        existingEmployee.setAddressLine2(detailsDTO.getAddressLine2());
//        existingEmployee.setState(detailsDTO.getState());
//        existingEmployee.setDistrict(detailsDTO.getDistrict());
//        existingEmployee.setPostalCode(detailsDTO.getPostalCode());
//        existingEmployee.setExperience(detailsDTO.getExperience());
//        existingEmployee.setDateOfJoining(detailsDTO.getDateOfJoining());
//        existingEmployee.setStatus(detailsDTO.getStatus());
//        existingEmployee.setApprovalStatus(detailsDTO.getApprovalStatus());
//
//
//        try {
//            employeeDetailsRepository.persist(existingEmployee);
//            log.info("Successfully updated employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
//        } catch (Exception e) {
//            log.error("Error while updating employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId, e);
//            return new ApiResponse(
//                    new Status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error updating employee details", requestId)
//            );
//        }
//
//        log.info("End updating employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
//        return new ApiResponse(
//                new Status(Response.Status.OK.getStatusCode(), "Employee details successfully updated", requestId)
//
//        );
//    }
//
    @Override
    @Transactional
    public ApiResponse updateApprovalStatus(Integer employeeId, String approvalStatus, String requestId) {
        log.info("Start updating approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);

        // Validate the approval status
        if (approvalStatus == null || (!"Approved".equalsIgnoreCase(approvalStatus) && !"Rejected".equalsIgnoreCase(approvalStatus))) {
            log.error("Invalid approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid approval status", requestId)
            );
        }

        // Fetch the existing employee details
        EmployeeDetails existingEmployee = employeeDetailsRepository.findById(employeeId);
        if (existingEmployee == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Update the approval status of the employee
        existingEmployee.setApprovalStatus(approvalStatus);
        try {
            employeeDetailsRepository.persist(existingEmployee);
            log.info("Successfully updated approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
        } catch (Exception e) {
            log.error("Error while updating approval status - RequestId: {}, EmployeeId: {}", requestId, employeeId, e);
            return new ApiResponse(
                    new Status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error updating approval status", requestId)
            );
        }

        log.info("End updating approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "" + approvalStatus, requestId)
        );
    }
//
//
//
    @Override
    @Transactional
    public ApiResponse fetchEmployeeById(Long employeeId, String requestId) {
        log.info("Start fetching employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        // Fetch the employee details by ID
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Use the new method from Employee Mapper
        EmployeeDTO.EmployeeDetailsDTO employees = EmployeeDetailsMapper.INSTANCE.toDTO(employee);

        // Return the employee details
        log.info("End fetching employee details - RequestId: {}, EmployeeId: {}", requestId, employeeId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Employee details fetched successfully", requestId),
                employees
        );
    }


    @Override
    public ApiResponse fetchAllEmployee(String requestId, ApiRequest<EmployeeDetailsRequest> apiRequest) throws BusinessException {
        log.info("Start fetching employees - RequestId: {}", requestId);

        // Validate request structure
        apiRequest.isValid(requestId);

        List<EmployeeDetails> employees;

        // Use employeeName instead of getValue()
        if (isValidData(apiRequest)) {
            employees = employeeDetailsRepository.listByName(
                    apiRequest.getData().getEmployeeName(), apiRequest.getPagination());
        } else {
            employees = employeeDetailsRepository.listAll(apiRequest.getPagination());
        }

        if (employees.isEmpty()) {
            log.info("No employees found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NO_CONTENT.getStatusCode(),
                            "No employee records found", requestId)
            );
        }

        log.info("End fetching employees - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(),
                        "Employee details fetched successfully", requestId),
                EmployeeDetailsMapper.INSTANCE.toDTOList(employees)
        );
    }

    @Override
    @Transactional
    public ApiResponse deleteEmployee(String requestId, Long employeeId) throws BusinessException {
        log.info("Request ID: {} | Deleting employee with ID: {}", requestId, employeeId);

        // Fetch the employee from the database
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);

        // If employee does not exist, return a NOT FOUND response
        if (employee == null) {
            log.warn("Request ID: {} | Employee with ID {} not found", requestId, employeeId);
            throw new BusinessException(
                    Response.Status.NOT_FOUND.getStatusCode(),
                    requestId,
                    "Employee not found"
            );
        }

        // Delete the employee
        employeeDetailsRepository.delete(employee);
        log.info("Request ID: {} | Employee with ID {} deleted successfully", requestId, employeeId);

        // Return success response
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(),
                        "Employee deleted successfully", requestId)
        );

    }

    // Corrected isValidData method
    private boolean isValidData(ApiRequest<EmployeeDetailsRequest> apiRequest) {
        return apiRequest.getData() != null && apiRequest.getData().getEmployeeName() != null;

    }

//
//    @Override
//    @Transactional
//    public ApiResponse fetchPendingApprovals(String requestId) {
//        log.info("Start fetching employees with pending approvals - RequestId: {}", requestId);
//
//        // Fetch all employees with Pending approval status
//        List<EmployeeDetails> pendingApprovals = employeeDetailsRepository.find("approvalStatus", "Pending").list();
//        if (pendingApprovals == null || pendingApprovals.isEmpty()) {
//            log.warn("No pending approvals found - RequestId: {}", requestId);
//            return new ApiResponse(
//                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No pending approvals found", requestId)
//            );
//        }
//
//
//        log.info("End fetching employees with pending approvals - RequestId: {}", requestId);
//        return new ApiResponse(
//                new Status(Response.Status.OK.getStatusCode(), "Pending approvals fetched successfully", requestId),
//                pendingApprovals
//        );
//    }
}
