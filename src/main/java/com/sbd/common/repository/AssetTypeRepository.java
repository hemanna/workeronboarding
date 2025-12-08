package com.sbd.common.repository;

import com.sbd.common.entity.AssetType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@ApplicationScoped
public class AssetTypeRepository implements PanacheRepository<AssetType> {
    public AssetType findById(Integer typeId) {
        return find("id", typeId).firstResult();
    }

    public List<AssetType> listAssetTypes(int pageIndex, int pageSize) {
        return find(QueryEnum.QUERY_LIST_ASSETTYPES.getValue())
                .page(pageIndex, pageSize)
                .list();
    }



    @Getter
    @AllArgsConstructor
    public enum QueryEnum {
        QUERY_LIST_ASSETTYPES("SELECT at FROM AssetType at ORDER BY at.typeId DESC");
        private final String value;

    }
}
