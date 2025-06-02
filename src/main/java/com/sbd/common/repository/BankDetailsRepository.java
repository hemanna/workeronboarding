package com.sbd.common.repository;

import com.sbd.common.entity.BankDetails;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.Skill;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BankDetailsRepository implements PanacheRepository<BankDetails> {
    public BankDetails findByEmployeeId(Integer employeeId) {
        return find("employeeId.id", employeeId).firstResult();
    }

}
