// src/main/java/com/spikka/digifolio/model/AchievementType.java
package com.spikka.digifolio.model;

public enum AchievementType {
    ACADEMIC("Академическое"),
    SPORT("Спортивное"),
    SOCIAL("Социальное"),
    PROJECT("Проектное"),
    OTHER("Другое");

    private final String displayName;
    AchievementType(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
