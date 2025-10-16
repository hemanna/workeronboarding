package com.sbd.common.Jsonb;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AssetDTO {
    private Integer assetId;
    private String assetTag;
    private String assetName;
    private String assetType;
    private String brand;
    private String model;
    private String serialNumber;
    private LocalDate purchaseDate;
    private BigDecimal purchaseCost;
    private String vendor;
    private LocalDate warrantyExpiry;
    private String status;
    private String assetImageUrl;
}
