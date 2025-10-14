package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
public interface UserRestController {
    @GetMapping("/me")
    ResponseEntity<UserInfoDTO> getUserInfo();
}
