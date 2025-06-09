package com.sbd.common.mapper;

import com.sbd.common.Jsonb.SalaryStructureDTO;
import com.sbd.common.entity.PayrollComponent;
import com.sbd.common.entity.SalaryStructure;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
@Mapper
public interface SalaryStructureMapper {
    SalaryStructureMapper INSTANCE = Mappers.getMapper(SalaryStructureMapper.class);
    SalaryStructureDTO toDTO(SalaryStructure entity);

    SalaryStructureDTO toDTO(PayrollComponent entity);
}
