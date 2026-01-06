package com.sbd.record.control.service;

import com.sbd.common.Jsonb.*;
import com.sbd.common.entity.*;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.SalaryActionEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.EmployeeSalaryStructureMapper;
import com.sbd.common.mapper.SalaryStructureMapper;
import com.sbd.common.repository.*;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.SalaryStructureControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class SalaryStructureService implements SalaryStructureControl {

    @Inject
    SalaryStructureRepository salaryStructureRepository;

    @Inject
    PayrollRepository payrollRepository;

    @Inject
    PayrollComponentRepository payrollComponentRepository;

    @Inject
    EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    EmployeeSalaryStructureRepository employeeSalaryStructureRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public ApiResponse fetchPayslipData(PayrollJsonb requestDTO, String requestId) throws BusinessException {
        Long employeeId = requestDTO.getEmployeeId();
        Integer month = requestDTO.getMonth();
        Integer year = requestDTO.getYear();

        log.info("Start fetching salary structure - RequestId: {}, EmployeeId: {}, Month: {}, Year: {}",
                requestId, employeeId, month, year);

        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId));
        }

        // Fetch EmployeeSalaryStructure records for the given employee
        List<EmployeeSalaryStructure> structures = employeeSalaryStructureRepository
                .find("employee.id = ?1", employeeId)
                .list();

        if (structures.isEmpty()) {
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "Salary structure not found", requestId));
        }

        // Map entity to JSONB DTO
        List<EmployeeSalaryStructureJsonb> structureJsonbList = structures.stream()
                .map(structure -> {
                    EmployeeSalaryStructureJsonb dto = new EmployeeSalaryStructureJsonb();
                    dto.setSalaryStructureId(structure.getId());
                    dto.setEmployeeId(employee.getId().intValue());
                    dto.setDepartmentId(structure.getDepartment().getId());
                    dto.setLocation(structure.getLocation());

                    dto.setBasicSalary(structure.getBasicSalary());
                    dto.setHouseRentAllowance(structure.getHouseRentAllowance());
                    dto.setSpecialAllowance(structure.getSpecialAllowance());
                    dto.setNpsEmployer(structure.getNpsEmployer());
                    dto.setCarReimbursement(structure.getCarReimbursement());
                    dto.setDriverReimbursement(structure.getDriverReimbursement());
                    dto.setPdReimbursement(structure.getPdReimbursement());
                    dto.setTelephoneReimbursement(structure.getTelephoneReimbursement());

                    dto.setGrossSalary(structure.getGrossSalary());
                    dto.setPfContribution(structure.getPfContribution());
                    dto.setEsiContribution(structure.getEsiContribution());
                    dto.setFixedSalary(structure.getFixedSalary());
                    dto.setGratuityPayable(structure.getGratuityPayable());
                    dto.setBonusPayable(structure.getBonusPayable());
                    dto.setLtaPayable(structure.getLtaPayable());
                    dto.setVariablePayable(structure.getVariablePayable());
                    dto.setMediclaimBenefits(structure.getMediclaimBenefits());
                    dto.setGrandTotalCtc(structure.getGrandTotalCtc());

                    dto.setMonthlySalary(structure.getMonthlySalary());
                    dto.setAnnualCtc(structure.getAnnualCtc());
                    dto.setApprovalStatus(structure.getApprovalStatus());
                    dto.setSalaryStatus(structure.getSalaryStatus());

                    dto.setPfApplicable(structure.getPfApplicable());
                    dto.setPfLimit(structure.getPfLimit());
                    dto.setEsiApplicable(structure.getEsiApplicable());
                    dto.setGratuityApplicable(structure.getGratuityApplicable());
                    dto.setBonusApplicable(structure.getBonusApplicable());

                    dto.setBasicSalaryPercent(structure.getBasicSalaryPercent());
                    dto.setHraPercent(structure.getHraPercent());
                    dto.setNpsPercent(structure.getNpsPercent());
                    dto.setMinimumWage(structure.getMinimumWage());

                    return dto;
                })
                .collect(Collectors.toList());

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Salary structure fetched successfully", requestId),
                structureJsonbList
        );
    }


    @Transactional
    @Override
    public ApiResponse fetchSalaryStructure(PayrollJsonb requestDTO, String requestId) throws BusinessException {
        log.info("Start fetching salary structure - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());

        EmployeeDetails employee = employeeDetailsRepository.findById(requestDTO.getEmployeeId());
        if (employee == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Fetch salary structure(s)
        List<EmployeeSalaryStructure> structures = employeeSalaryStructureRepository
                .find("employee.id = ?1", requestDTO.getEmployeeId())
                .list();

        if (structures == null || structures.isEmpty()) {
            log.error("Salary structure not found for employee - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Salary structure not found for employee", requestId)
            );
        }

        // Map to EmployeeSalaryStructureJsonb
        List<EmployeeSalaryStructureJsonb> jsonbList = structures.stream().map(structure -> {
            EmployeeSalaryStructureJsonb dto = new EmployeeSalaryStructureJsonb();

            dto.setSalaryStructureId(structure.getId());
            dto.setEmployeeId(employee.getId().intValue());
            dto.setDepartmentId(structure.getDepartment().getId());
            dto.setLocation(structure.getLocation());

            dto.setBasicSalary(structure.getBasicSalary());
            dto.setHouseRentAllowance(structure.getHouseRentAllowance());
            dto.setSpecialAllowance(structure.getSpecialAllowance());
            dto.setNpsEmployer(structure.getNpsEmployer());
            dto.setCarReimbursement(structure.getCarReimbursement());
            dto.setDriverReimbursement(structure.getDriverReimbursement());
            dto.setPdReimbursement(structure.getPdReimbursement());
            dto.setTelephoneReimbursement(structure.getTelephoneReimbursement());

            dto.setGrossSalary(structure.getGrossSalary());
            dto.setPfContribution(structure.getPfContribution());
            dto.setEsiContribution(structure.getEsiContribution());
            dto.setFixedSalary(structure.getFixedSalary());
            dto.setGratuityPayable(structure.getGratuityPayable());
            dto.setBonusPayable(structure.getBonusPayable());
            dto.setLtaPayable(structure.getLtaPayable());
            dto.setVariablePayable(structure.getVariablePayable());
            dto.setMediclaimBenefits(structure.getMediclaimBenefits());
            dto.setGrandTotalCtc(structure.getGrandTotalCtc());

            dto.setMonthlySalary(structure.getMonthlySalary());
            dto.setAnnualCtc(structure.getAnnualCtc());
            dto.setApprovalStatus(structure.getApprovalStatus());
            dto.setSalaryStatus(structure.getSalaryStatus());

            dto.setPfApplicable(structure.getPfApplicable());
            dto.setPfLimit(structure.getPfLimit());
            dto.setEsiApplicable(structure.getEsiApplicable());
            dto.setGratuityApplicable(structure.getGratuityApplicable());
            dto.setBonusApplicable(structure.getBonusApplicable());

            dto.setBasicSalaryPercent(structure.getBasicSalaryPercent());
            dto.setHraPercent(structure.getHraPercent());
            dto.setNpsPercent(structure.getNpsPercent());
            dto.setMinimumWage(structure.getMinimumWage());

            return dto;
        }).collect(Collectors.toList());

        log.info("End fetching salary structure - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Salary structure fetched successfully", requestId),
                jsonbList
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAllEmployeesPayslipData(PayrollJsonb requestDTO, String requestId) throws BusinessException {
        Integer month = requestDTO.getMonth();
        Integer year = requestDTO.getYear();

        log.info("Start fetching all employees salary structure data - RequestId: {}, Month: {}, Year: {}", requestId, month, year);

        List<EmployeeDetails> employees = employeeDetailsRepository.listAll();

        if (employees == null || employees.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No employees found", requestId)
            );
        }

        List<EmployeePayslipDTO> responseList = employees.stream()
                .map(employee -> {
                    List<EmployeeSalaryStructure> salaryStructures;

                    if (month != null && year != null) {
                        salaryStructures = employeeSalaryStructureRepository.find(
                                "employee = ?1 AND MONTH(createdAt) = ?2 AND YEAR(createdAt) = ?3",
                                employee, month, year
                        ).list();
                    } else if (year != null) {
                        salaryStructures = employeeSalaryStructureRepository.find(
                                "employee = ?1 AND YEAR(createdAt) = ?2",
                                employee, year
                        ).list();
                    } else {
                        salaryStructures = employeeSalaryStructureRepository.find(
                                "employee = ?1",
                                employee
                        ).list();
                    }

                    List<EmployeeSalaryStructureJsonb> salaryJsonbList = salaryStructures.stream()
                            .map(EmployeeSalaryStructureMapper.INSTANCE::toJsonb)
                            .collect(Collectors.toList());

                    return new EmployeePayslipDTO(
                            employee.getId().longValue(),
                            employee.getEmployeeName(),
                            salaryJsonbList
                    );
                })
                .filter(dto -> !dto.getSalaryStructures().isEmpty())
                .collect(Collectors.toList());

        if (responseList.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No salary structure data found for any employee", requestId)
            );
        }

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Salary structure data fetched successfully", requestId),
                responseList
        );
    }


    @Override
    @Transactional
    public ApiResponse createSalaryStructure(EmployeeSalaryStructureJsonb request, String requestId) throws BusinessException {
        log.info("Start creating Employee Salary Structure - RequestId: {}", requestId);

        // Validate Employee existence
        EmployeeDetails employee = employeeDetailsRepository.findById(request.getEmployeeId());
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Check for existing Salary Structure (optional duplication prevention)
        EmployeeSalaryStructure existing = employeeSalaryStructureRepository.findByEmployeeId(request.getEmployeeId());

        if (existing != null) {
            return new ApiResponse(
                    new Status(Response.Status.CONFLICT.getStatusCode(), "Salary structure already exists for this employee", requestId)
            );
        }

        // Map request to entity
        EmployeeSalaryStructure salary = new EmployeeSalaryStructure();
        salary.setEmployee(employee);
        salary.setDepartment(departmentRepository.findById(request.getDepartmentId()));
        salary.setLocation(request.getLocation());

        // Fixed Components
        salary.setBasicSalary(request.getBasicSalary());
        salary.setHouseRentAllowance(request.getHouseRentAllowance());
        salary.setSpecialAllowance(request.getSpecialAllowance());
        salary.setNpsEmployer(request.getNpsEmployer());
        salary.setCarReimbursement(request.getCarReimbursement());
        salary.setDriverReimbursement(request.getDriverReimbursement());
        salary.setPdReimbursement(request.getPdReimbursement());
        salary.setTelephoneReimbursement(request.getTelephoneReimbursement());

        // Salary Calculations
        salary.setGrossSalary(request.getGrossSalary());
        salary.setPfContribution(request.getPfContribution());
        salary.setEsiContribution(request.getEsiContribution());
        salary.setFixedSalary(request.getFixedSalary());
        salary.setGratuityPayable(request.getGratuityPayable());
        salary.setBonusPayable(request.getBonusPayable());
        salary.setLtaPayable(request.getLtaPayable());
        salary.setVariablePayable(request.getVariablePayable());
        salary.setMediclaimBenefits(request.getMediclaimBenefits());
        salary.setGrandTotalCtc(request.getGrandTotalCtc());

        // Summary
        salary.setMonthlySalary(request.getMonthlySalary());
        salary.setAnnualCtc(request.getAnnualCtc());
        salary.setApprovalStatus(request.getApprovalStatus());
        salary.setSalaryStatus(request.getSalaryStatus());

        // Percentages
        salary.setPfApplicable(request.getPfApplicable());
        salary.setPfLimit(request.getPfLimit());
        salary.setEsiApplicable(request.getEsiApplicable());
        salary.setGratuityApplicable(request.getGratuityApplicable());
        salary.setBonusApplicable(request.getBonusApplicable());
        salary.setBasicSalaryPercent(request.getBasicSalaryPercent());
        salary.setHraPercent(request.getHraPercent());
        salary.setNpsPercent(request.getNpsPercent());
        salary.setMinimumWage(request.getMinimumWage());

        salary.setCreatedAt(LocalDateTime.now());
        salary.setUpdatedAt(LocalDateTime.now());

        // Persist salary structure
        employeeSalaryStructureRepository.persist(salary);
        employeeSalaryStructureRepository.flush();

        log.info("End creating Employee Salary Structure - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Salary structure successfully created", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse updateSalaryStructure(Integer employeeId, ApiRequest<EmployeeSalaryStructureJsonb> apiRequest, String requestId) throws BusinessException {
        log.info("Start updating Employee Salary Structure - RequestId: {}, EmployeeId: {}", requestId, employeeId);

        EmployeeSalaryStructureJsonb request = apiRequest.getData();
        if (request == null) {
            throw new BusinessException(Response.Status.BAD_REQUEST.getStatusCode(), requestId, "Request data cannot be null");
        }

        // Validate existence of Employee
        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        // Fetch existing salary structure
        EmployeeSalaryStructure existing = employeeSalaryStructureRepository.findByEmployeeId(employeeId);
        if (existing == null) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Salary structure not found for this employee", requestId)
            );
        }

        // Update fields
        existing.setLocation(request.getLocation());

        // Fixed Components
        existing.setBasicSalary(request.getBasicSalary());
        existing.setHouseRentAllowance(request.getHouseRentAllowance());
        existing.setSpecialAllowance(request.getSpecialAllowance());
        existing.setNpsEmployer(request.getNpsEmployer());
        existing.setCarReimbursement(request.getCarReimbursement());
        existing.setDriverReimbursement(request.getDriverReimbursement());
        existing.setPdReimbursement(request.getPdReimbursement());
        existing.setTelephoneReimbursement(request.getTelephoneReimbursement());

        // Salary Calculations
        existing.setGrossSalary(request.getGrossSalary());
        existing.setPfContribution(request.getPfContribution());
        existing.setEsiContribution(request.getEsiContribution());
        existing.setFixedSalary(request.getFixedSalary());
        existing.setGratuityPayable(request.getGratuityPayable());
        existing.setBonusPayable(request.getBonusPayable());
        existing.setLtaPayable(request.getLtaPayable());
        existing.setVariablePayable(request.getVariablePayable());
        existing.setMediclaimBenefits(request.getMediclaimBenefits());
        existing.setGrandTotalCtc(request.getGrandTotalCtc());

        // Summary
        existing.setMonthlySalary(request.getMonthlySalary());
        existing.setAnnualCtc(request.getAnnualCtc());
        existing.setApprovalStatus(request.getApprovalStatus());
        existing.setSalaryStatus(request.getSalaryStatus());

        // Percentages
        existing.setPfApplicable(request.getPfApplicable());
        existing.setPfLimit(request.getPfLimit());
        existing.setEsiApplicable(request.getEsiApplicable());
        existing.setGratuityApplicable(request.getGratuityApplicable());
        existing.setBonusApplicable(request.getBonusApplicable());
        existing.setBasicSalaryPercent(request.getBasicSalaryPercent());
        existing.setHraPercent(request.getHraPercent());
        existing.setNpsPercent(request.getNpsPercent());
        existing.setMinimumWage(request.getMinimumWage());

        existing.setUpdatedAt(LocalDateTime.now());

        employeeSalaryStructureRepository.persist(existing);
        employeeSalaryStructureRepository.flush();

        log.info("End updating Employee Salary Structure - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Salary structure successfully updated", requestId)
        );
    }

    @Override
    @Transactional
    public ApiResponse updateApprovalStatus(Integer employeeId, String approvalStatus, String requestId) {
        log.info("Start updating salary structure approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);

        // Validate the approval status
        if (approvalStatus == null ||
                (!"Approved".equalsIgnoreCase(approvalStatus) &&
                        !"Rejected".equalsIgnoreCase(approvalStatus) &&
                        !"Pending".equalsIgnoreCase(approvalStatus))) {

            log.error("Invalid approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid approval status", requestId)
            );
        }

        // Fetch the existing salary structure
        EmployeeSalaryStructure salaryStructure = employeeSalaryStructureRepository.findByEmployeeId(employeeId);
        if (salaryStructure == null) {
            log.error("Salary structure not found - RequestId: {}, EmployeeId: {}", requestId, employeeId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Salary structure not found for this employee", requestId)
            );
        }

        // Update the approval status
        salaryStructure.setApprovalStatus(approvalStatus);
        salaryStructure.setUpdatedAt(LocalDateTime.now());

        try {
            employeeSalaryStructureRepository.persist(salaryStructure);
            log.info("Successfully updated salary structure approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
        } catch (Exception e) {
            log.error("Error while updating salary structure approval status - RequestId: {}, EmployeeId: {}", requestId, employeeId, e);
            return new ApiResponse(
                    new Status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Error updating salary structure approval status", requestId)
            );
        }

        log.info("End updating salary structure approval status - RequestId: {}, EmployeeId: {}, ApprovalStatus: {}", requestId, employeeId, approvalStatus);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), approvalStatus + " updated", requestId)
        );
    }

    @Override
    public ApiResponse fetchSalaryDashboard(String correlationId) throws BusinessException {
        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                SalaryActionEnum.SALARY_DASHBOARD.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        // Fetch aggregated salary data
        Map<String, Object> rawData =
                salaryStructureRepository.fetchSalaryDashboardSummary();

        // Map result → DTO
        SalaryDashboardDTO response =
                SalaryStructureMapper.INSTANCE.toDTO(rawData);


        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                SalaryActionEnum.SALARY_DASHBOARD.getValue(),
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


}
