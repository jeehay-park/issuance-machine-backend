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
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_handler")
@Entity
public class WorkHandler {

    @InjectSequenceValue(sequencename = "seq", tablename = "work_handler")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    public long seq;

    @Id
    @Column(name = "hdl_id", unique = true, nullable = false)
    private String hdlId;

    @Column(name = "work_id", nullable = false)
    private String workId;

    @Column(name = "device_id", nullable = false)
    private String dvcId;

    @Column(name = "mcn_id", nullable = false)
    private String mcnId;

    @Column(name = "hdl_name", nullable = false)
    private String hdlName;

    @Column(name = "status")
    private String status;

    @Column(name = "detail_msg")
    private String detailMsg;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    // Relationship with Device
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dvc_id", referencedColumnName = "dvc_id", insertable = false, updatable = false)
    private Device device;

    @PrePersist
    public void onSave() {
        if (!CommonUtils.hasValue(hdlId) || AppConstants.TEMPORARY_ID.equals(hdlId))
            hdlId = "hdl_" + String.format("%04d", seq);
        updatedAt = LocalDateTime.now();
    }
}
