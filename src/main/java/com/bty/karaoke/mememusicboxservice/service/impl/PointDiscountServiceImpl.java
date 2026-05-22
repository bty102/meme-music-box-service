package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.PointDiscountResponse;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.PointDiscountMapper;
import com.bty.karaoke.mememusicboxservice.repository.PointDiscountRepository;
import com.bty.karaoke.mememusicboxservice.service.PointDiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PointDiscountServiceImpl implements PointDiscountService {

    private final PointDiscountRepository pointDiscountRepository;
    private final PointDiscountMapper pointDiscountMapper;

    @Override
    public PointDiscountResponse createPointDiscount(@Valid PointDiscountCreationRequest request) {
        if(pointDiscountRepository.existsByRequiredPoint(request.getRequiredPoint())) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_REQUIRED_POINT_EXISTED);
        }

        PointDiscount pointDiscount = pointDiscountMapper.toPointDiscount(request);
        pointDiscount = pointDiscountRepository.save(pointDiscount);
        return pointDiscountMapper.toPointDiscountResponse(pointDiscount);
    }

    @Override
    public List<PointDiscountResponse> findAllPointDiscounts() {
        return pointDiscountRepository.findAll().stream()
                .map(pointDiscount -> pointDiscountMapper.toPointDiscountResponse(pointDiscount))
                .toList();
    }

    @Override
    public PointDiscountResponse updatePointDiscount(Long id, @Valid PointDiscountUpdateRequest request) {
        if(id == null) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_NOT_EXISTED);
        }

        PointDiscount pointDiscount = pointDiscountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POINT_DISCOUNT_NOT_EXISTED));

        if(pointDiscountRepository.existsByRequiredPointAndIdIsNot(request.getRequiredPoint(), id)) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_REQUIRED_POINT_EXISTED);
        }

        pointDiscountMapper.updatePointDiscount(pointDiscount, request);
        pointDiscount = pointDiscountRepository.save(pointDiscount);
        return pointDiscountMapper.toPointDiscountResponse(pointDiscount);
    }

    @Override
    public void deletePointDiscount(Long id) {
        if(id == null) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_NOT_EXISTED);
        }
        if(!pointDiscountRepository.existsById(id)) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_NOT_EXISTED);
        }
        try {
            pointDiscountRepository.deleteById(id);
        } catch (Exception e) {
            throw new AppException(ErrorCode.POINT_DISCOUNT_DELETION_FAILED);
        }
    }
}
