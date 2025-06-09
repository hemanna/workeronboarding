package com.sbd.common.repository;

import com.sbd.common.entity.Payroll;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PayrollRepository implements PanacheRepository<Payroll> {

}
