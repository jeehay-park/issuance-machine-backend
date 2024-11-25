package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "program_info")
@Entity
public class ProgramInfo {
    @InjectSequenceValue(sequencename = "seq", tablename = "program_info")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    public long seq;

    @Id // primary key
    @Column(name = "prog_id", unique = true, nullable = false)
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

    @Column(name = "param")
    private String param;

    @Column(name = "param_ext")
    private String paramExt;

    @NonNull
    @Column(name = "is_encrypted_sn")
    private boolean isEncryptedSn;

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

    @Schema(description = "is_encrypted_sn")
    private boolean isEncryptSn;

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

    @Getter
    @OneToMany(mappedBy = "programInfoProfileId")
    private List<ProfileConfig> profileConfig = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "programInfoKeyissueId") // Match the field name in KeyissueConfig
    private List<KeyissueConfig> keyIssueInfo = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "programInfoScriptId")
    private List<ScriptConfig> scriptInfo = new ArrayList<>();

    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(progId) || AppConstants.TEMPORARY_ID.equals(progId))
            progId = "prgo_" + String.format("%04d", seq);

        updatedAt = LocalDateTime.now();
    }


}