package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/accounts")
public interface AccountRestController {
    @PostMapping()
    ResponseEntity<AccountCreatedDTO> createUserAccount(@RequestBody CreateUserAccountDTO accountDTO);
}
