package com.sbd.common.repository;

import com.sbd.common.entity.SalaryStructure;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SalaryStructureRepository implements PanacheRepository<SalaryStructure> {
}
