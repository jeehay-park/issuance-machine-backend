package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.annotations.ValidateString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
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
    private String work_id;

    @Column(name = "work_no")
    private String work_no;

    @Column(name = "tag_name")
    private String tag_name;

    @Column(name = "customer")
    private String customer;

    @Column(name = "device_name")
    private String device_name;

    @Column(name = "order_no")
    private String order_no;

    @NotNull
    @Column(name = "prog_id")
    private String prog_id;

    @NotNull
    @Column(name = "mcn_id")
    private String mcn_id;

    @Column(name = "snr_id")
    private String snr_id;

    @Column(name = "target_size")
    private long target_size;

    @Column(name = "completed_size")
    private long completed_size;

    @Column(name = "failed_size")
    private long failed_size;

    @Column(name = "check_size")
    private long check_size;

    @Column(name = "due_date")
    private LocalDateTime due_date;

    @Column(name = "is_lock")
    private String is_lock;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

}
