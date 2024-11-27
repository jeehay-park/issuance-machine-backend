package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "script_config")
@Entity
public class ScriptConfig {

    @InjectSequenceValue(sequencename = "seq", tablename = "script_config")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "scrt_id", unique = true, nullable = false)
    @Setter
    public String scrtId = AppConstants.TEMPORARY_ID;

    @Column(name = "scrt_name", nullable = false)
    @Setter
    private String scrtName;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "scrt_type")
    @Setter
    private String scrtType;

    @Column(name = "version")
    @Setter
    private String version;

    @Column(name = "ctnt_data", nullable = false)
    @Setter
    private String ctntData;

    @Column(name = "data_hash", nullable = false)
    @Setter
    private String dataHash;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Setter
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    //Define the relationship to ProgramInfoSearchRSB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrt_id", referencedColumnName = "scrt_id", insertable = false, updatable = false)
    private ProgramInfo programInfoScriptId;

    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(scrtId) || AppConstants.TEMPORARY_ID.equals(scrtId))
            scrtId = "scrt_" + String.format("%06d", seq);
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ScriptConfig{" +
                "seq=" + seq +
                ", scrtId='" + scrtId + '\'' +
                ", scrtName='" + scrtName + '\'' +
                ", description='" + description + '\'' +
                ", scrtType='" + scrtType + '\'' +
                ", version='" + version + '\'' +
                ", ctntData='" + ctntData + '\'' +
                ", dataHash='" + dataHash + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';
    }

}
