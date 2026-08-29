package com.sbd.globalsetting.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.AdvancedSettingsJsonb;

public interface AdvancedSettingsControl {

    ApiResponse insertAdvancedSettings(
            AdvancedSettingsJsonb advancedSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;
}
