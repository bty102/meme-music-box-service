package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomOfInvoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
    uses = {RoomMapper.class, InvoiceMapper.class}
)
public interface RoomOfInvoiceMapper {

    public RoomOfInvoice toRoomOfInvoice(RoomOfInvoiceCreationRequest request);

    public RoomOfInvoiceResponse toRoomOfInvoiceResponse(RoomOfInvoice roomOfInvoice);
}
