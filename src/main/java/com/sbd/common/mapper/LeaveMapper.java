package com.sbd.common.mapper;

import com.sbd.common.entity.Leave;
import com.sbd.common.request.EmployeeDTO;
import jakarta.enterprise.context.Dependent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "leaveTypeId", source = "leaveType.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "attachmentUrl", source = "attachment", qualifiedByName = "mapAttachment") // Custom mapping
    EmployeeDTO.LeaveDTO toDTO(Leave leave);

    @Mapping(target = "attachment", ignore = true) // Ignore mapping back to byte[]
    Leave toEntity(EmployeeDTO.LeaveDTO leaveDTO);

    //  New Method: Convert List of Leave to List of LeaveDTO
    default List<EmployeeDTO.LeaveDTO> toDTOList(List<Leave> leaves) {
        return leaves.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Custom Method to Convert byte[] to URL
    @Named("mapAttachment")
    default String mapAttachment(byte[] attachment) {
        return (attachment != null) ? "http://localhost:8080/leave/attachment/" + attachment.hashCode() : null;
    }
}
