package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "session_info")
@Entity
public class DeviceSessionInfo {
    @InjectSequenceValue(sequencename = "seq", tablename = "session_info")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    public long seq;

    @Id
    @Column(name = "sess_id", unique = true, nullable = false)
    private String sessId;

    @Column(name = "work_id", nullable = false)
    private String workId;

    @Column(name = "workdet_id")
    private String workdetId;

    @Column(name = "dvc_id", nullable = false)
    private String dvcId;

    @CreatedDate
    @Column(name = "session_date", nullable = false, updatable = false)
    private LocalDateTime sessionDate;

    @Column(name = "session_no")
    private String sessionNo;

    @Column(name = "chip_sn")
    private String chipSn;

    @Column(name = "status")
    @Pattern(regexp = "READY|BURNING|COMPLETED", message = "Status must be one of the following: READY, BURNING, COMPLETED")
    private String status;

    @Column(name = "result")
    @Pattern(regexp = "NONE|OK|FAIL|CHECK|OVER", message = "Result must be one of the following: NONE, OK, FAIL, CHECK, OVER")
    private String result;

    @Column(name = "error")
    private String error;

    @Column(name = "tk_msec_time")
    private int tkMsecTime;

    @Column(name = "param")
    private String param;

    @Column(name = "param_ext")
    private String paramExt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "comment")
    private String comment;

    // Relationship with WorkInfo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", referencedColumnName = "work_id", insertable = false, updatable = false)
    private WorkInfo workInfo;

    @PrePersist
    public void onSave() {
        if (!CommonUtils.hasValue(sessId) || AppConstants.TEMPORARY_ID.equals(sessId))
            sessId = "sess_" + String.format("%04d", seq);
        updatedAt = LocalDateTime.now();
    }


}
