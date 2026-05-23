package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.PointDiscountResponse;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PointDiscountService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public PointDiscountResponse createPointDiscount(@Valid PointDiscountCreationRequest request);

    public List<PointDiscountResponse> findAllPointDiscounts();

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public PointDiscountResponse updatePointDiscount(Long id, @Valid PointDiscountUpdateRequest request);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public void deletePointDiscount(Long id);
}
