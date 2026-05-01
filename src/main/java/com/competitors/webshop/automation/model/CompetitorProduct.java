package com.competitors.webshop.automation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "competitor_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String normalizedName;

    private String competitorSource; // звідки
    private String competitorUrl;    // пряме посилання на товар
    private BigDecimal price;
    private String currency;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}