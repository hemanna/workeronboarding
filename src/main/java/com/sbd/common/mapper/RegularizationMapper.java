package com.sbd.common.mapper;

import com.sbd.common.Jsonb.EmployeeAttendanceRegularizationJsonb;
import com.sbd.common.Jsonb.EmployeeAttendanceRegularizationResponseDto;
import com.sbd.common.entity.EmployeeAttendanceRegularization;
import jakarta.enterprise.context.ApplicationScoped;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@ApplicationScoped
@Mapper
public interface RegularizationMapper {
    EmployeeAttendanceRegularizationResponseDto toResponseDto(EmployeeAttendanceRegularization reg);

    List<EmployeeAttendanceRegularizationResponseDto> toResponseDtoList(List<EmployeeAttendanceRegularization> regs);

}
