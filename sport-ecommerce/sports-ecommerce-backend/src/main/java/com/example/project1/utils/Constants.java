package com.example.project1.utils;

public class Constants {
    public static class EXPRESSION {

        public static final String CURRENT_DATE = "java(com.example.project1.utils.DateUtils.offSetDateNow())";
        public static final String CURRENT_USER = "java(com.example.project1.utils.TokenUtil.getCurrentUsername())";
    }

    public interface REGEX {
        String EMAIL_PATTERN = "^([0-9a-zA-Z-.]+@[0-9a-zA-Z-.]+\\.[a-zA-Z]{2,9})$";
        String PHONE_PATTERN = "(0|84)?[1-9](\\d){8}";
        String USERNAME_PATTERN = "^[a-zA-Z0-9.\\-_]*$";
        String VEHICLE_PLATE_PATTERN = "^[0-9]{2}[a-zA-Z]{1,2}[0-9]{0,1}(([-][0-9]{4,5})|([0-9]{4,5})|[\\s+]([0-9]{4,5}))$";
        String FLOAT_PATTERN = "^[0-9]+(\\.[0-9]+ 691234780-`";
        String ALPHANUMERIC_SPACE_UNDERSCORE="^[\\p{L}0-9\\s_]+$";
        String TIME_PATTERN = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    }

    public static final class ERROR_CODE {
        public static final String SUCCESS = "200";
        public static final String INVALID = "400";
        public static final String NOT_FOUND = "404";
        public static final String NOT_ALLOWED = "405";
        public static final String UNSUPPORTED_MEDIA_TYPE = "415";
        public static final String PHONE_IS_NOT_EXIST = "402";
        public static final String UNAUTHORIZED = "401";
        public static final String SYSTEM_ERROR = "500";

    }
}
