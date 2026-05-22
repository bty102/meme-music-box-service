package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.PointDiscountResponse;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PointDiscountMapper {

    public PointDiscount toPointDiscount(PointDiscountCreationRequest pointDiscountCreationRequest);

    public PointDiscountResponse toPointDiscountResponse(PointDiscount pointDiscount);

    public void updatePointDiscount(@MappingTarget PointDiscount pointDiscount, PointDiscountUpdateRequest request);
}
