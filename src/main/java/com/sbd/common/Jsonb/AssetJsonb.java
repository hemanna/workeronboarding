package com.sbd.common.Jsonb;

import jakarta.ws.rs.FormParam;

import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

@Getter
@Setter
public class AssetJsonb {
    @FormParam("assetTag")
    private String assetTag;

    @FormParam("assetName")
    private String assetName;

    @FormParam("assetType")
    private Integer assetType;

    @FormParam("brand")
    private String brand;

    @FormParam("model")
    private String model;

    @FormParam("serialNumber")
    private String serialNumber;

    @FormParam("status")
    private String status;

    @FormParam("vendor")
    private String vendor;

    @FormParam("purchaseDate")
    private String purchaseDate;

    @FormParam("warrantyExpiry")
    private String warrantyExpiry;

    @FormParam("purchaseCost")
    private String purchaseCost;

    @FormParam("files")
    private List<FileUpload> files;

    @FormParam("removedImages")
    private List<FileUpload> removedImages;
}
