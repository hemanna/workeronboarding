package com.sbd.common.repository;

import com.sbd.common.entity.Asset;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetRepository implements PanacheRepository<Asset>{
    public Asset findByAssetTag(String assetTag) {
        return find("assetTag", assetTag).firstResult();
    }

}
