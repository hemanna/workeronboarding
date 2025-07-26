package com.sbd.common.mapper;

import com.sbd.common.Jsonb.EmployeeSalaryStructureJsonb;
import com.sbd.common.entity.EmployeeSalaryStructure;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
@Mapper
public interface EmployeeSalaryStructureMapper {
    EmployeeSalaryStructureMapper INSTANCE = Mappers.getMapper(EmployeeSalaryStructureMapper.class);

    @Mapping(target = "salaryStructureId", source = "id")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "departmentId", source = "department.id")
    EmployeeSalaryStructureJsonb toJsonb(EmployeeSalaryStructure entity);

}
