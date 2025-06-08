package com.sbd.common.repository;

import com.sbd.common.entity.PayrollComponent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PayrollComponentRepository implements PanacheRepository<PayrollComponent> {
}
