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
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {

    @InjectSequenceValue(sequencename = "seq", tablename = "user")
    @Column(name = "seq", unique = true, nullable = false, updatable = false)
    @Setter
    public long seq;

    @Id // Make seq the primary key
    @Column(name = "user_id", unique = true, nullable = false)
    @Setter
    private String userId = AppConstants.TEMPORARY_ID;;

    @Column(name = "pass_salt")
    private String passSalt;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    // @PrePersist, which means it is a JPA lifecycle callback method. This method is automatically called
    // before the entity is persisted (saved for the first time) into the database.
    public void onSave(){
        if(!CommonUtils.hasValue(userId) || AppConstants.TEMPORARY_ID.equals(userId))
            userId = "user_" + String.format("%06d", seq);
        updatedAt = LocalDateTime.now();
    }
}
