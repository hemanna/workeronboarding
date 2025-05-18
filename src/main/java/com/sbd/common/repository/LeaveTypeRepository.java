package com.sbd.common.repository;

import com.sbd.common.entity.LeaveType;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LeaveTypeRepository implements PanacheRepository<LeaveType> {

    public LeaveType findByType(String type) {
        return find("type", type)
                .firstResult();
    }
}
