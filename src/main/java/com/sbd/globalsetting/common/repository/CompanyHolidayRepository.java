package com.sbd.globalsetting.common.repository;

import com.sbd.globalsetting.common.entity.CompanyHolidays;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CompanyHolidayRepository
        implements PanacheRepository<CompanyHolidays> {

    public List<CompanyHolidays> findAllHolidays() {

        return find(
                "ORDER BY holidayDate ASC"
        ).list();
    }

    public List<CompanyHolidays> findByCalendar(
            String holidayCalendar
    ) {

        return find(
                "holidayCalendar = ?1 ORDER BY holidayDate ASC",
                holidayCalendar
        ).list();
    }
}