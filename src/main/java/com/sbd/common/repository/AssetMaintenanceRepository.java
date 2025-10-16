package com.sbd.common.repository;

import com.sbd.common.entity.Asset;
import com.sbd.common.entity.AssetMaintenance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetMaintenanceRepository implements PanacheRepository<AssetMaintenance> {
}
