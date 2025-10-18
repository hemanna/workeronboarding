package com.sbd.record.control.service;

import com.sbd.common.Jsonb.AssetDTO;
import com.sbd.common.Jsonb.AssetJsonb;
import com.sbd.common.Jsonb.AssetStatusCountDTO;
import com.sbd.common.entity.Asset;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.AssetMapper;
import com.sbd.common.repository.AssetRepository;
import com.sbd.common.response.ApiResponse;
import com.sbd.common.response.Status;
import com.sbd.record.control.AssetControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class AssetService implements AssetControl {

    @Inject
    AssetRepository assetRepository;

@Override
@Transactional
    public ApiResponse createAsset(AssetJsonb assetRequest, String requestId) throws BusinessException {

        // Check if assetTag already exists
        Asset existing = assetRepository.findByAssetTag(assetRequest.getAssetTag());
        if (existing != null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(), "Asset tag already exists", requestId)
            );
        }

        // Create Asset entity
        Asset asset = new Asset();
        asset.setAssetTag(assetRequest.getAssetTag());
        asset.setAssetName(assetRequest.getAssetName());
        asset.setAssetType(assetRequest.getAssetType());
        asset.setBrand(assetRequest.getBrand());
        asset.setModel(assetRequest.getModel());
        asset.setSerialNumber(assetRequest.getSerialNumber());
        asset.setPurchaseDate(assetRequest.getPurchaseDate());
        asset.setPurchaseCost(assetRequest.getPurchaseCost());
        asset.setVendor(assetRequest.getVendor());
        asset.setWarrantyExpiry(assetRequest.getWarrantyExpiry());
        asset.setStatus(assetRequest.getStatus());
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());

        // Save first image (asset image)
        if (assetRequest.getAssetImage() != null) {
            asset.setAssetImage(new String(assetRequest.getAssetImage())); // store as String, or use byte[] if preferred
        }

        // Save second image if available (example: handover image)
        // Assuming you extend AssetRequest to have byte[] handoverImage
        // asset.setHandoverImage(assetRequest.getHandoverImage() != null ? new String(assetRequest.getHandoverImage()) : null);

        // Persist entity
        assetRepository.persist(asset);
        assetRepository.flush();

        // Convert to DTO
        AssetDTO assetDTO = AssetMapper.INSTANCE.toDTO(asset);

//        // Prepare response
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("asset", assetDTO);

        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Asset created successfully", requestId)
        );
    }

    @Override
    public ApiResponse fetchAllAssets(String requestId) throws BusinessException {
        log.info("Start fetching all assets - RequestId: {}", requestId);

        // Fetch all asset records from the database
        List<Asset> assets = assetRepository.listAll();
        if (assets == null || assets.isEmpty()) {
            log.warn("No assets found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No assets found", requestId)
            );
        }

        // Map entity to DTO using MapStruct (recommended)
        List<AssetDTO> assetDTOList = assets.stream()
                .map(AssetMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());

        log.info("End fetching all assets - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Assets fetched successfully", requestId),
                assetDTOList
        );

    }
    @Transactional
    public ApiResponse fetchCountAssets(String requestId) {
        log.info("Start fetching counts assets - RequestId: {}", requestId);

        // Get counts grouped by status
        List<AssetStatusCountDTO> statusCounts = assetRepository.getStatusWiseAssetCount();

        // Prepare response
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("statusCounts", statusCounts);

        log.info("End fetching counts assets - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(), "Assets counts fetched successfully", requestId),
                responseData
        );
    }


}
