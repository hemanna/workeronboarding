package com.sbd.common.repository;

import com.sbd.common.entity.AssetType;
import com.sbd.common.entity.EmployeeDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetTypeRepository implements PanacheRepository<AssetType> {
    public AssetType findById(Integer typeId) {
        return find("id", typeId).firstResult();
    }

}
