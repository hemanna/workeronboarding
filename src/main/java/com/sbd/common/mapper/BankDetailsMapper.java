package com.sbd.common.mapper;

import com.sbd.common.Jsonb.BankDetailsJsonb;
import com.sbd.common.entity.BankDetails;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
@Mapper
public interface BankDetailsMapper {

    BankDetailsMapper INSTANCE = Mappers.getMapper(BankDetailsMapper.class);

    @Mapping(target = "employeeId", source = "employeeId.id")
    BankDetailsJsonb toJsonb(BankDetails bankDetails);

    @Mapping(target = "employeeId.id", source = "employeeId")
    BankDetails toEntity(BankDetailsJsonb jsonb);
}


