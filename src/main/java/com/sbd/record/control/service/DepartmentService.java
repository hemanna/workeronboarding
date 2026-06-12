package com.sbd.record.control.service;

import com.sbd.common.entity.Department;
import com.sbd.common.enums.DepartmentActionEnum;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.repository.DepartmentRepository;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.DepartmentControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class DepartmentService implements DepartmentControl {

    @Inject
    DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public ApiResponse fetchAllDepartments(String correlationId) throws BusinessException {
            log.info(
                    LogEnum.ACTIVITY.getValue(),
                    correlationId,
                    DepartmentActionEnum.DEPARTMENT_LIST.getValue(),
                    LogEnum.LogMessage.STARTED.getValue()
            );

            List<Department> departmentList =
                    departmentRepository.listAllDepartments();

            if (departmentList == null || departmentList.isEmpty()) {

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
                    DepartmentActionEnum.DEPARTMENT_LIST.getValue(),
                    LogEnum.LogMessage.ENDED.getValue()
            );

            return new ApiResponse(
                    new Status(
                            Response.Status.OK.getStatusCode(),
                            StatusCodeEnum.SUCCESS.getValue(),
                            correlationId
                    ),
                    departmentList
            );
        }
}

