package com.bty.karaoke.mememusicboxservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // RoomArea
    AREA_NAME_NULL(1001, "Area name must be not null", HttpStatus.BAD_REQUEST),
    INVALID_AREA_NAME_SIZE(1002, "Area name size must be between 1 character and 100 characters", HttpStatus.BAD_REQUEST),
    INVALID_AREA_DESC_SIZE(1003, "Area description size must be between 0 character and 255 characters", HttpStatus.BAD_REQUEST),
    AREA_NAME_EXISTED(1004, "Area name already exists", HttpStatus.BAD_REQUEST),
    AREA_NOT_EXISTED(1005, "Area not exists", HttpStatus.BAD_REQUEST),
    AREA_ACTIVE_STATE_NULL(1006, "Area active state must be not null", HttpStatus.BAD_REQUEST),
    EXISTING_ACTIVE_ROOM_OF_AREA(1007, "Existing an active room of the area", HttpStatus.BAD_REQUEST),
    ;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
