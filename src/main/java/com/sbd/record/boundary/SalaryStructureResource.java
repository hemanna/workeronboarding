package com.sbd.record.boundary;

import com.sbd.common.Jsonb.EmployeeSalaryStructureJsonb;
import com.sbd.common.Jsonb.PayrollJsonb;
import com.sbd.common.Jsonb.SalaryReportJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.SalaryStructureControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get_all_payslipdata")
    public ApiResponse fetchAllEmployeesPayslipData(PayrollJsonb requestDTO) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        log.info("request_id: {} | Fetching all employees", requestId);

        return salaryStructureControl.fetchAllEmployeesPayslipData(requestDTO, requestId);
    }

    @POST
    @Path("/create")
    public ApiResponse createSalaryStructure(EmployeeSalaryStructureJsonb apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        return salaryStructureControl.createSalaryStructure(apiRequest, requestId);
    }

    @PATCH
    @Path("/update/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateSalaryStructure(@PathParam("employeeId") Integer employeeId,
                                                 ApiRequest<EmployeeSalaryStructureJsonb> apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        return salaryStructureControl.updateSalaryStructure(employeeId, apiRequest, requestId);
    }

    @PATCH
    @Path("ApprovalStatus/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateApprovalStatus(@PathParam("employeeId") Integer employeeId, ApiRequest<EmployeeSalaryStructureJsonb> apiRequest) {
        String requestId = UUID.randomUUID().toString();
        log.info("RequestId: {} | Update Employee Approval Status Request: EmployeeId: {}", requestId, employeeId);

        if (apiRequest == null || apiRequest.getData() == null) {
            log.error("Null request or data - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Request or data cannot be null", requestId)
            );
        }

        String approvalStatus = apiRequest.getData().getApprovalStatus();
        if (approvalStatus == null || approvalStatus.isEmpty()) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Approval status is required", requestId)
            );
        }

        return salaryStructureControl.updateApprovalStatus(employeeId, approvalStatus, requestId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/salary/dashboard")
    public ApiResponse getSalaryDashboard() throws BusinessException {
        String correlationId = UUID.randomUUID().toString();
        log.info("correlation_Id: {} | Fetching salary dashboard", correlationId);
        return salaryStructureControl.fetchSalaryDashboard(correlationId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/salary_report")
    public ApiResponse getSalaryReport(SalaryReportJsonb salaryReportJsonb) throws BusinessException {
        String correlationId = UUID.randomUUID().toString();
        log.info("correlation_Id: {} | Fetching salary report", correlationId);
        return salaryStructureControl.fetchSalaryReport(salaryReportJsonb,correlationId);
    }

}
