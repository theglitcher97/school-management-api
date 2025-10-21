package com.school_management.api.enums;

import lombok.Getter;

@Getter
public enum USER_TYPE {
    TEACHER("TEACHER"), STUDENT("STUDENT"), ADMIN("ADMIN");

    private String value;

    USER_TYPE(String value){this.value = value;}
}
