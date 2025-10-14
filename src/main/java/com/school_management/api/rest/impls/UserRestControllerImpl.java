package com.school_management.api.rest.impls;

import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.rest.interfaces.UserRestController;
import com.school_management.api.services.interfaces.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestControllerImpl implements UserRestController {
    @Autowired
    private UsersService usersService;

    @Override
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();
        return ResponseEntity.ok().body(userInfoDTO);
    }
}
