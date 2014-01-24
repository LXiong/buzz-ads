package com.buzzinate.adx.util;

import com.buzzinate.adx.enums.Gender;

public class UserInfoUtils {
    
    public static Gender getGender(String gender) {
        if ("m".equals(gender)) {
            return Gender.male;
        } else {
            return Gender.female;
        }
    }
}
