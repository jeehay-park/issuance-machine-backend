package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.annotations.ValidateString;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "work_info")
@Entity
public class WorkInfo {

    @InjectSequenceValue(sequencename = "seq", tablename = "work_info")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    public long seq;

    @Id
    @Column(name = "work_id", unique = true, nullable = false)
    private String workId;

    @Column(name = "work_no", unique = true, nullable = false)
    private String workNo;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "customer")
    private String customer;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "order_no")
    private String orderNo;

    @NotNull
    @Column(name = "prog_id")
    private String progId;

    @NotNull
    @Column(name = "mcn_id")
    private String mcnId;

    @Column(name = "snr_id")
    private String snrId;

    @Column(name = "target_size")
    private long targetSize;

    @Column(name = "completed_size")
    private long completedSize;

    @Column(name = "failed_size")
    private long failedSize;

    @Column(name = "check_size")
    private long checkSize;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "is_lock")
    private boolean isLock;

    @Column(name = "status")
    @ValidateString(acceptedValues = {"INIT", "READY", "RUNNING", "ON_STOP", "FINISHED"}, message = "status가 유효하지 않습니다.")
    private String status;

    @Column(name = "parameter")
    private String param;

    @Column(name = "param_ext")
    private String paramExt;

    @Column(name = "detail_msg")
    private String detailMsg;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name ="prog_id", insertable = false, updatable = false)
    private ProgramInfo programInfo;

    @ManyToOne
    @JoinColumn(name ="mcn_id", insertable = false, updatable = false)
    private Machine machineInfo;

    @ManyToOne
    @JoinColumn(name ="snr_id", insertable = false, updatable = false)
    private SNRule snRuleInfo;

    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(workId) || AppConstants.TEMPORARY_ID.equals(workId))
            workId = "wrk_" + String.format("%04d", seq);

        updatedAt = LocalDateTime.now();
    }

}
