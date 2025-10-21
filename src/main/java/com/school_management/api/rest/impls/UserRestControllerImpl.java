package com.school_management.api.rest.impls;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.User;
import com.school_management.api.policies.UserAccessPolicy;
import com.school_management.api.rest.interfaces.UserRestController;
import com.school_management.api.services.interfaces.UsersService;
import com.school_management.api.utils.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserRestControllerImpl implements UserRestController {
    private UsersService usersService;

    @Override
    public ResponseEntity<UserInfoDTO> getUserInfo() throws AccessDeniedException {
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();
        return ResponseEntity.ok().body(userInfoDTO);
    }

    @Override
    public ResponseEntity<AccountCreatedDTO> createUserAccount(CreateUserAccountDTO accountDTO) throws AccessDeniedException {
        return new ResponseEntity<>(this.usersService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> resetPassword(Long userId) {
        String newPassword = this.usersService.resetPassword(userId);
        return new ResponseEntity<>(Map.of("newPassword", newPassword), HttpStatus.OK);
    }
}
