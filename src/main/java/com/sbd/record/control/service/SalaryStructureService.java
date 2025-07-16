package com.sbd.record.control.service;

import com.sbd.common.Jsonb.*;
import com.sbd.common.entity.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.SalaryStructureMapper;
import com.sbd.common.repository.*;
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

        log.info("Start fetching payslip data - RequestId: {}, EmployeeId: {}, Month: {}, Year: {}",
                requestId, employeeId, month, year);

        EmployeeDetails employee = employeeDetailsRepository.findById(employeeId);
        if (employee == null) {
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId));
        }

        List<Payroll> payrolls;

        if (month != null && year != null) {
            payrolls = payrollRepository.find("employeeId = ?1 and month = ?2 and year = ?3", employee, month, year).list();
        } else if (year != null) {
            payrolls = payrollRepository.find("employeeId = ?1 and year = ?2", employee, year).list();
        } else {
            payrolls = payrollRepository.find("employeeId = ?1", employee).list();
        }

        if (payrolls.isEmpty()) {
            return new ApiResponse(new Status(Response.Status.NOT_FOUND.getStatusCode(), "Payroll data not found", requestId));
        }

        List<PayrollDTO> responseList = payrolls.stream().map(payroll -> {
            List<PayrollComponent> components = payrollComponentRepository.find("payrollId = ?1", payroll).list();
            List<SalaryStructureDTO> componentDTOs = components.stream()
                    .map(SalaryStructureMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());
            return new PayrollDTO(
                    payroll.getGrossSalary(),
                    payroll.getNetSalary(),
                    payroll.getGeneratedOn(),
                    payroll.getMonth(),
                    payroll.getYear(),
                    componentDTOs
            );
        }).collect(Collectors.toList());

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Payslip fetched successfully", requestId),
                responseList
        );
    }


    @Transactional
    @Override
    public ApiResponse fetchSalaryStructure(PayrollJsonb requestDTO,String requestId) throws BusinessException {
        log.info("Start generating payslip - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());

        EmployeeDetails employee = employeeDetailsRepository.findById(requestDTO.getEmployeeId());
        if (employee == null) {
            log.error("Employee not found - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "Employee not found", requestId)
            );
        }

        Payroll existingPayroll = payrollRepository.find("employeeId = ?1 and month = ?2 and year = ?3",
                        employee, requestDTO.getMonth(), requestDTO.getYear())
                .firstResult();

        List<SalaryStructureDTO> components;
        Payroll payroll;

        if (existingPayroll != null) {
            log.info("Payslip already generated - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
            payroll = existingPayroll;
            List<PayrollComponent> componentEntities = payrollComponentRepository
                    .find("payrollId = ?1", payroll)
                    .list();

            components = componentEntities.stream()
                    .map(SalaryStructureMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());

        } else {
            log.info("Generating new payslip - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
            List<SalaryStructure> structures = salaryStructureRepository
                    .find("employeeId = ?1", employee)
                    .list();

            if (structures.isEmpty()) {
                log.error("Salary structure not defined for employee - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
                return new ApiResponse(
                        new Status(Response.Status.NOT_FOUND.getStatusCode(), "Salary structure not found for employee", requestId)
                );
            }

            components = structures.stream()
                    .map(SalaryStructureMapper.INSTANCE::toDTO)
                    .collect(Collectors.toList());

            BigDecimal grossSalary = structures.stream()
                    .filter(s -> s.getType().equalsIgnoreCase("earning") || s.getType().equalsIgnoreCase("reimbursement"))
                    .map(SalaryStructure::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalDeductions = structures.stream()
                    .filter(s -> s.getType().equalsIgnoreCase("deduction"))
                    .map(SalaryStructure::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal netSalary = grossSalary.subtract(totalDeductions);

            payroll = new Payroll();
            payroll.setEmployeeId(employee);
            payroll.setMonth(requestDTO.getMonth());
            payroll.setYear(requestDTO.getYear());
            payroll.setGrossSalary(grossSalary);
            payroll.setNetSalary(netSalary);
            payroll.setGeneratedOn(LocalDate.now());
            payroll.setStatus("GENERATED");
            payroll.setCreatedAt(LocalDateTime.now());
            payroll.setUpdatedAt(LocalDateTime.now());

            payrollRepository.persist(payroll);

            for (SalaryStructure s : structures) {
                PayrollComponent pc = new PayrollComponent();
                pc.setPayrollId(payroll);
                pc.setComponentName(s.getComponentName());
                pc.setAmount(s.getAmount());
                pc.setType(s.getType());
                pc.setCreatedAt(LocalDateTime.now());
                pc.setUpdatedAt(LocalDateTime.now());
                payrollComponentRepository.persist(pc);
            }

            log.info("Payslip generated and saved - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
        }

        PayrollDTO response = new PayrollDTO(
                payroll.getGrossSalary(),
                payroll.getNetSalary(),
                payroll.getGeneratedOn(),
                payroll.getMonth(),
                payroll.getYear(),
                components
        );

        log.info("End generating payslip - RequestId: {}, EmployeeId: {}", requestId, requestDTO.getEmployeeId());
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Payslip generated successfully", requestId),
                response
        );
    }

    @Override
    @Transactional
    public ApiResponse fetchAllEmployeesPayslipData(PayrollJsonb requestDTO, String requestId) throws BusinessException {
        Integer month = requestDTO.getMonth();
        Integer year = requestDTO.getYear();

        log.info("Start fetching all employees payslip data - RequestId: {}, Month: {}, Year: {}", requestId, month, year);

        List<EmployeeDetails> employees = employeeDetailsRepository.listAll();

        if (employees == null || employees.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No employees found", requestId)
            );
        }

        List<EmployeePayslipDTO> responseList = employees.stream()
                .map(employee -> {
                    List<Payroll> payrolls;

                    if (month != null && year != null) {
                        payrolls = payrollRepository.find(
                                "employeeId = ?1 and month = ?2 and year = ?3",
                                employee,
                                month,
                                year
                        ).list();
                    } else if (year != null) {
                        payrolls = payrollRepository.find(
                                "employeeId = ?1 and year = ?2",
                                employee,
                                year
                        ).list();
                    } else {
                        // Fetch all payrolls for employee (no filter)
                        payrolls = payrollRepository.find(
                                "employeeId = ?1",
                                employee
                        ).list();
                    }

                    List<PayrollDTO> payrollDTOs = payrolls.stream()
                            .map(payroll -> {
                                List<PayrollComponent> components = payrollComponentRepository.find(
                                        "payrollId = ?1", payroll
                                ).list();

                                List<SalaryStructureDTO> componentDTOs = components.stream()
                                        .map(SalaryStructureMapper.INSTANCE::toDTO)
                                        .collect(Collectors.toList());

                                return new PayrollDTO(
                                        payroll.getGrossSalary(),
                                        payroll.getNetSalary(),
                                        payroll.getGeneratedOn(),
                                        payroll.getMonth(),
                                        payroll.getYear(),
                                        componentDTOs
                                );
                            })
                            .collect(Collectors.toList());

                    return new EmployeePayslipDTO(
                            employee.getId().longValue(),
                            employee.getEmployeeName(),
                            payrollDTOs
                    );
                })
                .filter(dto -> !dto.getPayrolls().isEmpty()) // Remove employees with no payroll data
                .collect(Collectors.toList());

        if (responseList.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No payroll data found for any employee", requestId)
            );
        }

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Payslip data fetched successfully", requestId),
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

}
