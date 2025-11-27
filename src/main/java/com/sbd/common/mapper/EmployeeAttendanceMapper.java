package com.sbd.common.mapper;

import com.sbd.common.Jsonb.EmployeeAttendanceResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
@ApplicationScoped
public interface EmployeeAttendanceMapper {
    EmployeeAttendanceMapper INSTANCE = Mappers.getMapper(EmployeeAttendanceMapper.class);

    default EmployeeAttendanceResponseDTO toDto(Map<String, Object> data) {
        return new EmployeeAttendanceResponseDTO(
                ((Number) data.get("employeeId")).longValue(),
                ((Number) data.get("presentDays")).intValue(),
                ((Number) data.get("pendingDays")).intValue(),
                ((Number) data.get("totalRecords")).intValue(),
                ((Number) data.get("totalDaysInMonth")).intValue(),
                ((Number) data.get("totalWorkingDays")).intValue()
        );
    }

}
