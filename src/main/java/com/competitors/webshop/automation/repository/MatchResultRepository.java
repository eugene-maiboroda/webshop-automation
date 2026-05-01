package com.competitors.webshop.automation.repository;

import com.competitors.webshop.automation.model.MatchResult;
import com.competitors.webshop.automation.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchResultRepository extends JpaRepository<MatchResult, String> {

    List<MatchResult> findAllByStatus(MatchStatus status);

    Optional<MatchResult> findByOurProductId(String ourProductId);

    boolean existsByOurProductIdAndStatus(String ourProductId, MatchStatus status);
}