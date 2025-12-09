package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Siege Ram - Slow but devastating against gates and buildings
 */
public class SiegeRam extends Unit {
    private int crewCount;
    private static final int MAX_CREW = 3;

    public SiegeRam(float x, float y) {
        super(
            x, y,
            Constants.RAM_HEALTH,
            Constants.RAM_SPEED,
            Constants.RAM_DAMAGE,
            new Color(0.4f, 0.3f, 0.2f, 1),
            "Siege Ram"
        );

        this.type = UnitType.SIEGE_RAM;
        this.size = 18;
        this.attackRange = 25f;
        this.attackCooldown = 2.0f;
        this.armor = 5f;
        this.baseArmor = 5f;
        this.crewCount = MAX_CREW;
        this.spriteKey = "ram";
    }

    @Override
    public void attackGate(Gate gate) {
        if (canAttack() && gate != null && !gate.isDestroyed()) {
            float damage = attackDamage * Constants.RAM_GATE_BONUS * (crewCount / (float)MAX_CREW);
            gate.takeDamage(damage);
            attackTimer = attackCooldown;
            targetGate = gate;
            isAttacking = true;
            attackAnimTimer = 0.3f;
        }
    }

    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        // Crew can be killed, reducing effectiveness
        if (health < maxHealth * 0.7f && crewCount > 2) crewCount = 2;
        if (health < maxHealth * 0.4f && crewCount > 1) crewCount = 1;
    }

    public int getCrewCount() {
        return crewCount;
    }
}
