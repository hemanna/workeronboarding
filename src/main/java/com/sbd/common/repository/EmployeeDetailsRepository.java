package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeDetailsRepository implements PanacheRepository<EmployeeDetails> {


    public EmployeeDetails findById(Integer employeeId) {
        return find("id", employeeId).firstResult();
    }

    public EmployeeDetails findByAadharNumber(String aadharNumber) {
        return find("aadharNumber", aadharNumber).firstResult();
    }

}
