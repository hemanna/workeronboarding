package com.sbd.common.repository;

import com.sbd.common.entity.SalaryStructure;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
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
    public List<Object[]> fetchDeductionReport(Integer month, Integer year) {

        return em.createNativeQuery(QueryEnum.DEDUCTION_REPORT.getValue())
                .setParameter(1, month)
                .setParameter(2, year)
                .getResultList();
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
        ),

        DEDUCTION_REPORT(
                "SELECT " +
                        "ed.employee_name AS name, " +

                        "SUM(CASE WHEN sc.component_code = 'PF' THEN esd.amount ELSE 0 END) AS pf, " +
                        "SUM(CASE WHEN sc.component_code = 'ESI' THEN esd.amount ELSE 0 END) AS esi, " +
                        "SUM(CASE WHEN sc.component_code = 'PT' THEN esd.amount ELSE 0 END) AS pt, " +
                        "SUM(CASE WHEN sc.component_code = 'LOP' THEN esd.amount ELSE 0 END) AS lop, " +

                        "0 AS advance, " +

                        "SUM(CASE WHEN sc.component_code NOT IN ('PF','ESI','PT','LOP') THEN esd.amount ELSE 0 END) AS other, " +

                        "SUM(esd.amount) AS total_deduction " +

                        "FROM employee_salary_deductions esd " +

                        "JOIN salary_component sc ON sc.id = esd.component_id " +
                        "JOIN employee_salary_structure ess ON ess.id = esd.salary_structure_id " +
                        "JOIN employee_details ed ON ed.id = ess.employee_id " +

                        "WHERE ess.salary_status='Active' " +
                        "AND ess.month = ?1 " +
                        "AND ess.year = ?2 " +

                        "GROUP BY ed.employee_name"
        );


        private final String value;
        }

    }


