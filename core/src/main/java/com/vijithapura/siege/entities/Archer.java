package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Archer unit - Long range attacks
 */
public class Archer extends Unit {
    private float projectileSpeed;

    public Archer(float x, float y) {
        super(
            x, y,
            Constants.ARCHER_HEALTH,
            Constants.ARCHER_SPEED,
            Constants.ARCHER_DAMAGE,
            new Color(0.6f, 0.8f, 0.3f, 1),
            "Archer"
        );

        this.type = UnitType.ARCHER;
        this.size = 11;
        this.attackRange = Constants.ARCHER_ATTACK_RANGE;
        this.attackCooldown = Constants.ARCHER_ATTACK_COOLDOWN;
        this.projectileSpeed = 400f;
        this.spriteKey = "archer";
    }

    public Archer(float x, float y, String name) {
        this(x, y);
        this.name = name;
    }

    @Override
    public void attack(Unit target) {
        if (canAttack() && target != null && target.isAlive()) {
            // Archers create projectiles instead of instant damage
            attackTimer = attackCooldown;
            currentTarget = target;
            isAttacking = true;
            attackAnimTimer = 0.3f;
            // Projectile will be created by combat system
        }
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }
}
