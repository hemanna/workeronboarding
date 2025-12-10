package com.sbd.common.repository;

import com.sbd.common.Jsonb.AssetStatusCountDTO;
import com.sbd.common.entity.Asset;
import com.sbd.common.entity.EmployeeDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssetRepository implements PanacheRepositoryBase<Asset,Integer> {

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

    public List<Object[]> getAssetTypeWiseCount() {
        return em.createNativeQuery(
                "SELECT at.type_name AS asset_type, COUNT(*) AS count " +
                        "FROM assets a " +
                        "LEFT JOIN asset_types at ON a.asset_type_id = at.type_id " +
                        "GROUP BY at.type_name"
        ).getResultList();
    }


    public List<Asset> listAllAssets(int pageIndex, int pageSize) {
        return find(QueryEnum.QUERY_LIST_ALLASSETS.getValue())
                .page(pageIndex, pageSize)
                .list();
    }

    public List<Asset> listByType(String type, int pageIndex, int pageSize) {
        return find(QueryEnum.QUERY_BY_TYPE.getValue(),
                Parameters.with("type", type.toLowerCase()))
                .page(pageIndex, pageSize)
                .list();
    }

    public List<Asset> listByTypeLike(String type, int pageIndex, int pageSize) {
        return find(QueryEnum.QUERY_BY_TYPE_LIKE.getValue(),
                Parameters.with("type", "%" + type.toLowerCase() + "%"))
                .page(pageIndex, pageSize)
                .list();
    }

    public long countActiveAssets() {
        return count(QueryEnum.QUERY_COUNT_ACTIVE.getValue());
    }

    @Getter
    @AllArgsConstructor
    public enum QueryEnum {
        QUERY_LIST_ALL("SELECT a FROM Asset a ORDER BY a.id DESC"),
        ASSET_TAG("assetTag"),
        ASSET_STATUS("status"),
        QUERY_LIST_ALLASSETS("SELECT a FROM Asset a ORDER BY a.assetId DESC"),
        QUERY_BY_TYPE("SELECT a FROM Asset a WHERE LOWER(a.assetType.typeName) = :type ORDER BY a.assetId DESC"),
        QUERY_BY_TYPE_LIKE("SELECT a FROM Asset a WHERE LOWER(a.assetType.typeName) LIKE :type ORDER BY a.assetId DESC"),
        QUERY_COUNT_ACTIVE("a.deleted = false"),
        IS_DELETED("isDeleted"),

        TYPE("type"),
        NAME("name");

        private final String value;
    }
}
