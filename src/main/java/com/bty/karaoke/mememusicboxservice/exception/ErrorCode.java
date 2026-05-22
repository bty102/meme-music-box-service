package com.bty.karaoke.mememusicboxservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    INVALID_UPLOADED_IMAGE(1, "File must be image", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    IMAGE_UPLOAD_EXCEPTION(2, "Upload image failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // RoomArea
    AREA_NAME_NULL(1001, "Area name must be not null", HttpStatus.BAD_REQUEST),
    INVALID_AREA_NAME_SIZE(1002, "Area name size must be between 1 character and 100 characters", HttpStatus.BAD_REQUEST),
    INVALID_AREA_DESC_SIZE(1003, "Area description size must be between 0 character and 255 characters", HttpStatus.BAD_REQUEST),
    AREA_NAME_EXISTED(1004, "Area name already exists", HttpStatus.BAD_REQUEST),
    AREA_NOT_EXISTED(1005, "Area not exists", HttpStatus.BAD_REQUEST),
    AREA_ACTIVE_STATE_NULL(1006, "Area active state must be not null", HttpStatus.BAD_REQUEST),
    EXISTING_ACTIVE_ROOM_OF_AREA(1007, "Existing an active room of the area", HttpStatus.BAD_REQUEST),
    AREA_ID_NULL(1008, "Area id must be not null", HttpStatus.BAD_REQUEST),

    // Room
    ROOM_NUMBER_RANGE_NOT_VALID(2001, "Room number must be greater than or equal to 1", HttpStatus.BAD_REQUEST),
    ROOM_NUMBER_NULL(2002, "Room number must be not null", HttpStatus.BAD_REQUEST),
    ROOM_CAPACITY_NULL(2003, "Room capacity must be not null", HttpStatus.BAD_REQUEST),
    ROOM_CAPACITY_RANGE_NOT_VALID(2004, "Room capacity must be greater than or equal to 1", HttpStatus.BAD_REQUEST),
    ROOM_HOURLY_RATE_NULL(2005, "Room hourly rate must be not null", HttpStatus.BAD_REQUEST),
    ROOM_HOURLY_RATE_RANGE_NOT_VALID(2006, "Room hourly rate must be greater than or equal to 0", HttpStatus.BAD_REQUEST),
    ROOM_NUMBER_EXISTED(2007, "Room number already exists", HttpStatus.BAD_REQUEST),
    AREA_OF_ROOM_NOT_ACTIVE(2008, "Area of room not active", HttpStatus.BAD_REQUEST),
    ROOM_NOT_EXISTED(2009, "Room not exists", HttpStatus.BAD_REQUEST),
    ROOM_ACTIVE_STATE_NULL(2010, "Room active state must be not null", HttpStatus.BAD_REQUEST),
    ROOM_STATUS_NOT_AVAILABLE(2011, "Room status not available", HttpStatus.BAD_REQUEST),

    // Product
    PRODUCT_CODE_NULL(3001, "Product code must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_CODE_SIZE_NOT_VALID(3002, "Product code size must be between 1 and 20", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_NULL(3003, "Product name must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_SIZE_NOT_VALID(3004, "Product name size must be between 1 and 100", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_NULL(3005, "Product unit must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_SIZE_NOT_VALID(3006, "Product unit size must be between 1 and 20", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_PRICE_NULL(3007, "Product unit price must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_PRICE_RANGE_NOT_VALID(3008, "Product unit price must be greater than or equal to 0", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_QUANTITY_NULL(3009, "Product stock quantity must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_QUANTITY_RANGE_NOT_VALID(3010, "Product stock quantity must be greater than or equal to 0", HttpStatus.BAD_REQUEST),
    PRODUCT_CODE_EXISTED(3011, "Product code already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_EXISTED(3012, "Product name already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(3013, "Product not exists", HttpStatus.BAD_REQUEST),
    PRODUCT_ACTIVE_STATE_NULL(3014, "Product active state must be not null", HttpStatus.BAD_REQUEST),
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
