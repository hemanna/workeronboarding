package com.sbd.common.mapper;

import com.sbd.common.Jsonb.EmployeeAttendanceDTO;
import com.sbd.common.Jsonb.EmployeeAttendanceResponseDTO;
import com.sbd.common.entity.EmployeeAttendance;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
@ApplicationScoped
public interface EmployeeAttendanceMapper {
    EmployeeAttendanceMapper INSTANCE = Mappers.getMapper(EmployeeAttendanceMapper.class);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "departmentId", source = "employee.department.id")
    @Mapping(target = "roleId", source = "employee.role.id")
    @Mapping(target = "leaveId", source = "leave.id")
    EmployeeAttendanceDTO toDTO(EmployeeAttendance entity);

    List<EmployeeAttendanceDTO> toDTOList(List<EmployeeAttendance> entities);
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
