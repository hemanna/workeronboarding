package com.sbd.common.repository;

import com.sbd.common.entity.SalaryStructure;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@ApplicationScoped
public class SalaryStructureRepository implements PanacheRepository<SalaryStructure> {
    @Inject
    EntityManager em;

    public Map<String, Object> fetchSalaryDashboardSummary() {

        Object[] r = (Object[]) em
                .createNativeQuery(QueryEnum.SALARY_DASHBOARD_SUMMARY.getValue())
                .getSingleResult();

        return Map.of(
                "totalGrossPay", r[0],
                "totalDeduction", r[1],
                "totalNetPay", r[2],
                "employeeCount", r[3]
        );
    }
        @Getter
        @AllArgsConstructor
        public enum QueryEnum {

            SALARY_DASHBOARD_SUMMARY(
                    "SELECT " +
                            "SUM(gross_salary), " +
                            "SUM(IFNULL(pf_contribution,0) + IFNULL(esi_contribution,0)), " +
                            "SUM(gross_salary - (IFNULL(pf_contribution,0) + IFNULL(esi_contribution,0))), " +
                            "COUNT(DISTINCT employee_id) " +
                            "FROM employee_salary_structure " +
                            "WHERE salary_status = 'Active' " +
                            "AND approval_status = 'Approved'"
            );

            private final String value;
        }

    }


