package com.hoanght.service.impl;

import com.hoanght.entity.Role;
import com.hoanght.entity.User;
import com.hoanght.repository.RoleRepository;
import com.hoanght.repository.UserRepository;
import com.hoanght.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createRoleIfNotFound("USER");
        createRoleIfNotFound("STAFF");
        createRoleIfNotFound("ADMIN");

        createAdminIfNotFound();

    }

    private void createRoleIfNotFound(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            roleRepository.save(new Role(roleName));
        }
    }

    private void createAdminIfNotFound() {
        Optional<User> user = userRepository.findByUsername("admin");
        if (user.isEmpty()) {
            List<Role> roles = roleRepository.findAll();
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setFullname("Admin");
            newUser.setPassword(passwordEncoder.encode("admin"));
            newUser.setRoles(Set.copyOf(roles));
            userRepository.save(newUser);
        }
    }
}
