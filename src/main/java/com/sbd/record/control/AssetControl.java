package com.sbd.record.control;

import com.sbd.common.Jsonb.*;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.response.ApiResponse;

public interface AssetControl {
    ApiResponse createAsset(
            String correlationId,
            ApiRequest<AssetJsonb> apiRequest)
            throws BusinessException;

    ApiResponse updateAsset(
            Integer id, String correlationId,
            ApiRequest<AssetDTO> apiRequest)
            throws BusinessException;

    ApiResponse fetchAllAssets
            (String requestId)
            throws BusinessException;

    ApiResponse fetchCountAssets
            (String requestId)
            throws BusinessException;

    ApiResponse fetchAssetByType(
            String requestId )
            throws BusinessException;

    ApiResponse listAssets(
            String correlationId,
            ApiRequest<AssetListRequest> apiRequest)
            throws BusinessException;

    ApiResponse createAssetType(
            String correlationId,
            ApiRequest<AssetTypeJsonb> apiRequest)
            throws BusinessException;

    ApiResponse createAssignasset(
            String correlationId,
            ApiRequest<AssetAssignJsonb> apiRequest)
            throws BusinessException;

    ApiResponse getAllAssetTypes(
            String correlationId,
            ApiRequest<AssetListRequest> apiRequest)
            throws BusinessException;

//    ApiResponse updateAssetImages(
//            String correlationId,
//            ApiRequest<AssetImagesJsonb> apiRequest
//    ) throws BusinessException;

}
