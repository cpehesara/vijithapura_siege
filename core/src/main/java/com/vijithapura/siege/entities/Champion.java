package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Champion unit - One of the 10 legendary warriors
 * Stronger than regular soldiers
 */
public class Champion extends Unit {
    private String title;
    private ChampionType championType;

    public enum ChampionType {
        NANDIMITHRA,
        SURANIMALA,
        MAHASONA,
        GOTHAIMBARA,
        THERAPUTTHABHAYA,
        VELUSUMANA,
        BHARANA,
        KHANJADEVA,
        PHUSSADEVA,
        LABHIYAVASABHA
    }

    public Champion(float x, float y, ChampionType type) {
        super(
            x, y,
            Constants.CHAMPION_HEALTH,
            Constants.CHAMPION_SPEED,
            Constants.CHAMPION_DAMAGE,
            Color.CYAN,
            type.name()
        );

        this.type = UnitType.CHAMPION;
        this.championType = type;
        this.title = getChampionTitle(type);
        this.size = 14;
        this.attackRange = 50f;
        this.attackCooldown = 0.8f;
        this.spriteKey = "champion"; // Link to champion.png sprite

        customizeChampion(type);
    }

    private void customizeChampion(ChampionType type) {
        switch (type) {
            case NANDIMITHRA:
                this.attackDamage *= 1.2f;
                this.color = new Color(0, 1, 1, 1);
                break;

            case SURANIMALA:
                this.attackDamage *= 1.5f;
                this.speed *= 0.9f;
                this.color = new Color(1, 0.5f, 0, 1);
                break;

            case VELUSUMANA:
                this.speed *= 1.5f;
                this.attackRange *= 0.8f;
                this.color = new Color(0.5f, 0.5f, 1, 1);
                break;

            case BHARANA:
                this.maxHealth *= 1.5f;
                this.health = this.maxHealth;
                this.speed *= 0.8f;
                this.color = new Color(0.8f, 0.8f, 0, 1);
                break;

            default:
                break;
        }
    }

    private String getChampionTitle(ChampionType type) {
        switch (type) {
            case NANDIMITHRA: return "The Commander";
            case SURANIMALA: return "The Mighty";
            case MAHASONA: return "The Fearless";
            case GOTHAIMBARA: return "The Swift";
            case THERAPUTTHABHAYA: return "The Skilled";
            case VELUSUMANA: return "The Rider";
            case BHARANA: return "The Shield";
            case KHANJADEVA: return "The Archer";
            case PHUSSADEVA: return "The Guardian";
            case LABHIYAVASABHA: return "The Wise";
            default: return "Champion";
        }
    }

    public String getTitle() {
        return title;
    }

    // Renamed to avoid conflict with Unit.getType()
    public ChampionType getChampionType() {
        return championType;
    }

    @Override
    public String getName() {
        return name + " " + title;
    }
}
