package com.sbd.record.control.service;

import com.sbd.common.enums.DepartmentActionEnum;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.RoleActionEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.repository.RoleRepository;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.RoleControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class RoleService implements RoleControl {

    @Inject
    RoleRepository roleRepository;

    @Override
    @Transactional
    public ApiResponse fetchAllRoles(String correlationId) throws BusinessException {
        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                RoleActionEnum.ROLE_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        var roleList = roleRepository.listAllRoles();

        if (roleList == null || roleList.isEmpty()) {
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
                RoleActionEnum.ROLE_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                roleList
        );
    }
}
