package com.competitors.webshop.automation.model;

public enum MatchStatus {

    NEW,
    PENDING,
    MATCHED,    // 100% збіг — exact або score > 0.88
    UNCERTAIN,  // score 0.65–0.88 — іде до GPT
    MANUAL,     // GPT не впевнений — ручна перевірка
    NO_MATCH,   // score < 0.65 — збігу немає
    FAILED      // системна помилка під час матчингу
}
