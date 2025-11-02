package com.sbd.common.mapper;

import com.sbd.common.Jsonb.AssetDTO;
import com.sbd.common.entity.Asset;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    // ✅ Map Asset entity → AssetDTO using Base64 encoded images
    @Mapping(target = "assetImagesBase64", expression = "java(mapImagesToBase64(asset.getAssetImage()))")
    AssetDTO toDTO(Asset asset);

    // ✅ Convert entity list → DTO list
    default List<AssetDTO> toDTOList(List<Asset> assets) {
        return assets.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ✅ Convert comma-separated filenames to Base64-encoded image data
    default List<String> mapImagesToBase64(String assetImage) {
        if (assetImage == null || assetImage.isEmpty()) return null;

        return Arrays.stream(assetImage.split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(fileName -> {
                    try {
                        Path path = Path.of("uploads/assets", fileName);
                        byte[] bytes = Files.readAllBytes(path);
                        return Base64.getEncoder().encodeToString(bytes);
                    } catch (IOException e) {
                        return "Error encoding " + fileName;
                    }
                })
                .collect(Collectors.toList());
    }
}
