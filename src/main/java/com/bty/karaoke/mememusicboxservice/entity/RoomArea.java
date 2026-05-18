package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RoomArea")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Nationalized
    @Column(name = "AreaName", unique = true, nullable = false, length = 100)
    private String areaName;

    @Nationalized
    @Column(name = "Description", length = 255)
    private String description;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "area")
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
