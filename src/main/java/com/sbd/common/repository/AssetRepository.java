package com.sbd.common.repository;

import com.sbd.common.Jsonb.AssetStatusCountDTO;
import com.sbd.common.entity.Asset;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssetRepository implements PanacheRepository<Asset>{

    @Inject
    EntityManager em;

    public Asset findByAssetTag(String assetTag) {
        return find("assetTag", assetTag).firstResult();
    }
    // Count all assets
    public long countAllAssets() {
        return count();
    }

    // Count assets by status
    public long countByStatus(String status) {
        return count("status", status);
    }

    // Get counts for all statuses and total using ROLLUP
    public List<AssetStatusCountDTO> getStatusWiseAssetCount() {
        List<Object[]> results = em.createNativeQuery(
                "SELECT COALESCE(status, 'Total') AS status, COUNT(*) AS count " +
                        "FROM assets " +
                        "GROUP BY status WITH ROLLUP"
        ).getResultList();

        return results.stream()
                .map(obj -> new AssetStatusCountDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    public enum QueryEnum {
        QUERY_LIST_ALL("SELECT a FROM Asset a ORDER BY a.id DESC"),
        ASSET_TAG("assetTag"),
        ASSET_STATUS("status");

        private final String value;
    }
}
