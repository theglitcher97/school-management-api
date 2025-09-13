package com.school_management.api.utils;

import org.springframework.stereotype.Component;

@Component
public class AppUtils {
    public static String getNextCode(String latestCode) {
        byte codeLength = 7;
        char ch = latestCode.charAt(0);
        String id = ""+(Long.parseLong(latestCode.substring(1)) + 1); // adds 1 to current latest code

        // finish the creation of the new code
        StringBuilder code = new StringBuilder();
        code.append(id);
        code.insert(0,  "0".repeat(codeLength - id.length()));
        code.insert(0,  ch);
        return code.toString();
    }
}
