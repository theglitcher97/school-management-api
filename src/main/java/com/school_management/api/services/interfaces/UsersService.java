package com.school_management.api.services.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.User;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    ResponseEntity<AccountCreatedDTO> createAccount(CreateUserAccountDTO accountDTO);

    String resetPassword(Long userId);

    UserInfoDTO getCurrentUserInfo();
    User getCurrentUser();
}
