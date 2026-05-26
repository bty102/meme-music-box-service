package com.bty.karaoke.mememusicboxservice.rabbitmq.consumer;

import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.rabbitmq.message.RoomBookingCreationMessage;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingConsumer {

    private final RoomBookingService roomBookingService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AccountRepository accountRepository;

    @RabbitListener(queues = "booking.queue",
            concurrency = "1"
    )
    public void processBooking(RoomBookingCreationMessage message) {
//        System.out.println("Received Booking from Room Booking Queue");
//        System.out.println(message.getRequest().getBookingTime().toString());
        Account member = accountRepository.findById(message.getMemberAccountId()).get();
        try {
            RoomBookingResponse response = roomBookingService.createRoomBooking(message.getRequest(), message.getMemberAccountId());

            simpMessagingTemplate.convertAndSendToUser(member.getEmail(), "/queue/booking",
                    ApiResponse.<RoomBookingResponse>builder()
                            .result(response)
                            .build()
                    );
        } catch (AppException e) {
//            e.printStackTrace();

            simpMessagingTemplate.convertAndSendToUser(member.getEmail(), "/queue/booking",
                    ApiResponse.<Void>builder()
                            .code(e.getErrorCode().getCode())
                            .message(e.getErrorCode().getMessage())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();

            simpMessagingTemplate.convertAndSendToUser(member.getEmail(), "/queue/booking",
                    ApiResponse.<Void>builder()
                            .code(ErrorCode.UNKNOWN_ERROR.getCode())
                            .message(ErrorCode.UNKNOWN_ERROR.getMessage())
                            .build()
            );
        }
    }
}
