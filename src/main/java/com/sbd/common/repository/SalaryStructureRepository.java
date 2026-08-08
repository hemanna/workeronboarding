package com.sbd.common.repository;

import com.sbd.common.Jsonb.MonthlyPayrollJsonb;
import com.sbd.common.Jsonb.PayrollStatusJsonb;
import com.sbd.common.Jsonb.PayrollSummaryJsonb;
import com.sbd.common.entity.SalaryStructure;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<PayrollStatusJsonb> fetchApprovedAttendanceStatus() {

        List<Object[]> resultList = em
                .createNativeQuery(
                        QueryEnum.FETCH_PAYROLL_MONTH_STATUS.getValue()
                )
                .getResultList();

        return resultList.stream()
                .map(row -> new PayrollStatusJsonb(
                        row[0] != null ? row[0].toString() : null,
                        row[1] != null ? row[1].toString() : null,
                        row[2] != null ? row[2].toString() : null
                ))
                .collect(Collectors.toList());
    }

    public List<MonthlyPayrollJsonb> fetchMonthlyPayroll(
            Integer month,
            Integer year) {

        List<Object[]> rows = em.createNativeQuery(
                        QueryEnum.GET_MONTHLY_PAYROLL.getValue())
                .setParameter(1, month)
                .setParameter(2, year)
                .getResultList();

        List<MonthlyPayrollJsonb> response = new ArrayList<>();

        for (Object[] row : rows) {

            response.add(

                    new MonthlyPayrollJsonb(

                            ((Number) row[0]).intValue(),

                            row[1].toString(),

                            BigDecimal.valueOf(((Number) row[2]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[3]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[4]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[5]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[6]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[7]).doubleValue()),

                            BigDecimal.valueOf(((Number) row[8]).doubleValue()),

                            row[9].toString()
                    )
            );
        }

        return response;
    }

    public PayrollSummaryJsonb fetchPayrollreport(
            Integer month,
            Integer year) {

        Object[] row = (Object[]) em.createNativeQuery(

                        QueryEnum.GET_PAYROLL_SUMMARY.getValue())

                .setParameter(1, month)

                .setParameter(2, year)

                .getSingleResult();

        return new PayrollSummaryJsonb(

                (BigDecimal) row[0],

                (BigDecimal) row[1],

                (BigDecimal) row[2]

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
        ),

        DEDUCTION_REPORT(
                "SELECT " +
                        "ed.employee_name AS name, " +

                        "SUM(CASE WHEN sc.component_code = 'PF' THEN esd.amount ELSE 0 END) AS pf, " +
                        "SUM(CASE WHEN sc.component_code = 'ESI' THEN esd.amount ELSE 0 END) AS esi, " +
                        "SUM(CASE WHEN sc.component_code = 'PT' THEN esd.amount ELSE 0 END) AS pt, " +
                        "SUM(CASE WHEN sc.component_code = 'LOP' THEN esd.amount ELSE 0 END) AS lop, " +

                        "CAST(0 AS DECIMAL(10,2)) AS advance, " +

                        "SUM(CASE WHEN sc.component_code NOT IN ('PF','ESI','PT','LOP') THEN esd.amount ELSE 0 END) AS other, " +

                        "SUM(esd.amount) AS total_deduction " +

                        "FROM employee_salary_deductions esd " +

                        "JOIN salary_component sc ON sc.id = esd.component_id " +
                        "JOIN employee_salary_structure ess ON ess.id = esd.salary_structure_id " +
                        "JOIN employee_details ed ON ed.id = ess.employee_id " +

                        "JOIN payroll p ON p.employee_id = ess.employee_id " +

                        "WHERE ess.salary_status='Active' " +
                        "AND p.month = ?1 " +
                        "AND p.year = ?2 " +

                        "GROUP BY ed.employee_name"
        ),

        FETCH_PAYROLL_MONTH_STATUS(
                "SELECT " +
                        "    DATE_FORMAT(MIN(a.date), '%b %Y') AS month, " +

                        "    CASE " +
                        "        WHEN COUNT(*) = SUM( " +
                        "            CASE " +
                        "                WHEN UPPER(a.approval_status) = 'APPROVED' " +
                        "                THEN 1 " +
                        "                ELSE 0 " +
                        "            END " +
                        "        ) " +
                        "        THEN 'Approved' " +
                        "        ELSE 'Pending' " +
                        "    END AS attendance_status, " +

                        "    COALESCE( " +
                        "        GROUP_CONCAT( " +
                        "            DISTINCT p.status " +
                        "            ORDER BY p.status " +
                        "            SEPARATOR ', ' " +
                        "        ), " +
                        "        'Not Started' " +
                        "    ) AS payroll_status " +

                        "FROM employee_attendance a " +

                        "LEFT JOIN payroll p " +
                        "    ON p.employee_id = a.employee_id " +
                        "    AND p.month = MONTH(a.date) " +
                        "    AND p.year = YEAR(a.date) " +

                        "GROUP BY " +
                        "    YEAR(a.date), " +
                        "    MONTH(a.date) " +

                        "ORDER BY " +
                        "    YEAR(a.date) DESC, " +
                        "    MONTH(a.date) DESC"
        ),
        GET_MONTHLY_PAYROLL(

                "SELECT " +
                        "ed.id AS employeeId, " +
                        "ed.employee_name AS employeeName, " +

                        "COALESCE(SUM(CASE " +
                        "WHEN ss.component_name='Basic' THEN ss.amount " +
                        "ELSE 0 END),0) AS basic, " +

                        "COALESCE(( " +
                        "SELECT SUM(overtime) " +
                        "FROM employee_attendance ea " +
                        "WHERE ea.employee_id=ed.id " +
                        "AND MONTH(ea.date)=?1 " +
                        "AND YEAR(ea.date)=?2 " +
                        "),0) AS ot, " +

                        "COALESCE(SUM(CASE " +
                        "WHEN ss.component_name='Bonus' THEN ss.amount " +
                        "ELSE 0 END),0) AS bonus, " +

                        "0 AS lop, " +

                        "p.gross_salary AS gross, " +

                        "COALESCE(SUM(CASE " +
                        "WHEN ss.type='deduction' THEN ss.amount " +
                        "ELSE 0 END),0) AS deductions, " +

                        "p.net_salary AS net, " +

                        "p.status " +

                        "FROM payroll p " +

                        "INNER JOIN employee_details ed " +
                        "ON p.employee_id=ed.id " +

                        "LEFT JOIN salary_structure ss " +
                        "ON ss.employee_id=ed.id " +

                        "WHERE p.month=?1 " +
                        "AND p.year=?2 " +

                        "GROUP BY " +
                        "p.payroll_id, " +
                        "ed.id, " +
                        "ed.employee_name, " +
                        "p.gross_salary, " +
                        "p.net_salary, " +
                        "p.status " +

                        "ORDER BY ed.employee_name"

        ),
        GET_PAYROLL_SUMMARY(

                "SELECT " +

                        "COALESCE(SUM(gross_salary),0), " +

                        "COALESCE(SUM(gross_salary-net_salary),0), " +

                        "COALESCE(SUM(net_salary),0) " +

                        "FROM payroll " +

                        "WHERE month=?1 " +

                        "AND year=?2"

        );

        private final String value;
        }

    }


