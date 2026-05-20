package com.bty.karaoke.mememusicboxservice.config;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import com.bty.karaoke.mememusicboxservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final RoomAreaService roomAreaService;
    private final RoomService roomService;

    @Override
    public void run(String... args) throws Exception {
        initRoomAreas();
        initRooms();
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

    private void initRooms() {

        List<RoomCreationRequest> rooms = List.of(

                // VIP - areaId = 1
                RoomCreationRequest.builder().roomNumber(101).capacity(10).hourlyRate(new BigDecimal("300000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(102).capacity(10).hourlyRate(new BigDecimal("300000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(103).capacity(12).hourlyRate(new BigDecimal("350000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(104).capacity(12).hourlyRate(new BigDecimal("350000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(105).capacity(15).hourlyRate(new BigDecimal("400000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(106).capacity(15).hourlyRate(new BigDecimal("400000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(107).capacity(20).hourlyRate(new BigDecimal("500000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(108).capacity(20).hourlyRate(new BigDecimal("500000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(109).capacity(25).hourlyRate(new BigDecimal("600000")).areaId(1L).build(),
                RoomCreationRequest.builder().roomNumber(110).capacity(25).hourlyRate(new BigDecimal("600000")).areaId(1L).build(),

                // Luxury - areaId = 2
                RoomCreationRequest.builder().roomNumber(201).capacity(8).hourlyRate(new BigDecimal("250000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(202).capacity(8).hourlyRate(new BigDecimal("250000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(203).capacity(10).hourlyRate(new BigDecimal("280000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(204).capacity(10).hourlyRate(new BigDecimal("280000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(205).capacity(12).hourlyRate(new BigDecimal("300000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(206).capacity(12).hourlyRate(new BigDecimal("300000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(207).capacity(15).hourlyRate(new BigDecimal("350000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(208).capacity(15).hourlyRate(new BigDecimal("350000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(209).capacity(18).hourlyRate(new BigDecimal("400000")).areaId(2L).build(),
                RoomCreationRequest.builder().roomNumber(210).capacity(18).hourlyRate(new BigDecimal("400000")).areaId(2L).build(),

                // Family - areaId = 3
                RoomCreationRequest.builder().roomNumber(301).capacity(6).hourlyRate(new BigDecimal("180000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(302).capacity(6).hourlyRate(new BigDecimal("180000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(303).capacity(8).hourlyRate(new BigDecimal("200000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(304).capacity(8).hourlyRate(new BigDecimal("200000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(305).capacity(10).hourlyRate(new BigDecimal("220000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(306).capacity(10).hourlyRate(new BigDecimal("220000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(307).capacity(12).hourlyRate(new BigDecimal("250000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(308).capacity(12).hourlyRate(new BigDecimal("250000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(309).capacity(15).hourlyRate(new BigDecimal("280000")).areaId(3L).build(),
                RoomCreationRequest.builder().roomNumber(310).capacity(15).hourlyRate(new BigDecimal("280000")).areaId(3L).build(),

                // Student - areaId = 4
                RoomCreationRequest.builder().roomNumber(401).capacity(4).hourlyRate(new BigDecimal("100000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(402).capacity(4).hourlyRate(new BigDecimal("100000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(403).capacity(5).hourlyRate(new BigDecimal("120000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(404).capacity(5).hourlyRate(new BigDecimal("120000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(405).capacity(6).hourlyRate(new BigDecimal("140000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(406).capacity(6).hourlyRate(new BigDecimal("140000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(407).capacity(8).hourlyRate(new BigDecimal("160000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(408).capacity(8).hourlyRate(new BigDecimal("160000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(409).capacity(10).hourlyRate(new BigDecimal("180000")).areaId(4L).build(),
                RoomCreationRequest.builder().roomNumber(410).capacity(10).hourlyRate(new BigDecimal("180000")).areaId(4L).build(),

                // Couple - areaId = 5
                RoomCreationRequest.builder().roomNumber(501).capacity(2).hourlyRate(new BigDecimal("150000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(502).capacity(2).hourlyRate(new BigDecimal("150000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(503).capacity(2).hourlyRate(new BigDecimal("170000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(504).capacity(2).hourlyRate(new BigDecimal("170000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(505).capacity(3).hourlyRate(new BigDecimal("180000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(506).capacity(3).hourlyRate(new BigDecimal("180000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(507).capacity(4).hourlyRate(new BigDecimal("200000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(508).capacity(4).hourlyRate(new BigDecimal("200000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(509).capacity(4).hourlyRate(new BigDecimal("220000")).areaId(5L).build(),
                RoomCreationRequest.builder().roomNumber(510).capacity(4).hourlyRate(new BigDecimal("220000")).areaId(5L).build()
        );

        for (RoomCreationRequest room : rooms) {
            try {
                roomService.createRoom(room);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
