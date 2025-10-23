package com.school_management.api.services.interfaces;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.User;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

public interface UsersService {
    AccountCreatedDTO createAccount(CreateUserAccountDTO accountDTO) throws AccessDeniedException;
    String resetPassword(Long userId);
    UserInfoDTO getCurrentUserInfo();
}
