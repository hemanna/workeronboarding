package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.request.Pagination;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EmployeeDetailsRepository implements PanacheRepository<EmployeeDetails> {


    public EmployeeDetails findById(Integer employeeId) {
        return find("id", employeeId).firstResult();
    }

    public EmployeeDetails findByAadharNumber(String aadharNumber) {
        return find("aadharNumber", aadharNumber).firstResult();
    }

    public List<EmployeeDetails> listAll(Pagination pagination) {
        return findAll()
                .page(pagination.getPageIndex() - 1, pagination.getPageSize())
                .list();
    }
    public List<EmployeeDetails> listByName(String name, Pagination pagination) {
        return find("employeeName LIKE ?1", "%" + name + "%")
                .page(pagination.getPageIndex() - 1, pagination.getPageSize())
                .list();
    }
    // Delete employee by ID
    public boolean deleteById(Long employeeId) {
        return delete("id", employeeId) > 0;
    }

}
