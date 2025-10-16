package com.sbd.common.mapper;

import com.sbd.common.Jsonb.AssetDTO;
import com.sbd.common.entity.Asset;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    // Map Asset entity → AssetDTO
    @Mapping(target = "assetImageUrl", source = "assetImage", qualifiedByName = "mapToBase64")
    AssetDTO toDTO(Asset asset);

    // Map list of entities → list of DTOs
    default List<AssetDTO> toDTOList(List<Asset> assets) {
        return assets.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Convert String → Base64 string (because assetImage in entity is String)
    @Named("mapToBase64")
    default String mapToBase64(String data) {
        if (data == null) return null;
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}
