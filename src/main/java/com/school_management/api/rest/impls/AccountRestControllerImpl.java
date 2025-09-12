package com.school_management.api.rest.impls;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.rest.interfaces.AccountRestController;
import com.school_management.api.services.interfaces.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountRestControllerImpl implements AccountRestController {
    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountCreatedDTO> createUserAccount(CreateUserAccountDTO accountDTO) {
        return this.accountService.createAccount(accountDTO);
    }
}
