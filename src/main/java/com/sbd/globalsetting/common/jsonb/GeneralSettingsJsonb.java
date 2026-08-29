package com.sbd.globalsetting.common.jsonb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSettingsJsonb {
    private Integer id;

    private String companyName;

    private String companyCode;

    private String organizationType;

    private String currency;

    private String timeZone;

    private String language;

    private String dateFormat;

    private String timeFormat;
}
