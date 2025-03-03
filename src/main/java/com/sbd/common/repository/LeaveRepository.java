package com.sbd.common.repository;

import com.sbd.common.entity.Leave;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

    public Leave findById(Integer id) {
        return find("id", id).firstResult();
    }

}
