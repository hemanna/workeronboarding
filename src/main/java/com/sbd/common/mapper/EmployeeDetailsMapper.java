package com.sbd.common.mapper;

import com.sbd.common.entity.EmployeeDetails;
import com.sbd.common.entity.Leave;
import com.sbd.common.request.EmployeeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Mapper
public interface EmployeeDetailsMapper {
    EmployeeDetailsMapper INSTANCE = Mappers.getMapper(EmployeeDetailsMapper.class);

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "profilePicUrl", source = "profilePic", qualifiedByName = "mapProfilePic")
    @Mapping(target = "aadhaarPicUrl", source = "aadhaarPic", qualifiedByName = "mapAadhaarPic")
    @Mapping(target = "pancardPicUrl", source = "pancardPic", qualifiedByName = "mapPancardPic")
    EmployeeDTO.EmployeeDetailsDTO toDTO(EmployeeDetails employeeDetails);

    //  New Method: Convert List of Leave to List of LeaveDTO
    default List<EmployeeDTO.EmployeeDetailsDTO> toDTOList(List<EmployeeDetails> employeeDetails) {
        return employeeDetails.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Named("mapProfilePic")
    default String mapProfilePic(byte[] profilePic) {
        return (profilePic != null) ? "http://localhost:8080/employeedetails/profilePic/" + profilePic.hashCode() : null;
    }

    @Named("mapAadhaarPic")
    default String mapAadhaarPic(byte[] aadhaarPic) {
        return (aadhaarPic != null) ? "http://localhost:8080/employeedetails/aadhaarPic/" + aadhaarPic.hashCode() : null;
    }

    @Named("mapPancardPic")
    default String mapPancardPic(byte[] pancardPic) {
        return (pancardPic != null) ? "http://localhost:8080/employeedetails/pancardPic/" + pancardPic.hashCode() : null;
    }
}
