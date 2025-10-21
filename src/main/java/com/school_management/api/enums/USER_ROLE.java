package com.school_management.api.enums;

import lombok.Getter;

@Getter
public enum USER_ROLE {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_TEACHER("ROLE_TEACHER"),
    ROLE_STUDENT("ROLE_STUDENT");

    final String value;

    USER_ROLE(String value){
        this.value = value;
    }
}
