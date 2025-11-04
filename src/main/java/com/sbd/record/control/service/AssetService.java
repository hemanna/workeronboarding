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
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class AssetService implements AssetControl {

    @Inject
    AssetRepository assetRepository;

    private static final String UPLOAD_DIR = "uploads/assets";

    @Override
    @Transactional
    public ApiResponse createAsset(AssetJsonb assetJsonb, String requestId) throws BusinessException {
        log.info("Start creating asset - RequestId: {}", requestId);

        // ✅ Check if asset tag exists
        Asset existing = assetRepository.findByAssetTag(assetJsonb.getAssetTag());
        if (existing != null) {
            return new ApiResponse(
                    new Status(Response.Status.BAD_REQUEST.getStatusCode(),
                            "Asset tag already exists", requestId)
            );
        }

        // ✅ Ensure upload directory exists
        try {
            Files.createDirectories(Path.of(UPLOAD_DIR));
        } catch (IOException e) {
            throw new BusinessException("Could not create upload directory");
        }

        //  Save uploaded images
        List<FileUpload> files = assetJsonb.getFiles();
        StringBuilder imagePaths = new StringBuilder();

        if (files != null && !files.isEmpty()) {
            for (FileUpload file : files) {
                try {
                    String uniqueFileName = UUID.randomUUID() + "_" + file.fileName();
                    Path dest = Path.of(UPLOAD_DIR, uniqueFileName);

                    //  Correct: uploadedFile() returns Path already
                    Files.copy(file.uploadedFile(), dest, StandardCopyOption.REPLACE_EXISTING);

                    imagePaths.append(uniqueFileName).append(",");
                } catch (IOException e) {
                    log.error("Error saving file: {}", file.fileName(), e);
                    throw new BusinessException("Error saving uploaded file: " + file.fileName());
                }
            }
        }

        //  Create and persist Asset
        Asset asset = new Asset();
        asset.setAssetTag(assetJsonb.getAssetTag());
        asset.setAssetName(assetJsonb.getAssetName());
        asset.setAssetType(assetJsonb.getAssetType());
        asset.setBrand(assetJsonb.getBrand());
        asset.setModel(assetJsonb.getModel());
        asset.setSerialNumber(assetJsonb.getSerialNumber());
        asset.setVendor(assetJsonb.getVendor());
        asset.setStatus(assetJsonb.getStatus());
        asset.setAssetImage(imagePaths.toString());
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());

        if (assetJsonb.getPurchaseDate() != null && !assetJsonb.getPurchaseDate().isEmpty()) {
            asset.setPurchaseDate(LocalDate.parse(assetJsonb.getPurchaseDate()));
        }
        if (assetJsonb.getWarrantyExpiry() != null && !assetJsonb.getWarrantyExpiry().isEmpty()) {
            asset.setWarrantyExpiry(LocalDate.parse(assetJsonb.getWarrantyExpiry()));
        }

        assetRepository.persist(asset);

        log.info("Asset created successfully - RequestId: {}", requestId);
        return new ApiResponse(
                new Status(Response.Status.OK.getStatusCode(),
                        "Asset created successfully", requestId)
        );
    }




    @Override
    @Transactional
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

    @Override
    @Transactional
    public ApiResponse fetchAssetByType( String requestId) throws BusinessException {
        log.info("Start fetching assets grouped by type - RequestId: {}", requestId);

        try {
            //Get counts of assets grouped by type
            List<Object[]> results = assetRepository.getAssetTypeWiseCount();

            //  Handle empty results
            if (results == null || results.isEmpty()) {
                log.warn("No assets found - RequestId: {}", requestId);
                return new ApiResponse(
                        new Status(Response.Status.NOT_FOUND.getStatusCode(),
                                "No assets found", requestId)
                );
            }

            // Convert List<Object[]> → Map<String, Long>
            Map<String, Long> assetTypeCounts = new HashMap<>();
            for (Object[] row : results) {
                String type = (String) row[0];
                Long count = ((Number) row[1]).longValue();
                assetTypeCounts.put(type, count);
            }

            log.info("End fetching asset counts by type - RequestId: {}", requestId);

            // Return final response
            return new ApiResponse(
                    new Status(Response.Status.OK.getStatusCode(),
                            "Assets grouped by type fetched successfully", requestId),
                    assetTypeCounts
            );

        } catch (Exception e) {
            log.error("Error fetching asset counts by type - RequestId: {}", requestId, e);
            throw new BusinessException("Failed to fetch asset counts by type: " + e.getMessage());
        }
    }



}
