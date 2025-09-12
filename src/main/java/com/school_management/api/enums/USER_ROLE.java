package com.school_management.api.enums;

import lombok.Getter;

@Getter
public enum USER_ROLE {
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT");

    final String value;

    USER_ROLE(String value){
        this.value = value;
    }
}
