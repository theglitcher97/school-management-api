package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequestMapping("/api/v1/users")
public interface UserRestController {
    @GetMapping("/me")
    ResponseEntity<UserInfoDTO> getUserInfo() throws AccessDeniedException;

    @PostMapping()
    ResponseEntity<AccountCreatedDTO> createUserAccount(@Validated @RequestBody CreateUserAccountDTO accountDTO) throws AccessDeniedException;

    // todo: maybe we need to receive te email too
    // todo: return a DTO
    @PutMapping("/{userId}")
    ResponseEntity<Object> resetPassword(@PathVariable Long userId);

    /**'
     * Add the following endpoints
     * 1. update user information
     * 2. disable user
     * 2. remove user, keeping in mind how other entities could be affected
     */
}
