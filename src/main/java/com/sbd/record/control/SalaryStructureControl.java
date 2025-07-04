package com.sbd.record.control;

import com.sbd.common.Jsonb.PayrollJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;

public interface SalaryStructureControl {
    ApiResponse fetchPayslipData(PayrollJsonb requestDTO, String requestId) throws BusinessException;
    ApiResponse fetchSalaryStructure(PayrollJsonb requestDTO,String requestId) throws BusinessException;
    ApiResponse fetchAllEmployeesPayslipData(PayrollJsonb requestDTO,String requestId) throws BusinessException;

}
