package com.sbd.common.mapper;

import com.sbd.common.Jsonb.DeductionReportDTO;
import com.sbd.common.Jsonb.SalaryDashboardDTO;
import com.sbd.common.Jsonb.SalaryStructureDTO;
import com.sbd.common.entity.PayrollComponent;
import com.sbd.common.entity.SalaryStructure;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Map;

@ApplicationScoped
@Mapper
public interface SalaryStructureMapper {
    SalaryStructureMapper INSTANCE = Mappers.getMapper(SalaryStructureMapper.class);
    SalaryStructureDTO toDTO(SalaryStructure entity);

    SalaryStructureDTO toDTO(PayrollComponent entity);
    default SalaryDashboardDTO toDTO(Map<String, Object> data) {
        return SalaryDashboardDTO.builder()
                .totalGrossPay((BigDecimal) data.get("totalGrossPay"))
                .totalDeduction((BigDecimal) data.get("totalDeduction"))
                .totalNetPay((BigDecimal) data.get("totalNetPay"))
                .employeeCount(((Number) data.get("employeeCount")).longValue())
                .build();
    }

    default DeductionReportDTO toDeductionDTO(Object[] r) {

        return DeductionReportDTO.builder()
                .name((String) r[0])
                .pf((BigDecimal) r[1])
                .esi((BigDecimal) r[2])
                .pt((BigDecimal) r[3])
                .lop((BigDecimal) r[4])
                .advance((BigDecimal) r[5])
                .other((BigDecimal) r[6])
                .totalDeduction((BigDecimal) r[7])
                .build();
    }

}
