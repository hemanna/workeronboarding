package com.sbd.common.Jsonb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsJsonb {
    private Integer bankId;
    private Integer employeeId;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String branchName;
    private String nameOnAccount;
    private String accountCountryTerritory;
    private String accountType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
