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
@Table(name = "profile_config")
@Entity
public class ProfileConfig {

    @InjectSequenceValue(sequencename = "seq", tablename = "profile_config")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "prof_id", unique = true, nullable = false)
    @Setter
    public String profId = AppConstants.TEMPORARY_ID;

    @Column(name = "prof_name", nullable = false)
    @Setter
    private String profName;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "prof_type")
    @Setter
    private String profType;

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
    @JoinColumn(name = "prof_id", insertable=false, updatable=false) // This links to ProgramInfo.progId
    private ProgramInfo programInfoProfileId;

    @PrePersist
    public void onSave(){

        System.out.println("profId :" + profId);
        if(!CommonUtils.hasValue(profId) || AppConstants.TEMPORARY_ID.equals(profId))
            profId = "prof" + String.format("%06d", seq);

        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ProfileConfig{" +
                "seq=" + seq +
                ", profId='" + profId + '\'' +
                ", profName='" + profName + '\'' +
                ", description='" + description + '\'' +
                ", profType='" + profType + '\'' +
                ", version='" + version + '\'' +
                ", ctntData='" + ctntData + '\'' +
                ", dataHash='" + dataHash + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';
    }
}
