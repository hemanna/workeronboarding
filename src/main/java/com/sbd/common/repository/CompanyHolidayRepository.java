package com.sbd.common.repository;

import com.sbd.common.entity.CompanyHoliday;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CompanyHolidayRepository implements PanacheRepository<CompanyHoliday> {
    public List<CompanyHoliday> listAllHolidays() {
        return listAll(Sort.by("holidayDate"));
    }

}
