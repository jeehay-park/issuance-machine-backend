package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "program_info")
@Entity
public class ProgramInfo {
    @InjectSequenceValue(sequencename = "seq", tablename = "program_info")
    @Column(name = "seq", unique = true, nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for seq
    public long seq;

    @Column(name = "prog_id", unique = true, nullable = false, updatable = false)
    @Id // primary key
    private String progId;

    @Column(name = "prog_name")
    private String progName;

    @Column(name = "product")
    private String product;

    @Column(name = "test_code")
    private String testCode;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Pattern(regexp = "ACTIVE|NOTUSE|DELETED", message = "Status must be one of the following: ACTIVE, NOTUSE, DELETED")
    private String status;

    // Optionally add a setter with validation
    //    public void setStatus(String status) {
    //        if (status == null || (!status.equals("ACTIVE") && !status.equals("NOTUSE") && !status.equals("DELETED"))) {
    //            throw new IllegalArgumentException("Invalid status value. Allowed values are: ACTIVE, NOTUSE, DELETED.");
    //        }
    //
    //        this.status = status;
    //
    //    }

    @Column(name = "param")
    private String param;

    @Column(name = "param_ext")
    private String paramExt;

    @Column(name = "is_encrypted_sn")
    private String isEncryptedSn;

    @Column(name = "prof_id", nullable = false)
    private String profId;

    @Column(name = "keyis_id")
    private String keyisId;

    @Column(name = "scrt_id")
    private String scrtId;

    @Column(name = "session_handler")
    private String sessionHandler;

    @Column(name = "etc_option")
    private String etcOption;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "interface_type")
    private String interfaceType;

    @Column(name = "package_type")
    private String packageType;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

//    public String toString() {
//        return "ProgramInfo{" +
//                "seq=" + seq +
//                ", progId=" + progId +
//                ", progName=" + progName +
//                ", product=" + product +
//                ", testCode=" + testCode +
//                ", description=" + description +
//                ", status=" + status +
//                ", param=" + param +
//                ", paramExt=" + paramExt +
//                ", isEncryptedSn=" + isEncryptedSn +
//                ", profId=" + profId +
//                ", keyisId=" + keyisId +
//                ", scrtId=" + scrtId +
//                ", sessionHandler=" + sessionHandler +
//                ", etcOption=" + etcOption +
//                ", companyCode=" + companyCode +
//                ", countryCode=" + countryCode +
//                ", interfaceType=" + interfaceType +
//                ", packageType=" + packageType +
//                ", updatedAt=" + updatedAt +
//                ", createdAt=" + createdAt +
//                ", comment=" + comment +
//                "}";
//    }
}