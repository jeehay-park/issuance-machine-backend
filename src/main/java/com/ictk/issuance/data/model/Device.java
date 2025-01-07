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
@Table(name = "machine_device")
@Entity
public class Device {

    @InjectSequenceValue(sequencename = "seq", tablename = "machine_device")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id // primary key
    @Column(name = "dvc_id", unique = true, nullable = false)
    @Setter
    public String dvcId = AppConstants.TEMPORARY_ID;

    @Column(name = "dvc_name")
    @Setter
    private String dvcName;

    @Column(name = "dvc_num", nullable = false)
    @Setter
    private int dvcNum;

    // @ManyToOne and @JoinColumn : the most common way to define a foreign key in a JPA entity
    @ManyToOne
    @JoinColumn(name = "mcn_id", referencedColumnName = "mcn_id")
    private Machine machine;

    @Column(name = "ip")
    @Setter
    private String ip;

    @Column(name = "rom_ver")
    @Setter
    private String romVer;

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
    public void onSave(){
        if(!CommonUtils.hasValue(dvcId) || AppConstants.TEMPORARY_ID.equals(dvcId))
            dvcId = "dvc_" + String.format("%04d", seq);

        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Device{" +
                "seq=" + seq +
                ", dvcId='" + dvcId + '\'' +
                ", dvcName='" + dvcName + '\'' +
                ", dvcNum=" + dvcNum +
                ", machine=" + (machine!=null?machine.getMcnId():"") +
                ", ip='" + ip + '\'' +
                ", romVer='" + romVer + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                '}';
    }
}
