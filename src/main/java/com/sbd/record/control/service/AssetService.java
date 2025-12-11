package com.sbd.record.control.service;

import com.sbd.common.Jsonb.*;
import com.sbd.common.entity.Asset;
import com.sbd.common.entity.AssetType;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.enums.AssetActionEnum;
import com.sbd.common.enums.LogEnum;
import com.sbd.common.enums.StatusCodeEnum;
import com.sbd.common.exception.BusinessException;
import com.sbd.common.mapper.AssetMapper;
import com.sbd.common.mapper.AssetTypeMapper;
import com.sbd.common.mapper.mapperimpl.AssetMapperImpl;
import com.sbd.common.repository.AssetRepository;
import com.sbd.common.repository.AssetTypeRepository;
import com.sbd.common.repository.EmployeeDetailsRepository;
import com.sbd.common.request.ApiRequest;
import com.sbd.common.request.Pagination;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class AssetService implements AssetControl {

    @Inject
    AssetRepository assetRepository;

    @Inject
    AssetTypeRepository assetTypeRepository;

    @Inject
    EmployeeDetailsRepository employeeDetailsRepository;

    @Inject
    AssetMapperImpl assetMapper;

    private static final String UPLOAD_DIR = "uploads/assets";

    @Override
    @Transactional
    public ApiResponse createAsset(
            String correlationId,
            ApiRequest<AssetJsonb> apiRequest) throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_SAVE.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        AssetJsonb assetJsonb = apiRequest.getData();

        // assetType is mandatory now
        if (assetJsonb == null || assetJsonb.getAssetType() == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    "Asset Type is required"
            );
        }

        // Check if assetTag provided, then check duplicate
        if (assetJsonb.getAssetTag() != null && !assetJsonb.getAssetTag().isBlank()) {
            Asset existing = assetRepository.findByAssetTag(assetJsonb.getAssetTag());
            if (existing != null) {
                return new ApiResponse(
                        new Status(
                                Response.Status.CONFLICT.getStatusCode(),
                                StatusCodeEnum.CONFLICT.getValue(),
                                correlationId
                        )
                );
            }
        }

        // Validate AssetType
        AssetType type = assetTypeRepository.findById(assetJsonb.getAssetType());
        if (type == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    "Invalid asset type"
            );
        }

        // Ensure upload directory exists
        try {
            Files.createDirectories(Path.of(UPLOAD_DIR));
        } catch (IOException e) {
            throw new BusinessException(
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.TECHNICAL_FAILURE.getValue()
            );
        }

        // File Upload
        List<FileUpload> files = assetJsonb.getFiles();
        StringBuilder imagePaths = new StringBuilder();

        if (files != null && !files.isEmpty()) {
            for (FileUpload file : files) {
                try {
                    String name = UUID.randomUUID() + "_" + file.fileName();
                    Path dest = Path.of(UPLOAD_DIR, name);

                    Files.copy(file.uploadedFile(), dest, StandardCopyOption.REPLACE_EXISTING);
                    imagePaths.append(name).append(",");

                } catch (IOException e) {
                    throw new BusinessException(
                            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            correlationId,
                            StatusCodeEnum.TECHNICAL_FAILURE.getValue()
                    );
                }
            }
        }

        // CREATE Asset Entity
        Asset asset = new Asset();
        asset.setAssetTag(assetJsonb.getAssetTag());
        asset.setAssetName(assetJsonb.getAssetName());
        asset.setAssetType(type);
        asset.setBrand(assetJsonb.getBrand());
        asset.setModel(assetJsonb.getModel());
        asset.setSerialNumber(assetJsonb.getSerialNumber());
        asset.setVendor(assetJsonb.getVendor());
        asset.setStatus(assetJsonb.getStatus());
        asset.setAssetImage(imagePaths.toString());
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());

        if (assetJsonb.getPurchaseDate() != null && !assetJsonb.getPurchaseDate().isBlank()) {
            asset.setPurchaseDate(LocalDate.parse(assetJsonb.getPurchaseDate()));
        }

        if (assetJsonb.getWarrantyExpiry() != null && !assetJsonb.getWarrantyExpiry().isBlank()) {
            asset.setWarrantyExpiry(LocalDate.parse(assetJsonb.getWarrantyExpiry()));
        }

        assetRepository.persist(asset);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_SAVE.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }


    @Override
    @Transactional
    public ApiResponse updateAsset(
            Integer id,
            String correlationId,
            ApiRequest<AssetJsonb> apiRequest) throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_UPDATE.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        AssetJsonb assetJsonb = apiRequest.getData();
        if (assetJsonb == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    "Invalid request"
            );
        }

        // Fetch existing asset
        Asset asset = assetRepository.findById(id);
        if (asset == null) {
            throw new BusinessException(
                    Response.Status.NOT_FOUND.getStatusCode(),
                    correlationId,
                    "Asset not found"
            );
        }

        // Validate AssetType
        if (assetJsonb.getAssetType() == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    "Asset Type is required"
            );
        }

        AssetType type = assetTypeRepository.findById(assetJsonb.getAssetType());
        if (type == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    "Invalid asset type"
            );
        }

        // Ensure upload directory exists
        try {
            Files.createDirectories(Path.of(UPLOAD_DIR));
        } catch (IOException e) {
            throw new BusinessException(
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.TECHNICAL_FAILURE.getValue()
            );
        }

