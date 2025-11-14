package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;
}
