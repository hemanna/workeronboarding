package com.sbd.record.boundary;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.EmployeeDTO;
import com.sbd.common.request.EmployeeDetailsRequest;
import com.sbd.common.response.ApiResponse;
import com.sbd.record.control.BankDetailsControl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Path("/Bank-Details")
@AllArgsConstructor
@Slf4j
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BankDetailsResource {

    private final BankDetailsControl bankDetailsControl;
    @POST
    @Path("/create")
    public ApiResponse createBankDetails(
            ApiRequest<BankDetailsJsonb> apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        return bankDetailsControl.bankHolderLogin(apiRequest, requestId);
    }

    @PATCH
    @Path("/update/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ApiResponse updateEmployeeBankDetails(@PathParam("employeeId") Integer employeeId,
                                          ApiRequest<BankDetailsJsonb> apiRequest) throws BusinessException {
        String requestId = UUID.randomUUID().toString();
        return bankDetailsControl.updateBankDetailsRequest(employeeId, apiRequest, requestId);
    }
}
