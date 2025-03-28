package com.sbd.common.mapper;

import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.Leave;
import com.sbd.common.request.EmployeeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper
public interface EmployeeDetailsMapper {
    EmployeeDetailsMapper INSTANCE = Mappers.getMapper(EmployeeDetailsMapper.class);


    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "aadharNumber" , source = "aadhaarNumber")
    @Mapping(target = "pancard" , source = "panCard")
    @Mapping(target = "status" , source = "status")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profilePicUrl", source = "profilePic", qualifiedByName = "mapToBase64")
    @Mapping(target = "aadhaarPicUrl", source = "aadhaarPic", qualifiedByName = "mapToBase64")
    @Mapping(target = "pancardPicUrl", source = "pancardPic", qualifiedByName = "mapToBase64")
    EmployeeDTO.EmployeeDetailsDTO toDTO(EmployeeDetails employeeDetails);

    //  New Method: Convert List of Leave to List of LeaveDTO
    default List<EmployeeDTO.EmployeeDetailsDTO> toDTOList(List<EmployeeDetails> employeeDetails) {
        return employeeDetails.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Named("mapToBase64")
    default String mapToBase64(byte[] data) {
        return (data != null) ? Base64.getEncoder().encodeToString(data) : null;
    }

}
