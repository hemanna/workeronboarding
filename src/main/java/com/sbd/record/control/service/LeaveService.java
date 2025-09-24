package com.sbd.record.control.service;

import com.sbd.common.Jsonb.CompanyHolidayJsonb;
import com.sbd.common.Jsonb.LeaveBalanceJsonb;
import com.sbd.common.Jsonb.LeaveDTO;
import com.sbd.common.entity.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.LeaveMapper;
import com.sbd.common.repository.*;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Inject
    LeaveBalanceRepository leaveBalanceRepository;

    @Inject
    CompanyHolidayRepository companyHolidayRepository;

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
        leaveDTO.setAdminRemarks("Pending");
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

        List<LeaveDTO> leaveList = leaveRepository.findAllWithLeaveDays();
        if (leaveList.isEmpty()) {
            log.warn("No leave requests found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No leave requests found", requestId)
            );
        }

        // Use the new method from LeaveMapper
//        List<LeaveDTO> leaveDTOs = Collections.singletonList(LeaveMapper.INSTANCE.toDTO((Leave) leaveList));


        log.info("End fetching all leave requests - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave requests fetched successfully", requestId),
                leaveList
        );
    }

    @Override
    @Transactional
    public ApiResponse updateApprovalStatus(Integer leaveId, String approvalStatus, String requestId) {
        log.info("Start updating approval status - RequestId: {}, LeaveId: {}, ApprovalStatus: {}", requestId, leaveId, approvalStatus);

        // Validate the approval status
        if (approvalStatus == null || (!"Approved".equalsIgnoreCase(approvalStatus) && !"Rejected".equalsIgnoreCase(approvalStatus))) {
            log.error("Invalid approval status - RequestId: {}, LeaveId: {}, ApprovalStatus: {}", requestId, leaveId, approvalStatus);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid approval status", requestId)
            );
        }

        // Fetch the existing leave request
        Leave existingLeave = leaveRepository.findById(leaveId);
        if (existingLeave == null) {
            log.error("Leave request not found - RequestId: {}, LeaveId: {}", requestId, leaveId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Leave request not found", requestId)
            );
        }

        // Update the approval status of the leave
        existingLeave.setStatus(approvalStatus);

        try {
            leaveRepository.persist(existingLeave);
            log.info("Successfully updated approval status - RequestId: {}, LeaveId: {}, ApprovalStatus: {}", requestId, leaveId, approvalStatus);
        } catch (Exception e) {
            log.error("Error while updating approval status - RequestId: {}, LeaveId: {}", requestId, leaveId, e);
            return new ApiResponse(
                    new Status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error updating approval status", requestId)
            );
        }

        log.info("End updating approval status - RequestId: {}, LeaveId: {}, ApprovalStatus: {}", requestId, leaveId, approvalStatus);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Approval status updated to " + approvalStatus, requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse<List<LeaveBalanceJsonb>> fetchLeaveBalancesByEmployeeId(int employeeId, int year, String requestId) {
        String leavePeriod = String.valueOf(year);
        List<Object[]> results = leaveRepository.findLeaveBalancesByEmployeeId(employeeId, year);

        List<LeaveBalanceJsonb> dtoList = new ArrayList<>();

        if (results.isEmpty()) {
            return new ApiResponse<>(
                    new Status(Response.Status.OK.getStatusCode(), "No leave balances found", requestId),
                    dtoList
            );
        }

        EmployeeDetails employee = employeeDetailsRepository.findById(Long.valueOf(employeeId));
        if (employee == null) {
            return new ApiResponse<>(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Employee not found", requestId),
                    dtoList
            );
        }

        for (Object[] row : results) {
            int leaveTypeId = ((Number) row[0]).intValue();
            String leaveTypeName = (String) row[1];
            int entitled = ((Number) row[2]).intValue();
            int takenDays = ((Number) row[3]).intValue();

            LeaveType leaveType = leaveTypeRepository.findById(Long.valueOf(leaveTypeId));
            if (leaveType == null) continue;

            boolean carryForwardAllowed = Boolean.TRUE.equals(leaveType.getIsCarryForwardAllowed());

            LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeAndLeaveTypeAndYear(employeeId, leaveTypeId, leavePeriod);

            if (leaveBalance == null) {
                leaveBalance = new LeaveBalance();
                leaveBalance.setEmployee(employee);
                leaveBalance.setLeaveType(leaveType);
                leaveBalance.setTakenDays(takenDays);
                leaveBalance.setCarryForwardDays(0); // Default value or calculate if needed
                leaveBalance.setRemainingDays(entitled - takenDays);
                leaveBalance.setLeavePeriod(leavePeriod);
                leaveBalanceRepository.persist(leaveBalance);
            } else {
                leaveBalance.setTakenDays(takenDays);
                leaveBalance.setRemainingDays(entitled - takenDays + leaveBalance.getCarryForwardDays());
                leaveBalanceRepository.persist(leaveBalance);
            }

            LeaveBalanceJsonb dto = new LeaveBalanceJsonb(
                    employeeId,
                    leaveTypeId,
                    entitled,
                    takenDays,
                    leaveBalance.getCarryForwardDays() != null ? leaveBalance.getCarryForwardDays() : 0,
                    leaveBalance.getRemainingDays(),
                    leavePeriod,
                    carryForwardAllowed
            );
            dtoList.add(dto);
        }

        return new ApiResponse<>(
                new Status(Response.Status.OK.getStatusCode(), "Leave Balance fetched successfully", requestId),
                dtoList
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
        leave.setLeaveDuration(leaveRequest.getLeaveDuration());
        leave.setStatus("pending");
        leave.setAdminRemarks(leaveRequest.getAdminRemarks());
        leave.setAppliedDate(LocalDate.now());

        if (leaveRequest.getAttachment() != null) {
            leave.setAttachment(leaveRequest.getAttachment());  // Store as BLOB
            leave.setAttachmentName(leaveRequest.getAttachmentName());
        }
        // Persist Leave Entity
        leaveRepository.persist(leave);
        // Convert Entity to DTO using MapStruct
        LeaveDTO leaveDTO = LeaveMapper.INSTANCE.toDTO(leave);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Leave request submitted successfully", requestId));
    }

    @Override
    @Transactional
    public ApiResponse fetchAllHolidays(String requestId) {
        log.info("Start fetching all holidays records - RequestId: {}", requestId);

        List<CompanyHoliday> holidays = companyHolidayRepository.listAllHolidays();

        log.info("End fetching all holidaysz records - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Fetched holidays successfully", requestId),
                holidays
        );
    }

    @Override
    @Transactional
    public ApiResponse createHoliday(ApiRequest<CompanyHolidayJsonb> apiRequest, String requestId) throws IOException, BusinessException {
        log.info("Start creating holiday - RequestId: {}", requestId);

        CompanyHolidayJsonb dto = apiRequest.getData();

        if (dto.getHolidayDate() == null || dto.getReason() == null || dto.getReason().isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Holiday date and reason are required", requestId)
            );
        }

        CompanyHoliday holiday = new CompanyHoliday();
        holiday.setHolidayDate(dto.getHolidayDate());
        holiday.setReason(dto.getReason());

        companyHolidayRepository.persist(holiday);

        log.info("End creating holiday - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Holiday created successfully", requestId)
        );
    }

}
