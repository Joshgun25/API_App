package com.example.api_app.repository;

import com.example.api_app.model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByUserId(Long userId);
}