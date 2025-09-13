package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
public interface AccountRestController {
    @PostMapping()
    ResponseEntity<AccountCreatedDTO> createUserAccount(@Validated @RequestBody CreateUserAccountDTO accountDTO);

    // todo: maybe we need to receive te email too
    // todo: return a DTO
    @PutMapping("/{userId}")
    ResponseEntity<Object> resetPassword(@PathVariable Long userId);
}
