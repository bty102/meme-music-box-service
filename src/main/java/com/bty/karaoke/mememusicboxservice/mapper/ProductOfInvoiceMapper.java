package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.ProductOfInvoice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
    uses = {InvoiceMapper.class, ProductMapper.class}
)
public interface ProductOfInvoiceMapper {

    public ProductOfInvoice toProductOfInvoice(ProductOfInvoiceCreationRequest request);

    public ProductOfInvoiceResponse toProductOfInvoiceResponse(ProductOfInvoice productOfInvoice);

    public void updateProductOfInvoice(@MappingTarget ProductOfInvoice productOfInvoice, ProductOfInvoiceUpdateRequest request);
}
