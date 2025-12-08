package com.sbd.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetActionEnum {
    ASSET_LIST("Asset List"),
    ASSET_SAVE("Asset Save"),
    ASSET_TYPE_SAVE("Asset Save"),
    ASSET_UPDATE("Asset Update"),
    ASSET_DELETE("Asset Delete"),
    ASSET_ASSIGN_SAVE("Assign Asset Save"),
    ASSET_TYPE_LIST("Asset Type List"),
    ASSET_DETAIL("Asset Detail");

    private final String value;
}