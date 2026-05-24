package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    uses = {AccountMapper.class}
)
public interface InvoiceMapper {

    @Mapping(source = "createdByAccount", target = "createdBy")
    @Mapping(source = "memberAccount", target = "member")
    public InvoiceResponse toInvoiceResponse(Invoice invoice);

    public Invoice toInvoice(InvoiceCreationRequest request);
}
