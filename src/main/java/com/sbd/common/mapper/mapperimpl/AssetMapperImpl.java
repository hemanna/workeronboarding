package com.sbd.common.mapper.mapperimpl;

import com.sbd.common.Jsonb.AssetDTO;
import com.sbd.common.entity.Asset;
import com.sbd.common.mapper.AssetMapper;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AssetMapperImpl {
    private final AssetMapper mapper = AssetMapper.INSTANCE;


    // Convert list of entities to DTOs
    public List<AssetDTO> toDTOList(List<Asset> assets) {
        if (assets == null || assets.isEmpty()) return List.of();
        return mapper.toDTOList(assets);
    }

    // Convert a single entity to DTO
    public AssetDTO toDTO(Asset asset) {
        if (asset == null) return null;
        return mapper.toDTO(asset);
    }


}
