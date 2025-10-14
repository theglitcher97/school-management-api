package com.school_management.api.services.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;

public interface UsersService {
    ResponseEntity<AccountCreatedDTO> createAccount(CreateUserAccountDTO accountDTO);

    String resetPassword(Long userId);

    UserInfoDTO getCurrentUserInfo();
}
