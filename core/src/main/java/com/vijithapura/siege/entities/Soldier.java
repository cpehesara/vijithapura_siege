package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Basic soldier unit - standard combat
 */
public class Soldier extends Unit {

    public Soldier(float x, float y) {
        super(
            x, y,
            Constants.SOLDIER_HEALTH,
            Constants.SOLDIER_SPEED,
            Constants.SOLDIER_DAMAGE,
            Color.BLUE,
            "Soldier"
        );

        this.size = 20;
        this.attackRange = 40f;
        this.attackCooldown = 1.0f;
        this.spriteKey = "soldier"; // Link to soldier.png sprite
    }

    public Soldier(float x, float y, String name) {
        this(x, y);
        this.name = name;
    }
}
