package com.sbd.common.repository;

import com.sbd.common.Jsonb.LeaveDTO;
import com.sbd.common.entity.Leave;
import com.sbd.common.mapper.LeaveMapper;
import com.sbd.common.request.EmployeeDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class LeaveRepository implements PanacheRepository<Leave> {

    public Leave findById(Integer id) {
        return find("id", id).firstResult();
    }

    public List<LeaveDTO> findAllWithLeaveDays() {
        List<Leave> leaves = getEntityManager().createQuery(
                        "SELECT l FROM Leave l", Leave.class)
                .getResultList();

        return leaves.stream()
                .map(LeaveMapper.INSTANCE::toDTO)
                .peek(l -> l.setNumberOfDays(ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 0))
                .collect(Collectors.toList());
    }



}
