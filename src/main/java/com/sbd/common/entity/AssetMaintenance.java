package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_maintenance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssetMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Integer maintenanceId;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "before_service_image")
    private String beforeServiceImage;

    @Column(name = "after_service_image")
    private String afterServiceImage;

    @Column(name = "service_invoice_image")
    private String serviceInvoiceImage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
