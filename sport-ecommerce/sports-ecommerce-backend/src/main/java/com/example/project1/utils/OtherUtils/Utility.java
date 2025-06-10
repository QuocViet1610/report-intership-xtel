package com.example.project1.utils.OtherUtils;

import java.time.OffsetDateTime;

public class Utility {

    public static Long offsetDateTimeToEpochMilli(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant().toEpochMilli();
    }

}
