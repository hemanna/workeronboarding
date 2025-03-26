package com.sbd.common.mapper;

import com.sbd.common.Jsonb.LeaveDTO;
import com.sbd.common.entity.Leave;
import com.sbd.common.request.EmployeeDTO;
import jakarta.enterprise.context.Dependent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "leaveTypeId", source = "leaveType.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "attachment", source = "attachment", qualifiedByName = "mapToBase64")
    LeaveDTO toDTO(Leave leave);

    @Mapping(target = "attachment", ignore = true)
    Leave toEntity(LeaveDTO leaveDTO);

    //  New Method: Convert List of Leave to List of LeaveDTO
    default List<LeaveDTO> toDTOList(List<Leave> leaves) {
        return leaves.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Named("mapToBase64")
    default String mapToBase64(byte[] data) {
        return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
    }

}
