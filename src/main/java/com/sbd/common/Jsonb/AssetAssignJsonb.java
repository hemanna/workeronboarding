package com.sbd.common.Jsonb;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssetAssignJsonb {
    private Integer assetId;
    private Integer employeeId;
    private LocalDate assignDate;
}
