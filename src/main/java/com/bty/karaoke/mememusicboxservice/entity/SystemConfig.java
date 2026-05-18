package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.SystemConfigDataType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "SystemConfig")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ConfigKey", unique = true, nullable = false, length = 50)
    private String configKey;

    @Column(name = "ConfigValue", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String configValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "DataType", nullable = false, length = 20)
    private SystemConfigDataType dataType;

    @Nationalized
    @Column(name = "Description", length = 255)
    private String description;
}
