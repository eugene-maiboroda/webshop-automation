package com.competitors.webshop.automation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "our_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OurProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String normalizedName;
    private String modelCode;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}