package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sn_rule")
@Entity
public class SNRule {

    @InjectSequenceValue(sequencename = "seq", tablename = "sn_rule")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "snr_id", unique = true, nullable = false)
    @Setter
    public String snrId = AppConstants.TEMPORARY_ID;

    @Column(name = "snr_name")
    @Setter
    private String snrName;

    @Column(name = "test_code")
    @Setter
    private String testCode;

    @Column(name = "location")
    @Setter
    private int location;

    @Column(name = "last_burn_date")
    @Setter
    private LocalDateTime lastBurnDate;

    @Column(name = "today_count")
    @Setter
    private long todayCount;

    @Column(name = "sum_count")
    @Setter
    private long countSum;

    @Column(name = "updated_at")
    @Setter
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;


    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(snrId) || AppConstants.TEMPORARY_ID.equals(snrId))
            snrId = "snr" + String.format("%06d", seq);
    }

    @Override
    public String toString() {
        return "SNRule{" +
                "seq=" + seq +
                ", snrId='" + snrId + '\'' +
                ", snrName='" + snrName + '\'' +
                ", testCode='" + testCode + '\'' +
                ", location=" + location +
                ", lastBurnDate=" + lastBurnDate +
                ", todayCount=" + todayCount +
                ", countSum=" + countSum +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';
    }

}
