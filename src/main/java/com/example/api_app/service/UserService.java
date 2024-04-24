package com.example.api_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api_app.model.User;
import com.example.api_app.model.Permission;
import com.example.api_app.model.UserPermission;
import com.example.api_app.model.UserType;
import com.example.api_app.model.UserTypePermission;
import com.example.api_app.repository.UserRepository;
import com.example.api_app.repository.PermissionRepository;
import com.example.api_app.repository.UserPermissionRepository;
import com.example.api_app.repository.UserTypeRepository;
import com.example.api_app.repository.UserTypePermissionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserTypePermissionRepository userTypePermissionRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Set<String> getUserFullInfoAndPermissions(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return new HashSet<>(); // Return an empty set if user not found
        }

        User user = userOptional.get();
        Set<Permission> permissions = getUserPermissions(userId);

        // Extracting permission names
        Set<String> permissionNames = new HashSet<>();
        for (Permission permission : permissions) {
            permissionNames.add(permission.getName());
        }

        // Concatenate user info and permissions
        Set<String> userFullInfoAndPermissions = new HashSet<>();
        userFullInfoAndPermissions.add("Username: " + user.getUsername());
        userFullInfoAndPermissions.add("Password: " + user.getPassword());
        userFullInfoAndPermissions.add("Name: " + (user.getName() != null ? user.getName() : ""));
        userFullInfoAndPermissions.add("Surname: " + (user.getSurname() != null ? user.getSurname() : ""));
        userFullInfoAndPermissions.add("Phone Number: " + (user.getPhoneNumber() != null ? user.getPhoneNumber() : ""));
        userFullInfoAndPermissions.add("Email: " + user.getEmail());
        userFullInfoAndPermissions.add("User Type ID: " + user.getUserTypeId());
        userFullInfoAndPermissions.add("Created At: " + user.getCreatedAt());
        userFullInfoAndPermissions.add("Updated At: " + (user.getUpdatedAt() != null ? user.getUpdatedAt() : ""));
        userFullInfoAndPermissions.add("Status: " + user.isStatus());
        userFullInfoAndPermissions.add("Created By: " + (user.getCreatedBy() != null ? user.getCreatedBy() : ""));
        userFullInfoAndPermissions.add("Updated By: " + (user.getUpdatedBy() != null ? user.getUpdatedBy() : ""));
        userFullInfoAndPermissions.addAll(permissionNames);

        return userFullInfoAndPermissions;
    }
    public Set<Permission> getUserPermissions(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Set<Permission> permissions = new HashSet<>();
        List<UserPermission> userPermissions = userPermissionRepository.findByUserId(userId);
        for (UserPermission userPermission : userPermissions) {
            Permission permission = permissionRepository.findById(userPermission.getPermissionId()).orElse(null);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        if(user.isPresent()){
            Optional<UserType> userType = userTypeRepository.findById(user.get().getUserTypeId());
            if (userType.isPresent()) {
                List<UserTypePermission> userTypePermissions = userTypePermissionRepository.findByUserTypeId(userType.get().getId());
                for (UserTypePermission userTypePermission : userTypePermissions) {
                    Permission permission = permissionRepository.findById(userTypePermission.getPermissionId()).orElse(null);
                    if (permission != null) {
                        permissions.add(permission);
                    }
                }
            }
        }
        return permissions;
    }
}
