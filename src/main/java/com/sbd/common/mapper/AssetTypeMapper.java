package com.sbd.common.mapper;

import com.sbd.common.Jsonb.AssetTypeJsonb;
import com.sbd.common.entity.AssetType;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper
public interface AssetTypeMapper {
    AssetTypeMapper INSTANCE = Mappers.getMapper(AssetTypeMapper.class);

    AssetTypeJsonb toDTO(AssetType assetType);

    default List<AssetTypeJsonb> toDTOList(List<AssetType> types) {
        return types == null ? List.of() :
                types.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
