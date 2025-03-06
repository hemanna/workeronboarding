package com.sbd.record.control.service;

import com.sbd.common.entity.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.LeaveMapper;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.LeaveRepository;
import com.sbd.common.repository.LeaveTypeRepository;
import com.sbd.common.repository.DepartmentRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.LeaveRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.LeaveControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public ApiResponse<EmployeeDTO.LeaveDTO> fetchLeaveById(Long leaveId, String requestId) {
        log.info("Start fetching leave details - RequestId: {}, LeaveId: {}", requestId, leaveId);

        // Fetch the leave details by Id
        Leave leave = leaveRepository.findById(leaveId);
        if (leave == null) {
            log.error("Leave not found - RequestId: {}, LeaveId: {}", requestId, leaveId);
            return new ApiResponse<>(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Leave not found", requestId)
            );
        }

        // Leave entity to LeaveDTO
        EmployeeDTO.LeaveDTO leaveDTO = mapToLeaveDTO(leave);

        // Return the leave details
        log.info("End fetching leave details - RequestId: {}, LeaveId: {}", requestId, leaveId);
        return new ApiResponse<>(
                new Status(Response.Status.OK.getStatusCode(), "Leave details fetched successfully", requestId),
                leaveDTO
        );
    }



    private EmployeeDTO.LeaveDTO mapToLeaveDTO(Leave leave) {
        EmployeeDTO.LeaveDTO leaveDTO = new EmployeeDTO.LeaveDTO();
        leaveDTO.setId(leave.getId());
        leaveDTO.setEmployeeId(leave.getEmployee().getId());
        leaveDTO.setLeaveTypeId(leave.getLeaveType().getId());
        leaveDTO.setStartDate(leave.getStartDate());
        leaveDTO.setEndDate(leave.getEndDate());
        leaveDTO.setDepartmentId(leave.getDepartment() != null ? leave.getDepartment().getId() : null);
        leaveDTO.setReason(leave.getReason());
        leaveDTO.setAppliedDate(leave.getAppliedDate());
        leaveDTO.setAdminRemarks(leave.getAdminRemarks());
//        leaveDTO.setAttachmentUrl(leave.getAttachment());  // Ensure this is properly handled
        leaveDTO.setAttachmentName(leave.getAttachmentName());
        return leaveDTO;
    }


    @Override
    @Transactional
    public ApiResponse updateLeaveRequest(Integer leaveId, ApiRequest<EmployeeDTO> apiRequest, String requestId) {
        log.info("RequestId: {} | Updating Leave Request with LeaveId: {}", requestId, leaveId);

        // Fetch the existing leave request
        Leave existingLeave = leaveRepository.findById(leaveId);
        if (existingLeave == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Leave request not found", requestId)
            );
        }

        EmployeeDTO.LeaveDTO leaveDTO = apiRequest.getData().getLeaveDTO();

        // Fetch the employee details
        EmployeeDetails employee = employeeDetailsRepository.findById(leaveDTO.getEmployeeId());
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Fetch the leave type
        LeaveType leaveType = leaveTypeRepository.findById(Long.valueOf(leaveDTO.getLeaveTypeId()));
        if (leaveType == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Leave type not found", requestId)
            );
        }

        // Update the  leave request
        existingLeave.setEmployee(employee);
        existingLeave.setLeaveType(leaveType);
        existingLeave.setStartDate(leaveDTO.getStartDate());
        existingLeave.setEndDate(leaveDTO.getEndDate());
        existingLeave.setReason(leaveDTO.getReason());
        leaveRepository.persist(existingLeave);

        log.info("RequestId: {} | Leave Request with LeaveId: {} updated successfully", requestId, leaveId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave request updated successfully", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAllLeaveRequest(String requestId) {
        log.info("Start fetching all leave requests - RequestId: {}", requestId);

        List<Leave> leaveList = leaveRepository.findAll().list();
        if (leaveList.isEmpty()) {
            log.warn("No leave requests found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No leave requests found", requestId)
            );
        }

        // Use the new method from LeaveMapper
        List<EmployeeDTO.LeaveDTO> leaveDTOs = LeaveMapper.INSTANCE.toDTOList(leaveList);


        log.info("End fetching all leave requests - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave requests fetched successfully", requestId),
                leaveDTOs
        );
    }

    @Transactional
    public ApiResponse createLeaveRequest(LeaveRequest leaveRequest, String requestId)
            throws IOException, BusinessException {

        // Fetch Employee from Database
        EmployeeDetails employee = employeeDetailsRepository.findById(leaveRequest.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("Employee not found for ID: " + leaveRequest.getEmployeeId());
        }

        // Fetch Leave Type
        LeaveType leaveType = leaveTypeRepository.findById(Long.valueOf(leaveRequest.getLeaveTypeId()));
        if (leaveType == null) {
            throw new BusinessException("Leave type not found for ID: " + leaveRequest.getLeaveTypeId());
        }

        // Fetch Department
        Department department = departmentRepository.findById(Long.valueOf(leaveRequest.getDepartmentId()));
        if (department == null) {
            throw new BusinessException("Leave type not found for ID: " + leaveRequest.getDepartmentId());
        }

        // Create Leave Entity
        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
        leave.setDepartment(department);
        leave.setStartDate(leaveRequest.getStartDate());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setReason(leaveRequest.getReason());
        leave.setAdminRemarks(leaveRequest.getAdminRemarks());
        leave.setAppliedDate(LocalDate.now());

        if (leaveRequest.getAttachment() != null) {
            leave.setAttachment(leaveRequest.getAttachment());  // Store as BLOB
            leave.setAttachmentName(leaveRequest.getAttachmentName());
        }

        // Persist Leave Entity
        leaveRepository.persist(leave);

        // Convert Entity to DTO using MapStruct
        EmployeeDTO.LeaveDTO leaveDTO = LeaveMapper.INSTANCE.toDTO(leave);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave request submitted successfully", requestId)
       ,leaveDTO );
    }
}
