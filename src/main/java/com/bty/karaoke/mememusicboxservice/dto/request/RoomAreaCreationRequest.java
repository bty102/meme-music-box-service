package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAreaCreationRequest {

    @NotNull(message = "AREA_NAME_NULL")
    @Size(min = 1, max = 100, message = "INVALID_AREA_NAME_SIZE")
    private String areaName;

    @Size(min = 0, max = 255, message = "INVALID_AREA_DESC_SIZE")
    private String description;
}
