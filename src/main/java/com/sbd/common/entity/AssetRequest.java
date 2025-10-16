package com.sbd.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeDetails employee;

    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private EmployeeDetails approver;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "request_attachment_image")
    private String requestAttachmentImage;

    @Column(name = "approval_document_image")
    private String approvalDocumentImage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
