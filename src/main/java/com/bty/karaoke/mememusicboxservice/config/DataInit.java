package com.bty.karaoke.mememusicboxservice.config;

import com.bty.karaoke.mememusicboxservice.constant.DefaultSystemConfig;
import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.constant.SystemConfigDataType;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.EmployeeProfile;
import com.bty.karaoke.mememusicboxservice.entity.MemberProfile;
import com.bty.karaoke.mememusicboxservice.entity.SystemConfig;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.SystemConfigRepository;
import com.bty.karaoke.mememusicboxservice.service.PointDiscountService;
import com.bty.karaoke.mememusicboxservice.service.ProductService;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import com.bty.karaoke.mememusicboxservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final RoomAreaService roomAreaService;
    private final RoomService roomService;
    private final ProductService productService;
    private final PointDiscountService pointDiscountService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemConfigRepository systemConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            var auth = new UsernamePasswordAuthenticationToken(
                    "system",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            // Default system configs
            defaultSystemConfig();

            // Init data
            initRoomAreas();
            initRooms();
            initProducts();
            initPointDiscounts();
            initAccounts();
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void defaultSystemConfig() {
        List<SystemConfig> systemConfigs = new ArrayList<>();
        for(DefaultSystemConfig config : DefaultSystemConfig.values()) {
            systemConfigs.add(
                    SystemConfig.builder()
                            .configKey(config.getConfigKey())
                            .configValue(config.getConfigValue())
                            .dataType(config.getDataType())
                            .description(config.getDescription())
                            .build()
            );
        }
        systemConfigRepository.saveAll(systemConfigs);
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

    private void initProducts() {

        List<ProductCreationRequest> products = List.of(

                ProductCreationRequest.builder().productCode("BIA001").productName("Bia Huda").unit("Lon").unitPrice(new BigDecimal("20000")).stockQuantity(200).build(),
                ProductCreationRequest.builder().productCode("BIA002").productName("Bia Tiger").unit("Lon").unitPrice(new BigDecimal("25000")).stockQuantity(200).build(),
                ProductCreationRequest.builder().productCode("BIA003").productName("Bia Heineken").unit("Lon").unitPrice(new BigDecimal("30000")).stockQuantity(200).build(),
                ProductCreationRequest.builder().productCode("BIA004").productName("Bia Saigon").unit("Lon").unitPrice(new BigDecimal("18000")).stockQuantity(200).build(),
                ProductCreationRequest.builder().productCode("BIA005").productName("Bia Larue").unit("Lon").unitPrice(new BigDecimal("17000")).stockQuantity(200).build(),

                ProductCreationRequest.builder().productCode("NUOC001").productName("Coca Cola").unit("Lon").unitPrice(new BigDecimal("15000")).stockQuantity(300).build(),
                ProductCreationRequest.builder().productCode("NUOC002").productName("Pepsi").unit("Lon").unitPrice(new BigDecimal("15000")).stockQuantity(300).build(),
                ProductCreationRequest.builder().productCode("NUOC003").productName("7 Up").unit("Lon").unitPrice(new BigDecimal("15000")).stockQuantity(300).build(),
                ProductCreationRequest.builder().productCode("NUOC004").productName("Sting Dâu").unit("Lon").unitPrice(new BigDecimal("16000")).stockQuantity(300).build(),
                ProductCreationRequest.builder().productCode("NUOC005").productName("Red Bull").unit("Lon").unitPrice(new BigDecimal("18000")).stockQuantity(300).build(),

                ProductCreationRequest.builder().productCode("SNACK001").productName("Khô Gà Lá Chanh").unit("Đĩa").unitPrice(new BigDecimal("89000")).stockQuantity(100).build(),
                ProductCreationRequest.builder().productCode("SNACK002").productName("Khô Bò").unit("Đĩa").unitPrice(new BigDecimal("99000")).stockQuantity(100).build(),
                ProductCreationRequest.builder().productCode("SNACK003").productName("Cá Viên Chiên").unit("Phần").unitPrice(new BigDecimal("69000")).stockQuantity(100).build(),
                ProductCreationRequest.builder().productCode("SNACK004").productName("Xúc Xích Chiên").unit("Phần").unitPrice(new BigDecimal("59000")).stockQuantity(100).build(),
                ProductCreationRequest.builder().productCode("SNACK005").productName("Khoai Tây Chiên").unit("Phần").unitPrice(new BigDecimal("79000")).stockQuantity(100).build(),

                ProductCreationRequest.builder().productCode("TRAI001").productName("Trái Cây Thập Cẩm").unit("Đĩa").unitPrice(new BigDecimal("120000")).stockQuantity(50).build(),
                ProductCreationRequest.builder().productCode("TRAI002").productName("Dưa Hấu").unit("Đĩa").unitPrice(new BigDecimal("70000")).stockQuantity(50).build(),
                ProductCreationRequest.builder().productCode("TRAI003").productName("Ổi Xí Muội").unit("Đĩa").unitPrice(new BigDecimal("65000")).stockQuantity(50).build(),
                ProductCreationRequest.builder().productCode("TRAI004").productName("Cóc Lắc").unit("Đĩa").unitPrice(new BigDecimal("69000")).stockQuantity(50).build(),
                ProductCreationRequest.builder().productCode("TRAI005").productName("Xoài Muối Ớt").unit("Đĩa").unitPrice(new BigDecimal("75000")).stockQuantity(50).build(),

                ProductCreationRequest.builder().productCode("MI001").productName("Mì Xào Bò").unit("Phần").unitPrice(new BigDecimal("99000")).stockQuantity(80).build(),
                ProductCreationRequest.builder().productCode("MI002").productName("Mì Xào Hải Sản").unit("Phần").unitPrice(new BigDecimal("109000")).stockQuantity(80).build(),
                ProductCreationRequest.builder().productCode("COM001").productName("Cơm Chiên Hải Sản").unit("Phần").unitPrice(new BigDecimal("119000")).stockQuantity(80).build(),
                ProductCreationRequest.builder().productCode("COM002").productName("Cơm Chiên Dương Châu").unit("Phần").unitPrice(new BigDecimal("99000")).stockQuantity(80).build(),
                ProductCreationRequest.builder().productCode("LAU001").productName("Lẩu Thái").unit("Nồi").unitPrice(new BigDecimal("299000")).stockQuantity(30).build(),

                ProductCreationRequest.builder().productCode("THUOC001").productName("Thuốc Marlboro").unit("Gói").unitPrice(new BigDecimal("45000")).stockQuantity(100).build(),
                ProductCreationRequest.builder().productCode("THUOC002").productName("Thuốc Jet").unit("Gói").unitPrice(new BigDecimal("30000")).stockQuantity(100).build(),

                ProductCreationRequest.builder().productCode("KHAN001").productName("Khăn Lạnh").unit("Cái").unitPrice(new BigDecimal("5000")).stockQuantity(500).build(),
                ProductCreationRequest.builder().productCode("NUOC006").productName("Nước Suối Aquafina").unit("Chai").unitPrice(new BigDecimal("10000")).stockQuantity(500).build(),
                ProductCreationRequest.builder().productCode("NUOC007").productName("Trà Xanh Không Độ").unit("Chai").unitPrice(new BigDecimal("18000")).stockQuantity(300).build()
        );

        for (ProductCreationRequest product : products) {
            try {
                productService.createProduct(product);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void initPointDiscounts() {

        List<PointDiscountCreationRequest> pointDiscounts = List.of(

                PointDiscountCreationRequest.builder()
                        .requiredPoint(100)
                        .discountPercent(new BigDecimal("5"))
                        .description("Giảm 5% cho hội viên đạt 100 điểm")
                        .build(),

                PointDiscountCreationRequest.builder()
                        .requiredPoint(300)
                        .discountPercent(new BigDecimal("10"))
                        .description("Giảm 10% cho hội viên đạt 300 điểm")
                        .build(),

                PointDiscountCreationRequest.builder()
                        .requiredPoint(500)
                        .discountPercent(new BigDecimal("15"))
                        .description("Giảm 15% cho hội viên đạt 500 điểm")
                        .build(),

                PointDiscountCreationRequest.builder()
                        .requiredPoint(1000)
                        .discountPercent(new BigDecimal("20"))
                        .description("Giảm 20% cho hội viên đạt 1000 điểm")
                        .build(),

                PointDiscountCreationRequest.builder()
                        .requiredPoint(2000)
                        .discountPercent(new BigDecimal("30"))
                        .description("Giảm 30% cho hội viên VIP đạt 2000 điểm")
                        .build()
        );

        for (PointDiscountCreationRequest pointDiscount : pointDiscounts) {
            try {
                pointDiscountService.createPointDiscount(pointDiscount);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void initAccounts() {
        List<Account> accounts = List.of(
                // ADMIN
                Account.builder()
                        .email("admin@gmail.com")
                        .passwordHash(passwordEncoder.encode("admin"))
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .isActive(true)
                        .build(),

                // EMPLOYEE
                Account.builder()
                        .email("employee@gmail.com")
                        .passwordHash(passwordEncoder.encode("employee"))
                        .role(Role.EMPLOYEE)
                        .createdAt(LocalDateTime.now())
                        .isActive(true)
                        .employeeProfile(
                                EmployeeProfile.builder()
                                        .employeeCode("EMP002")
                                        .fullName("Trần Thị Nhân Viên")
                                        .phoneNumber("0900000002")
                                        .nationalId("001203000002")
                                        .isMale(false)
                                        .dateOfBirth(LocalDate.of(2000, 5, 10))
                                        .address("Đà Nẵng")
                                        .imageUrl(null)
                                        .build()
                        )
                        .build(),

                // MEMBER
                Account.builder()
                        .email("member@gmail.com")
                        .passwordHash(passwordEncoder.encode("member"))
                        .role(Role.MEMBER)
                        .createdAt(LocalDateTime.now())
                        .isActive(true)
                        .memberProfile(
                                MemberProfile.builder()
                                        .memberCode("MEM001")
                                        .fullName("Lê Văn Hội Viên")
                                        .isMale(true)
                                        .dateOfBirth(LocalDate.of(2002, 8, 20))
                                        .loyaltyPoint(500)
                                        .imageUrl(null)
                                        .build()
                        )
                        .build()
        );

        for (Account account : accounts) {
            if (account.getEmployeeProfile() != null) {
                account.getEmployeeProfile().setAccount(account);
            }
            if (account.getMemberProfile() != null) {
                account.getMemberProfile().setAccount(account);
            }
        }

        for (Account account : accounts) {
            accountRepository.save(account);
        }
    }
}
