package com.competitors.webshop.automation.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_results")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String ourProductId;        // FK на our_products
    private String competitorProductId; // FK на competitor_products

    @Enumerated(EnumType.STRING)
    private MatchStatus status;         // MATCHED / MANUAL / NO_MATCH / FAILED

    private float score;                // similarity score від Qdrant
    private String failReason;          // якщо FAILED

    private BigDecimal competitorPrice; // ціна конкурента на момент матчингу
    private BigDecimal recommendedPrice;// порахована PricingJob

    @CreatedDate
    private LocalDateTime matchedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}