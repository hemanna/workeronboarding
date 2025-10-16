package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "asset_tag", unique = true, nullable = false)
    private String assetTag;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_cost")
    private BigDecimal purchaseCost;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "warranty_expiry")
    private LocalDate warrantyExpiry;

    @Column(name = "status")
    private String status;

    @Column(name = "asset_image", columnDefinition = "LONGBLOB")
    private String assetImage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssetAllocation> allocations;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AssetMaintenance> maintenances;

}
