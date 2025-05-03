package com.sbd.common.repository;

import com.sbd.common.Jsonb.SkillCountDTO;
import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.request.Pagination;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeDetailsRepository implements PanacheRepository<EmployeeDetails> {

    @Inject
    EntityManager em;
    public EmployeeDetails findByEmail(String email) {
        return find("email", email).firstResult();
    }
    public EmployeeDetails findByEmailOrPhone(String username) {
        return find("email = ?1 OR phoneNumber = ?1", username).firstResult();
    }

    public EmployeeDetails findById(Integer employeeId) {
        return find("id", employeeId).firstResult();
    }

    public EmployeeDetails findByAadharNumber(String aadharNumber) {
        return find("aadharNumber", aadharNumber).firstResult();
    }

public List<EmployeeDetails> listAll(Pagination pagination) {
    return find(QueryEnum.QUERY_LIST_ALL.getValue() )
            .page(pagination.getPageIndex() - 1, pagination.getPageSize())
            .list();
}

    public List<EmployeeDetails> listByName(String name, Pagination pagination) {
        return find(QueryEnum.QUERY_LIST_BY_NAME.getValue() + " ORDER BY e.id DESC",
                         Parameters.with(QueryEnum.EMPLOYEE_NAME.getValue(), "%" + name + "%"))
                .page(pagination.getPageIndex() - 1, pagination.getPageSize())
                .list();
    }

    public List<SkillCountDTO> getSkillWiseEmployeeCount() {
        List<Object[]> results = em.createNativeQuery(QueryEnum.QUERY_SKILL_COUNT.getValue()).getResultList();
        return results.stream()
                .map(obj -> new SkillCountDTO((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }
    // Delete employee by ID
    public boolean deleteById(Long employeeId) {
        return delete("id", employeeId) > 0;
    }

    @Getter
    @AllArgsConstructor
    private enum QueryEnum {
        QUERY_LIST_ALL("SELECT e FROM EmployeeDetails e order by e.id desc"),
        QUERY_LIST_BY_NAME("SELECT e FROM EmployeeDetails e WHERE e.employeeName LIKE :employeeName"),
        QUERY_SKILL_COUNT("SELECT s.skill_name AS skillName, COUNT(es.employee_id) AS employeeCount " +
                "FROM skills s " +
                "JOIN employee_skills es ON s.id = es.skill_id " +
                "GROUP BY s.id, s.skill_name"),
        EMPLOYEE_ID("employeeId"),
        EMPLOYEE_NAME("employeeName");

        private final String value;
    }

    }
