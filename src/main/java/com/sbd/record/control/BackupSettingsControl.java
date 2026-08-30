package com.sbd.record.control;

import com.sbd.common.exception.BusinessException;
import com.sbd.common.exception.TechnicalException;
import com.sbd.common.response.ApiResponse;
import com.sbd.globalsetting.common.jsonb.BackupSettingsJsonb;

public interface BackupSettingsControl {

    ApiResponse insertBackupSettings(
            BackupSettingsJsonb backupSettingsJsonb,
            String correlationId
    ) throws BusinessException, TechnicalException;
}
