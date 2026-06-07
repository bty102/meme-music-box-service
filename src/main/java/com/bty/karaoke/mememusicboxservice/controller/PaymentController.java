package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.config.VNPayConfig;
import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.repository.InvoiceRepository;
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import com.bty.karaoke.mememusicboxservice.util.JwtUtil;
import com.bty.karaoke.mememusicboxservice.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayConfig vnPayConfig;
    private final InvoiceRepository invoiceRepository;
    private final TemplateEngine templateEngine;
    private final InvoiceService invoiceService;
    private final JwtUtil jwtUtil;
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @PostMapping(path = "/vnpay/createPaymentUrl", produces = "application/json")
    public ResponseEntity<ApiResponse<String>> createPaymentUrl(
            @RequestParam(name = "invoiceCode") String invoiceCode,
            HttpServletRequest request
    ) throws UnsupportedEncodingException {

        Invoice invoice = invoiceRepository.findByInvoiceCode(invoiceCode)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        String vnpTxnRef = invoice.getInvoiceCode() + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnPayConfig.getTmnCode());

        String amount = invoice.getFinalAmount()
                .multiply(BigDecimal.valueOf(100))
                .toBigInteger()
                .toString();

        params.put("vnp_Amount", amount);

        params.put("vnp_CurrCode", "VND");

        params.put("vnp_TxnRef", vnpTxnRef);

        params.put("vnp_OrderInfo", "Thanh toan hoa don " + invoice.getInvoiceCode());

        params.put("vnp_OrderType", "other");

        params.put("vnp_Locale", "vn");

        params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());

        params.put("vnp_IpAddr",
                VNPayUtil.getIpAddress(request));
//        params.put("vnp_IpAddr",
//                "127.0.0.1");

        params.put("vnp_CreateDate",
                new SimpleDateFormat("yyyyMMddHHmmss")
                        .format(new Date()));

        List<String> fieldNames =
                new ArrayList<>(params.keySet());

        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {

            String value = params.get(fieldName);

            if (value != null && value.length() > 0) {

                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                query.append('&');
                hashData.append('&');
            }
        }

        query.deleteCharAt(query.length() - 1);
        hashData.deleteCharAt(hashData.length() - 1);

        String secureHash = VNPayUtil.hmacSHA512(
                vnPayConfig.getHashSecret(),
                hashData.toString()
        );

        query.append("&vnp_SecureHash=");
        query.append(secureHash);

        String paymentUrl =
                vnPayConfig.getPayUrl() + "?" + query;

//        System.out.println(hashData);
//        System.out.println(secureHash);
//        System.out.println(vnPayConfig.getTmnCode());
//        System.out.println(vnPayConfig.getHashSecret());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result(paymentUrl)
                        .build()
        );
    }

    @GetMapping(path = "/vnpay/return", produces = MediaType.TEXT_HTML_VALUE)
    public String paymentReturn(
            @RequestParam Map<String, String> params

    ) {
        Context context = new Context();
        String responseCode = params.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {

            context.setVariable("paymentResult", "Thanh toán thành công");
            return templateEngine.process("/vnpay/payment-result", context);
        }

        context.setVariable("paymentResult", "Thanh toán thất bại");
        return templateEngine.process("/vnpay/payment-result", context);
    }

    @GetMapping("/vnpay/ipn")
    public ResponseEntity<?> ipn(
            @RequestParam Map<String, String> params
    ) {

        String responseCode = params.get("vnp_ResponseCode");

        String txnRef = params.get("vnp_TxnRef");

        String invoiceCode = txnRef.split("_")[0];

        Invoice invoice = invoiceRepository.findByInvoiceCode(invoiceCode)
                .orElse(null);
        if (invoice == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "RspCode", "01",
                            "Message", "Invoice not Found"
                    )
            );
        }

        if ("00".equals(responseCode)) {

            // update DB paid
//            System.out.println("Updated DB");
            try {

//                var auth = new UsernamePasswordAuthenticationToken(
//                        "system",
//                        null,
//                        List.of(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()))
//                );
                String token = jwtUtil.generateToken(invoice.getCreatedByAccount());
                Jwt jwt = jwtDecoder.decode(token);
                Authentication auth = jwtAuthenticationConverter.convert(jwt);

                SecurityContextHolder.getContext().setAuthentication(auth);

                invoiceService.paymentConfirmation(invoice.getId());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(
                        Map.of(
                                "RspCode", "99",
                                "Message", "Unknow error"
                        )
                );
            } finally {

                SecurityContextHolder.clearContext();
            }
        }

        return ResponseEntity.ok(Map.of(
                "RspCode", "00",
                "Message", "Confirm Success"
        ));
    }
}
