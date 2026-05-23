package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "InvalidToken")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvalidToken {
    @Id
    @Column(name = "Id")
    private String id;

    @Column(name = "ExpiryTime", nullable = false)
    private Date expiryTime;
}
