package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AssetStatusCountDTO {
    private String status;
    private long count;

}
