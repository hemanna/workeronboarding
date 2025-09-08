package com.sbd.common.repository;

import com.sbd.common.entity.EmployeeAttendanceRegularization;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegularizedAttendanceRepository implements PanacheRepository<EmployeeAttendanceRegularization> {

}
