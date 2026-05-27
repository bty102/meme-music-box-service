package com.bty.karaoke.mememusicboxservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNKNOWN_ERROR(0, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
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
    ROOM_NOT_ACTIVE(2012, "Room not active", HttpStatus.BAD_REQUEST),
    CURRENT_TIME_INVALID_TO_OPEN_ROOM(2013, "Current time is invalid to open room", HttpStatus.BAD_REQUEST),
    ROOM_STATUS_INVALID_TO_OPEN_ROOM(2014, "Room status is invalid to open room", HttpStatus.BAD_REQUEST),
    ROOM_STATUS_INVALID_TO_TRANSFER_TO(2015, "Room status is invalid to transfer to", HttpStatus.BAD_REQUEST),
    CURRENT_TIME_INVALID_TO_TRANSFER_TO_ROOM(2016, "Current time is invalid to transfer to room", HttpStatus.BAD_REQUEST),

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

    // PointDiscount
    POINT_DISCOUNT_REQUIRED_POINT_NULL(4001, "Point discount must be not null", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_REQUIRED_POINT_IN_INVALID_RANGE(4002, "Point discount must be greater than or equal 0", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_DISCOUNT_PERCENT_NULL(4003, "Point discount percent must be not null", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_DISCOUNT_PERCENT_IN_INVALID_RANGE(4004, "Point discount percent must be between 0 and 100", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_DESCRIPTION_SIZE_NOT_VALID(4005, "Point discount description size must be between 0 and 255", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_REQUIRED_POINT_EXISTED(4006, "Required point already exists", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_NOT_EXISTED(4007, "Point discount not exists", HttpStatus.BAD_REQUEST),
    POINT_DISCOUNT_DELETION_FAILED(4008, "Point discount deletion is failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth
    ACCOUNT_NOT_EXISTED(5001, "Account not exists", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(5002, "Incorrect password", HttpStatus.BAD_REQUEST),
    ACCESSTOKEN_GENERATION_FAILED(5003, "Access token generation is failed", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGOUT_FAILED(5004, "Logout failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ACCESSTOKEN(5005, "Access token is invalid", HttpStatus.BAD_REQUEST),

    // Invoice
    INVOICE_DISCOUNT_PERCENT_NULL(6001, "Invoice discount percent must be not null", HttpStatus.BAD_REQUEST),
    INVOICE_DISCOUNT_PERCENT_IN_INVALID_RANGE(6002, "Invoice discount percent must be between 0 and 100", HttpStatus.BAD_REQUEST),
    INVOICE_VAT_PERCENT_NULL(6003, "Invoice vat percent must be not null", HttpStatus.BAD_REQUEST),
    INVOICE_VAT_PERCENT_IN_INVALID_RANGE(6004, "Invoice VAT percent must be between 0 and 100", HttpStatus.BAD_REQUEST),
    INVOICE_CREATOR_NULL(6005, "Invoice creator must be not null", HttpStatus.BAD_REQUEST),
    INVOICE_NOT_EXISTED(6006, "Invoice not exists", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_UPDATE(6007, "Invoice status is invalid to update", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_TRANSFER_TO_ROOM(6008, "Invoice status is invalid to transfer to room", HttpStatus.BAD_REQUEST),
    ROOM_CAPACITY_NOT_ENOUGH_TO_TRANSFER_TO(6009, "Room capacity not enough to transfer to", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_CHECK_OUT(6011, "Invoice status is invalid to check out", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_CONFIRM_PAYMENT(6012, "Invoice status is invalid to confirm payment", HttpStatus.BAD_REQUEST),
    TEMPORARY_INVOICE_OF_ROOM_NOT_FOUND(6013, "Temporary invoice of room not found", HttpStatus.BAD_REQUEST),

    // Account
    ACCOUNT_NOT_ACTIVE(7001, "Account is not active", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_MEMBER(7002, "Account is not member", HttpStatus.BAD_REQUEST),
    ACCOUNT_EXISTED(7003, "Account already exists", HttpStatus.BAD_REQUEST),
    ACCOUNT_PASSWORD_NULL(7004, "Password must be not null", HttpStatus.BAD_REQUEST),
    ACCOUNT_PASSWORD_SIZE_INVALID(7005, "Password size must be greater than or equal 0", HttpStatus.BAD_REQUEST),

    // SystemConfig
    DEFAULT_SYSTEM_CONFIG_NOT_CREATED(8001, "Default system config is not created", HttpStatus.INTERNAL_SERVER_ERROR),

    // RoomOfInvoce
    ROOM_STATUS_NOT_VALID_TO_CREATE_ROOM_OF_INVOICE(9001, "Room status is not valid to create room of invoice", HttpStatus.BAD_REQUEST),
    ROOM_OF_INVOICE_EXISTED(9002, "Room of invoice already exists", HttpStatus.BAD_REQUEST),
    EXISTING_ROOM_OF_INVOICE_NOT_TRANSFERRED(9003, "Existing room of invoice not transferred", HttpStatus.BAD_REQUEST),
    CURRENT_ROOM_ON_BILL(9004, "This is the current room on the bill", HttpStatus.BAD_REQUEST),

    // ProductOfInvoice
    PRODUCT_OF_INVOICE_QUANTITY_NULL(10001, "Product quantity in invoice must be not null", HttpStatus.BAD_REQUEST),
    PRODUCT_OF_INVOICE_QUANTITY_IN_INVALID_RANGE(10002, "Product quantity in invoice must be greater than or equal 0", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_CREATE_PRODUCT_OF_INVOICE(10003, "Invoice status is invalid to create product of invoice", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ACTIVE_TO_CREATE_PRODUCT_OF_INVOICE(10004, "Product not active to create product of invoice", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_QUANTITY_NOT_ENOUGH_TO_CREATE_PRODUCT_OF_INVOICE(10005, "Product stock quantity not enough to create product of invoice", HttpStatus.BAD_REQUEST),
    PRODUCT_OF_INVOICE_EXISTED(10006, "Product of invoice already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_OF_INVOICE_NOT_EXISTED(10007, "Product of invoice not exists", HttpStatus.BAD_REQUEST),
    INVOICE_STATUS_INVALID_TO_UPDATE_PRODUCT_OF_INVOICE(10008, "Invoice status is invalid to update product of invoice", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_QUANTITY_NOT_ENOUGH_TO_UPDATE_PRODUCT_OF_INVOICE(10009, "Product stock quantity not enough to update product of invoice", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ACTIVE_TO_UPDATE_PRODUCT_OF_INVOICE(10010, "Product not active to update product of invoice", HttpStatus.BAD_REQUEST),

    // RoomBooking
    BOOKING_TIME_NULL(11001, "Booking time must be not null", HttpStatus.BAD_REQUEST),
    BOOKING_TIME_INVALID(11002, "Booking time is invalid", HttpStatus.BAD_REQUEST),
    ROOM_STATUS_INVALID_TO_BOOK(11003, "Room status is invalid to book", HttpStatus.BAD_REQUEST),
    ROOM_BOOKING_NOT_EXISTED(11004, "Room booking not existed", HttpStatus.BAD_REQUEST),
    ROOM_BOOKING_STATUS_INVALID_TO_CHECK_IN(11005, "Room booking status is invalid to check in", HttpStatus.BAD_REQUEST),
    ROOM_STATUS_INVALID_TO_CHECK_IN_ROOM_BOOKING(11006, "Room status is invalid to check in room booking", HttpStatus.BAD_REQUEST),
    CURRENT_TIME_CANNOT_CHECK_IN_ROOM_BOOKING(11007, "Current time can't check in room booking", HttpStatus.BAD_REQUEST),
    ROOM_BOOKING_STATUS_INVALID_TO_CANCEL(11008, "Room status is invalid to cancel", HttpStatus.BAD_REQUEST),

    // OTP
    SEND_REGISTRATION_OTP_UNSUCCESSFULLY(12001, "Send registration OTP unsuccessfully", HttpStatus.INTERNAL_SERVER_ERROR),
    OTP_INVALID(12002, "OTP invalid", HttpStatus.BAD_REQUEST),

    // MemberProfile
    MEMBER_PROFILE_FULL_NAME_NULL(13001, "Full name must be not null", HttpStatus.BAD_REQUEST),
    MEMBER_PROFILE_FULL_NAME_SIZE_INVALID(13002, "Full name size must be greater than or equal 0", HttpStatus.BAD_REQUEST),
    MEMBER_PROFILE_IS_MALE_NULL(13003, "Is male must be not null", HttpStatus.BAD_REQUEST),
    MEMBER_PROFILE_DATE_OF_BIRTH_NULL(13004, "Date of birth must be not null", HttpStatus.BAD_REQUEST),
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
