package com.sbd.record.control.service;

import com.sbd.common.Jsonb.PayrollDTO;
import com.sbd.common.Jsonb.PayrollJsonb;
import com.sbd.common.Jsonb.SalaryStructureDTO;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.Payroll;
import com.sbd.common.entity.PayrollComponent;
import com.sbd.common.entity.SalaryStructure;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.SalaryStructureMapper;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.repository.PayrollComponentRepository;
import com.sbd.common.repository.PayrollRepository;
import com.sbd.common.repository.SalaryStructureRepository;
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
    }
