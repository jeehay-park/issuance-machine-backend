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
@Table(name = "code_info")
@Entity
public class CodeInfo {

    @InjectSequenceValue(sequencename = "seq", tablename = "code_info")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "code_id", unique = true, nullable = false)
    @Setter
    public String codeId = AppConstants.TEMPORARY_ID;

    @Column(name = "code_name", nullable = false)
    @Setter
    private String codeName;

    @Column(name = "code_group")
    @Setter
    private String codeGroup;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "status")
    @Setter
    private String status;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Setter
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    @PrePersist
    public void onSave() {
        if(!CommonUtils.hasValue(codeId) || AppConstants.TEMPORARY_ID.equals(codeId))
            codeId = "code" + String.format("%06d", seq);
        updatedAt = LocalDateTime.now();

    }

@Override
    public String toString() {

        return "CodeInfo{" +
                "seq=" + seq +
                ", codeId='" + codeId + '\'' +
                ", codeName='" + codeName + '\'' +
                ", codeGroup='" + codeGroup + '\'' +
                ", description=" + description +
                ", status=" + status +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';

    }

}
