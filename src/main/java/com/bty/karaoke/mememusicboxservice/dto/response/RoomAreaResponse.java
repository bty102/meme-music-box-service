package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAreaResponse {

    private Long id;

    private String areaName;

    private String description;

    private Boolean isActive;
}
