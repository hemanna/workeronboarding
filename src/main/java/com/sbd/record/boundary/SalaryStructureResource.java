package com.sbd.record.boundary;

import com.sbd.common.Jsonb.PayrollJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.SalaryStructureControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/salarystructure")
@AllArgsConstructor
@Slf4j
public class SalaryStructureResource {

    private final SalaryStructureControl salaryStructureControl;
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getPayslip")
    public ApiResponse fetchPayslipData(PayrollJsonb requestDTO) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id : {} | get payslip for employeeId: {}, month: {}, year: {}",
                requestId, requestDTO.getEmployeeId(), requestDTO.getMonth(), requestDTO.getYear());

        return salaryStructureControl.fetchPayslipData(requestDTO, requestId);
    }



    @POST
    @Path("/generatePayslip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse generatePayslip(PayrollJsonb requestDTO) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        return salaryStructureControl.fetchSalaryStructure(requestDTO, requestId);
    }

}
