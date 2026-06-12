package com.sbd.record.control.service;

import com.sbd.common.entity.Skill;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.RoleActionEnum;
import com.sbd.common.enums.SkillActionEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.repository.SkillRepository;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.SkillControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class SkillService implements SkillControl {

    @Inject
    SkillRepository skillRepository;

    @Override
    @Transactional
    public ApiResponse fetchAllSkills(String correlationId)
            throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                SkillActionEnum.SKILL_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        List<Skill> skillList =
                skillRepository.listAllSkills();

        if (skillList == null || skillList.isEmpty()) {
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
                SkillActionEnum.SKILL_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                skillList
        );
    }
}
