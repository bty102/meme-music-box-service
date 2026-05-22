package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.PointDiscountResponse;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import jakarta.validation.Valid;

import java.util.List;

public interface PointDiscountService {

    public PointDiscountResponse createPointDiscount(@Valid PointDiscountCreationRequest request);

    public List<PointDiscountResponse> findAllPointDiscounts();

    public PointDiscountResponse updatePointDiscount(Long id, @Valid PointDiscountUpdateRequest request);

    public void deletePointDiscount(Long id);
}
