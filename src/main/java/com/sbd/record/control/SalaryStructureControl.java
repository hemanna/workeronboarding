package com.sbd.record.control;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.Jsonb.EmployeeSalaryStructureJsonb;
import com.sbd.common.Jsonb.PayrollJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;

public interface SalaryStructureControl {
    ApiResponse fetchPayslipData(PayrollJsonb requestDTO, String requestId) throws BusinessException;
    ApiResponse fetchSalaryStructure(PayrollJsonb requestDTO,String requestId) throws BusinessException;
    ApiResponse fetchAllEmployeesPayslipData(PayrollJsonb requestDTO,String requestId) throws BusinessException;
    ApiResponse createSalaryStructure(EmployeeSalaryStructureJsonb apiRequest, String requestId) throws BusinessException;

    ApiResponse updateSalaryStructure(
            Integer employeeId,
            ApiRequest<EmployeeSalaryStructureJsonb> apiRequest,
            String requestId)
            throws BusinessException;

    ApiResponse updateApprovalStatus(
            Integer employeeId,
            String approvalStatus,
            String requestId);

    ApiResponse fetchSalaryDashboard(
            String correlationId) throws BusinessException;

}
