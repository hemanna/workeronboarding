package com.sbd.globalsetting.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.SecuritySettingsJsonb;

public interface SecuritySettingsControl {
    ApiResponse insertSecuritySettings(
            SecuritySettingsJsonb securitySettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;
}
