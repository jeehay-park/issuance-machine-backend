package com.ictk.issuance.repository;

import com.ictk.issuance.data.model.User;
import com.ictk.issuance.repository.dao.IssuanceDao;
import com.ictk.issuance.repository.dao.UserDao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String>, UserDao, IssuanceDao {

    // JpaRepository<User, Long> provides CRUD operations automatically.
    User findByUserId(String userId);

    // Custom method to check if a user exists by userId
    boolean existsByUserId(String userId);

    // Custom method to check if a user exists by email
    boolean existsByEmail(String email);

    // Find pass_salt by userId using a native query
    @Query(value = "SELECT pass_salt FROM user WHERE user_id = :userId", nativeQuery = true)
    String findPassSaltByUserId(@Param("userId") String userId);

    // Find password_hash by userId using a native query
    @Query(value = "SELECT password_hash FROM user WHERE user_id = :userId", nativeQuery = true)
    String findPasswordHashByUserId(@Param("userId") String userId);
}