// IMAGE HANDLING
        String existingImages = asset.getAssetImage() != null ? asset.getAssetImage() : "";
        List<String> currentImages = Arrays.stream(existingImages.split(","))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

// Remove deleted images
        if (assetJsonb.getRemovedImages() != null && !assetJsonb.getRemovedImages().isEmpty()) {
            for (FileUpload removedFile : assetJsonb.getRemovedImages()) {
                String fileName = removedFile.fileName(); // original filename
                currentImages.remove(fileName);
                try {
                    Files.deleteIfExists(Path.of(UPLOAD_DIR, fileName));
                } catch (IOException ignored) {}
            }
        }


// Add new uploaded files
        if (assetJsonb.getFiles() != null && !assetJsonb.getFiles().isEmpty()) {
            for (FileUpload file : assetJsonb.getFiles()) {
                try {
                    String name = UUID.randomUUID() + "_" + file.fileName();
                    Path dest = Path.of(UPLOAD_DIR, name);
                    Files.copy(file.uploadedFile(), dest, StandardCopyOption.REPLACE_EXISTING);
                    currentImages.add(name);
                } catch (IOException e) {
                    throw new BusinessException(
                            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            correlationId,
                            "Failed to upload file"
                    );
                }
            }
        }


        // UPDATE OTHER FIELDS
        if (assetJsonb.getAssetTag() != null) asset.setAssetTag(assetJsonb.getAssetTag());
        if (assetJsonb.getAssetName() != null) asset.setAssetName(assetJsonb.getAssetName());
        asset.setAssetType(type);

        if (assetJsonb.getBrand() != null) asset.setBrand(assetJsonb.getBrand());
        if (assetJsonb.getModel() != null) asset.setModel(assetJsonb.getModel());
        if (assetJsonb.getSerialNumber() != null) asset.setSerialNumber(assetJsonb.getSerialNumber());
        if (assetJsonb.getVendor() != null) asset.setVendor(assetJsonb.getVendor());
        if (assetJsonb.getStatus() != null) asset.setStatus(assetJsonb.getStatus());

        if (assetJsonb.getPurchaseCost() != null && !assetJsonb.getPurchaseCost().isBlank()) {
            asset.setPurchaseCost(new BigDecimal(assetJsonb.getPurchaseCost()));
        }

        if (assetJsonb.getPurchaseDate() != null && !assetJsonb.getPurchaseDate().isBlank()) {
            asset.setPurchaseDate(LocalDate.parse(assetJsonb.getPurchaseDate()));
        }

        if (assetJsonb.getWarrantyExpiry() != null && !assetJsonb.getWarrantyExpiry().isBlank()) {
            asset.setWarrantyExpiry(LocalDate.parse(assetJsonb.getWarrantyExpiry()));
        }

        // Save final image list
        asset.setAssetImage(String.join(",", currentImages));
        asset.setUpdatedAt(LocalDateTime.now());

        assetRepository.persist(asset);
        assetRepository.flush();

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_UPDATE.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }





    @Override
    @Transactional
    public ApiResponse fetchAllAssets(String requestId) throws BusinessException {
        log.info("Start fetching all assets - RequestId: {}", requestId);

        // Fetch all asset records from the db
        List<Asset> assets = assetRepository.listAll();
        if (assets == null || assets.isEmpty()) {
            log.warn("No assets found - RequestId: {}", requestId);
            return new ApiResponse(
                    new Status(Response.Status.NOT_FOUND.getStatusCode(), "No assets found", requestId)
            );
        }

        // Map entity to DTO using MapStruct
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


            Map<String, Long> assetTypeCounts = new HashMap<>();
            for (Object[] row : results) {
                String type = (String) row[0];
                Long count = ((Number) row[1]).longValue();
                assetTypeCounts.put(type, count);
            }

            log.info("End fetching asset counts by type - RequestId: {}", requestId);


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

    @Override
    public ApiResponse listAssets(String correlationId, ApiRequest<AssetListRequest> apiRequest) throws BusinessException {

        log.info( LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue() );

        // Validate the data
        AssetListRequest assetListRequest = apiRequest.getData();
        if (assetListRequest == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }

        // Validate pagination
        Pagination pagination = assetListRequest.getPagination();
        if (pagination == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }

        String type = assetListRequest.getType();

        // Fetch assets based on type filter
        List<Asset> assets;
        if (type != null && !type.equalsIgnoreCase("ALL")) {

            assets = assetRepository.listByType(type, pagination.getPageIndex(), pagination.getPageSize());

            // LIKE search
            if (assets.isEmpty()) {
                assets = assetRepository.listByTypeLike(type, pagination.getPageIndex(), pagination.getPageSize());
            }

        } else {
            // Fetch all assets
            assets = assetRepository.listAllAssets(pagination.getPageIndex(), pagination.getPageSize());
        }

        if (assets == null || assets.isEmpty()) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        // Convert to DTOs
        List<AssetDTO> assetDTOs = AssetMapper.INSTANCE.toDTOList(assets);

        log.info( LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue() );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                assetDTOs
        );
    }

    @Override
    @Transactional
    public ApiResponse createAssetType(
            String correlationId,
            ApiRequest<AssetTypeJsonb> apiRequest) throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_TYPE_SAVE.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        // VALIDATE DATA
        AssetTypeJsonb data = apiRequest.getData();

        if (data == null ||
                data.getTypeId() == null ||
                data.getTypeName() == null ||
                data.getTypeName().isBlank()) {

            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }


        // CHECK DUPLICATE type_id
        AssetType existingByTypeId = assetTypeRepository.find("typeId", data.getTypeId()).firstResult();
        if (existingByTypeId != null) {
            return new ApiResponse(
                    new Status(
                            Response.Status.CONFLICT.getStatusCode(),
                            "TYPE_ID_ALREADY_EXISTS",
                            correlationId
                    )
            );
        }


        // CHECK DUPLICATE type_name
        AssetType existingByName = assetTypeRepository.find("typeName", data.getTypeName()).firstResult();
        if (existingByName != null) {
            return new ApiResponse(
                    new Status(
                            Response.Status.CONFLICT.getStatusCode(),
                            "TYPE_NAME_ALREADY_EXISTS",
                            correlationId
                    )
            );
        }

        // CREATE ENTITY
        AssetType newType = new AssetType();
        newType.setTypeId(data.getTypeId());
        newType.setTypeName(data.getTypeName());

        // SAVE
        assetTypeRepository.persist(newType);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_TYPE_SAVE.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }

    @Override
    @Transactional
    public ApiResponse createAssignasset(
            String correlationId,
            ApiRequest<AssetAssignJsonb> apiRequest) throws BusinessException {

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_ASSIGN_SAVE.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        //Validate data
        AssetAssignJsonb data = apiRequest.getData();

        if (data == null ||
                data.getAssetId() == null ||
                data.getEmployeeId() == null ||
                data.getAssignDate() == null) {

            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }

        //Validate Asset
        Asset asset = assetRepository.find("assetId", data.getAssetId()).firstResult();
        if (asset == null) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NOT_FOUND.getStatusCode(),
                            "ASSET_NOT_FOUND",
                            correlationId
                    )
            );
        }

        // Validate Employee
        EmployeeDetails employee = employeeDetailsRepository.find("id", data.getEmployeeId()).firstResult();
        if (employee == null) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NOT_FOUND.getStatusCode(),
                            "EMPLOYEE_NOT_FOUND",
                            correlationId
                    )
            );
        }

        // Prevent Re-Assigning Already Assigned Asset
        if (asset.getEmployee() != null) {
            return new ApiResponse(
                    new Status(
                            Response.Status.CONFLICT.getStatusCode(),
                            "ASSET_ALREADY_ASSIGNED",
                            correlationId
                    )
            );
        }

        // Save Asset Assign
        asset.setEmployee(employee);
        asset.setAssignDate(data.getAssignDate());
        assetRepository.persist(asset);

        log.info(
                LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_ASSIGN_SAVE.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                )
        );
    }

    @Override
    public ApiResponse getAllAssetTypes(String correlationId, ApiRequest<AssetListRequest> apiRequest)
            throws BusinessException {

        log.info(LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_TYPE_LIST.getValue(),
                LogEnum.LogMessage.STARTED.getValue()
        );

        // Validate request
        AssetListRequest assetListRequest = apiRequest.getData();
        if (assetListRequest == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }

        // Validate pagination
        Pagination pagination = assetListRequest.getPagination();
        if (pagination == null) {
            throw new BusinessException(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    correlationId,
                    StatusCodeEnum.REQUIRED_FIELDS_MISSING.getValue()
            );
        }

        // Fetch all asset types
        List<AssetType> assetTypes =
                assetTypeRepository.listAssetTypes(pagination.getPageIndex(), pagination.getPageSize());

        if (assetTypes == null || assetTypes.isEmpty()) {
            return new ApiResponse(
                    new Status(
                            Response.Status.NO_CONTENT.getStatusCode(),
                            StatusCodeEnum.NO_CONTENT.getValue(),
                            correlationId
                    )
            );
        }

        // Convert entities to Jsonb
        List<AssetTypeJsonb> typeDTOs = AssetTypeMapper.INSTANCE.toDTOList(assetTypes);

        log.info(LogEnum.ACTIVITY.getValue(),
                correlationId,
                AssetActionEnum.ASSET_TYPE_LIST.getValue(),
                LogEnum.LogMessage.ENDED.getValue()
        );

        return new ApiResponse(
                new Status(
                        Response.Status.OK.getStatusCode(),
                        StatusCodeEnum.SUCCESS.getValue(),
                        correlationId
                ),
                typeDTOs
        );
    }


}


