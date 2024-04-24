package com.example.api_app.repository;

import com.example.api_app.model.UserTypePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserTypePermissionRepository extends JpaRepository<UserTypePermission, Long> {

    List<UserTypePermission> findByUserTypeId(Long userTypeId);
}