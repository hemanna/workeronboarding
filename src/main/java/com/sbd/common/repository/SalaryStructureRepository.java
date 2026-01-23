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

    public Map<String, Object> fetchPayrollSummary(Integer month, Integer year) {

        String query;
        Object[] r;

        if (month != null) {
            // Monthly summary
            query = QueryEnum.MONTHLY_PAYROLL_SUMMARY.getValue();
            r = (Object[]) em.createNativeQuery(query)
                    .setParameter(1, month)
                    .setParameter(2, year)
                    .getSingleResult();
        } else {
            // Yearly summary
            query = QueryEnum.YEARLY_PAYROLL_SUMMARY.getValue();
            r = (Object[]) em.createNativeQuery(query)
                    .setParameter(1, year)
                    .getSingleResult();
        }

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
            ),
        MONTHLY_PAYROLL_SUMMARY(
                "SELECT " +
                        "IFNULL(SUM(gross_salary),0), " +
                        "IFNULL(SUM(gross_salary - net_salary),0), " +
                        "IFNULL(SUM(net_salary),0), " +
                        "COUNT(DISTINCT employee_id) " +
                        "FROM payroll " +
                        "WHERE month = ?1 " +
                        "AND year = ?2 " +
                        "AND status = 'GENERATED'"
        ),

        YEARLY_PAYROLL_SUMMARY(
                "SELECT " +
                        "IFNULL(SUM(gross_salary),0), " +
                        "IFNULL(SUM(gross_salary - net_salary),0), " +
                        "IFNULL(SUM(net_salary),0), " +
                        "COUNT(DISTINCT employee_id) " +
                        "FROM payroll " +
                        "WHERE year = ?1 " +
                        "AND status = 'GENERATED'"
        );


        private final String value;
        }

    }


