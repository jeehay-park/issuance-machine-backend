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
@Table(name = "keyissue_config")
@Entity
public class KeyissueConfig {

    @InjectSequenceValue(sequencename = "seq", tablename = "keyissue_config")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "keyis_id", unique = true, nullable = false)
    @Setter
    public String keyisId = AppConstants.TEMPORARY_ID;

    @Column(name = "keyis_name", nullable = false)
    @Setter
    private String keyisName;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "keyis_type")
    @Setter
    private String keyisType;

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

    // Define the relationship to ProgramInfoSearchRSB
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyis_id", insertable=false, updatable=false) // This links to ProgramInfo.progId
    private ProgramInfo programInfoKeyissueId;

    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(keyisId) || AppConstants.TEMPORARY_ID.equals(keyisId))
            keyisId = "kis" + String.format("%06d", seq);
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "KeyissueConfig{" +
                "seq=" + seq +
                ", keyisId='" + keyisId + '\'' +
                ", keyisName='" + keyisName + '\'' +
                ", description='" + description + '\'' +
                ", keyisType='" + keyisType + '\'' +
                ", version='" + version + '\'' +
                ", ctntData='" + ctntData + '\'' +
                ", dataHash='" + dataHash + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';
    }

}
