package com.sbd.common.repository;

import com.sbd.common.entity.Asset;
import com.sbd.common.entity.AssetRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AssetRequestRepository implements PanacheRepository<AssetRequest> {
}
