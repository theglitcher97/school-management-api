package com.school_management.api.rest.impls;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.rest.interfaces.AccountRestController;
import com.school_management.api.services.interfaces.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AccountRestControllerImpl implements AccountRestController {
    private final UsersService usersService;

    @Override
    public ResponseEntity<AccountCreatedDTO> createUserAccount(CreateUserAccountDTO accountDTO) {
        return new ResponseEntity<>(this.usersService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> resetPassword(Long userId) {
        String newPassword = this.usersService.resetPassword(userId);
        return new ResponseEntity<>(Map.of("newPassword", newPassword), HttpStatus.OK);
    }
}
