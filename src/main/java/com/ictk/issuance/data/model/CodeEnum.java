package com.ictk.issuance.data.model;

import com.ictk.issuance.common.annotations.InjectSequenceValue;
import com.ictk.issuance.common.utils.CommonUtils;
import com.ictk.issuance.constants.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "code_enum")
@Entity
public class CodeEnum {
    @InjectSequenceValue(sequencename = "seq", tablename = "code_enum")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Column(name = "enum_seq")
    @Setter
    private long enumSeq;

    @Id // primary key
    @Column(name = "enum_id", unique = true, nullable = false)
    @Setter
    private String enumId;

    // Define code_id as a column in CodeEnum and allow inserts/updates
    @Column(name = "code_id", nullable = false, insertable = true, updatable = true )
    @Setter
    private String codeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", referencedColumnName = "code_id", insertable = false, updatable = false)
    private CodeInfo codeInfo;

    @Column(name = "enum_value", nullable = false)
    @Setter
    private String enumValue;

    @Column(name = "is_mandatory", nullable = false)
    @Setter
    private Boolean isMandatory;

    @Column(name = "ip", nullable = false)
    @Setter
    private String ip;

    @Column(name = "`order`", nullable = false)
    @Setter
    private long order;

    @Column(name = "description")
    @Setter
    private String description;

    @PrePersist
    public void onSave(){
        if(!CommonUtils.hasValue(enumId) || AppConstants.TEMPORARY_ID.equals(enumId))
            enumId = "code_id_" + String.format("%04d", seq);
    }

    @Override
    public String toString() {
        return "Enum{" +
                "seq=" + seq +
                "enumSeq=" + enumSeq + '\'' +
                ", enumId='" + enumId +
                ", codeInfo='" + (codeInfo!=null?codeInfo.getCodeId():"") + '\'' +
                ", enumValue=" + enumValue + '\'' +
                ", isMandatory=" + isMandatory +
                ", ip='" + ip + '\'' +
                ", order='" + order + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}