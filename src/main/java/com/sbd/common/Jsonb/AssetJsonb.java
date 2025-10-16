package com.sbd.common.Jsonb;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.PartType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AssetJsonb {
    @FormParam("assetTag")
    private String assetTag;

    @FormParam("assetName")
    private String assetName;

    @FormParam("assetType")
    private String assetType;

    @FormParam("brand")
    private String brand;

    @FormParam("model")
    private String model;

    @FormParam("serialNumber")
    private String serialNumber;

    @FormParam("purchaseDate")
    private LocalDate purchaseDate;

    @FormParam("purchaseCost")
    private BigDecimal purchaseCost;

    @FormParam("vendor")
    private String vendor;

    @FormParam("warrantyExpiry")
    private LocalDate warrantyExpiry;

    @FormParam("status")
    private String status;

    @FormParam("assetImage")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] assetImage;

}
