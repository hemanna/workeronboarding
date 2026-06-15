package com.sbd.record.control.service;

import com.sbd.common.enums.LeaveTypeEnum;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.repository.LeaveTypeRepository;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.LeaveTypeControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class LeaveTypeService implements LeaveTypeControl {

    @Inject
    LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public ApiResponse fetchAllLeaveTypes(String correlationId) throws BusinessException {
        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                LeaveTypeEnum.LEAVE_TYPE_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        var leaveTypeList =
                leaveTypeRepository.listAllLeaveTypes();

        if (leaveTypeList == null || leaveTypeList.isEmpty()) {

            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                LeaveTypeEnum.LEAVE_TYPE_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                leaveTypeList
        );
    }

}
