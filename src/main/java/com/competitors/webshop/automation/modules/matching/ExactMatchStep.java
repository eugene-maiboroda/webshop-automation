package com.competitors.webshop.automation.modules.matching;

import com.competitors.webshop.automation.model.CompetitorProduct;
import com.competitors.webshop.automation.modules.matching.dto.MatchInput;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;
import com.competitors.webshop.automation.repository.CompetitorProductRepository;

import java.util.List;

public class ExactMatchStep {

    private final CompetitorProductRepository competitorRepo;

    public ExactMatchStep(CompetitorProductRepository competitorRepo) {
        this.competitorRepo = competitorRepo;
    }

    public MatchResultDto match(MatchInput input) {
        /*
        ⏺ Тоді є чітке рішення — pg_trgm + GIN індекс.

  PostgreSQL має розширення pg_trgm яке дозволяє індексувати колонку name і робити regex пошук з межами слова — швидко, без будь-якого ручного токенізування. Нові продукти автоматично індексуються.

  Одноразово в БД:
  CREATE EXTENSION IF NOT EXISTS pg_trgm;
  CREATE INDEX idx_competitor_name_trgm ON competitor_products USING GIN (name gin_trgm_ops);

  Репозиторій:
  @Query(value = "SELECT * FROM competitor_products WHERE name ~* CONCAT('\\m', :modelCode, '\\M') LIMIT 2", nativeQuery = true)
  List<CompetitorProduct> findTop2ByExactModelCode(@Param("modelCode") String modelCode);

  Метод match — фінальна версія:
  public MatchResultDto match(MatchInput input) {
      List<CompetitorProduct> found = competitorRepo.findTop2ByExactModelCode(input.modelCode());

      if (found.size() == 1) {
          return MatchResultDto.matched(found.getFirst().getName(), 1.0f);
      }

      return MatchResultDto.notMatched();
  }

  Переваги:
  - точний збіг по межі слова (\m...\M)
  - GIN індекс → швидко на 100k+ записів
  - нові продукти індексуються автоматично
  - нічого додаткового в коді підтримувати не треба

  Чи є у тебе доступ до міграцій Liquibase/Flyway щоб додати extension та індекс?
         */
        List<CompetitorProduct> found = competitorRepo.findByNameContaining(input.modelCode());
        if (found.size() == 1) {
            return MatchResultDto.matched(found.getFirst().getName(), 1.0f);
        }

        return MatchResultDto.notMatched();
    }
}
