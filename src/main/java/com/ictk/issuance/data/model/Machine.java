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
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "machine")
@Entity
public class Machine {

    @InjectSequenceValue(sequencename = "seq", tablename = "machine")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id
    @Column(name = "mcn_id", unique = true, nullable = false)
    // @Setter
    public String mcnId = AppConstants.TEMPORARY_ID;

    @Column(name = "mcn_name")
    @Setter
    private String mcnName;

    @Column(name = "etc")
    @Setter
    private String etc;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Setter
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "comment")
    private String comment;

    // 가능하면 양방향 연관관계는 안쓰는게 좋음.
    // 여긴 machine과 machine_device는 거의 한몸처럼 움직이므로 사용해도 무방.
    @OneToMany(mappedBy = "machine")
    private List<Device> devices = new ArrayList<>();


    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(mcnId) || AppConstants.TEMPORARY_ID.equals(mcnId))
            mcnId = "mcn" + String.format("%04d", seq);

        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Machine{" +
                "seq=" + seq +
                ", mcnId='" + mcnId + '\'' +
                ", mcnName='" + mcnName + '\'' +
                ", etc='" + etc + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", comment='" + comment + '\'' +
                // ", devices=" + devices +
                '}';
    }

}
