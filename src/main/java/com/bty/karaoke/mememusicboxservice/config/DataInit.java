package com.bty.karaoke.mememusicboxservice.config;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final RoomAreaService roomAreaService;

    @Override
    public void run(String... args) throws Exception {
        initRoomAreas();
    }

    private void initRoomAreas() {
        List<RoomAreaCreationRequest> roomAreas = List.of(
                RoomAreaCreationRequest.builder()
                        .areaName("VIP")
                        .description("Khu vực phòng VIP cao cấp")
                        .build(),

                RoomAreaCreationRequest.builder()
                        .areaName("Luxury")
                        .description("Khu vực phòng sang trọng dành cho nhóm đông")
                        .build(),

                RoomAreaCreationRequest.builder()
                        .areaName("Family")
                        .description("Khu vực phòng dành cho gia đình")
                        .build(),

                RoomAreaCreationRequest.builder()
                        .areaName("Student")
                        .description("Khu vực phòng giá rẻ dành cho sinh viên")
                        .build(),

                RoomAreaCreationRequest.builder()
                        .areaName("Couple")
                        .description("Khu vực phòng riêng tư dành cho cặp đôi")
                        .build()
        );

        for (RoomAreaCreationRequest roomArea : roomAreas) {
            try {
                roomAreaService.createRoomArea(roomArea);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
