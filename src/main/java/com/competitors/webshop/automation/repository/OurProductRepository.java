package com.competitors.webshop.automation.repository;

import com.competitors.webshop.automation.model.OurProduct;
import com.competitors.webshop.automation.model.MatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OurProductRepository extends JpaRepository<OurProduct, String> {

    List<OurProduct> findAllByStatus(MatchStatus status, Pageable pageable);

    @Modifying
    @Query("UPDATE OurProduct p SET p.status = :status WHERE p.id = :id")
    void updateStatus(@Param("id") String id, @Param("status") MatchStatus status);

    @Modifying
    @Transactional
    @Query("""
        UPDATE OurProduct p SET
            p.normalizedName = :normalizedName,
            p.status         = :status
        WHERE p.id = :id
        """)
    void updateNormalized(
            @Param("id")             String id,
            @Param("normalizedName") String normalizedName,
            @Param("status")         MatchStatus status
    );
}
