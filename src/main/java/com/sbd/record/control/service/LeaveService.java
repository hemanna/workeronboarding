package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.LeaveRepository;
import com.sbd.common.repository.LeaveTypeRepository;
import com.sbd.common.repository.DepartmentRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.LeaveControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class LeaveService implements LeaveControl {

    @Inject
    LeaveRepository leaveRepository;

    @Inject
    EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    LeaveTypeRepository leaveTypeRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public ApiResponse createLeaveRequest(ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("Processing leave request with RequestId: {}", requestId);

        EmployeeDTO employeeDTO = apiRequest.getData();
        EmployeeDTO.LeaveDTO leaveDTO = employeeDTO.getLeaveDTO();

        // Fetch employee details
        EmployeeDetails employee = employeeDetailsRepository.findById(leaveDTO.getEmployeeId());
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Fetch the leaveType
        LeaveType leaveType = leaveTypeRepository.findById(Long.valueOf(leaveDTO.getLeaveTypeId()));
        if (leaveType == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Leave type not found", requestId)
            );
        }

        // Fetch the department from employee
        Department dept = employee.getDepartment();
        if (dept == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Department not found for employee", requestId)
            );
        }

        // Convert DTO to Entity
        Leave leave = mapLeave(employee, leaveDTO, dept, leaveType);

        leaveRepository.persist(leave);

        log.info("Leave request successfully saved with ID: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave request submitted successfully", requestId)
        );
    }

    // Convert DTO to Entity
    private Leave mapLeave(EmployeeDetails employee, EmployeeDTO.LeaveDTO leaveDTO, Department department, LeaveType leaveType) {
        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
        leave.setStartDate(leaveDTO.getStartDate());
        leave.setEndDate(leaveDTO.getEndDate());
        leave.setDepartment(department);
        leave.setReason(leaveDTO.getReason());
        return leave;
    }
}
