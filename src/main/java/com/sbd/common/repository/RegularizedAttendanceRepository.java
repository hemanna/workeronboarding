package com.sbd.common.repository;

import com.sbd.common.entity.BankDetails;
import com.sbd.common.entity.CompanyHoliday;
import com.sbd.common.entity.EmployeeAttendanceRegularization;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RegularizedAttendanceRepository implements PanacheRepository<EmployeeAttendanceRegularization> {

    public EmployeeAttendanceRegularization findByRegularizationId(Integer id) {
        return find("id", id).firstResult();
    }

//    public List<EmployeeAttendanceRegularization> listAllRegularizationAttendance() {
//        return listAll(Sort.by("date")); // or just listAll() if no sorting
//    }

    public List<EmployeeAttendanceRegularization> listAllRegularizationAttendance() {
        return listAll(Sort.by("date"));
    }


}
