package com.sbd.common.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "leave_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "annual_entitlement", nullable = false)
    private Integer annual_entitlement;

    @Column(name = "is_carry_forward_allowed")
    private Boolean isCarryForwardAllowed;

    @OneToMany(mappedBy = "leaveType")
    @JsonbTransient
    private Set<Leave> leaves;

    @OneToMany(mappedBy = "leaveType")
    @JsonbTransient
    private Set<LeaveBalance> leaveBalances;
}
