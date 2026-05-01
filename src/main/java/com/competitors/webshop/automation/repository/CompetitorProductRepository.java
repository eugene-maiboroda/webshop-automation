package com.competitors.webshop.automation.repository;

import com.competitors.webshop.automation.model.CompetitorProduct;
import com.competitors.webshop.automation.model.MatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompetitorProductRepository extends JpaRepository<CompetitorProduct, String> {

@Query("""
    SELECT p FROM CompetitorProduct p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :modelCode, '%'))
    """)
List<CompetitorProduct> findByNameContaining(@Param("modelCode") String modelCode);

    List<CompetitorProduct> findAllByStatus(MatchStatus status, Pageable pageable);

    List<CompetitorProduct> findByNameIn(List<String> names);

    @Modifying
    @Transactional
    @Query("""
            UPDATE CompetitorProduct p SET
                p.normalizedName = :normalizedName,
                p.status         = :status
            WHERE p.id = :id
            """)
    void updateNormalized(
            @Param("id") String id,
            @Param("normalizedName") String normalizedName,
            @Param("status") MatchStatus status
    );
}


