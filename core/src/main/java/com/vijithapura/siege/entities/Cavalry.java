package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Cavalry unit - Fast moving mounted warriors
 */
public class Cavalry extends Unit {
    private boolean isCharging;
    private float chargeTimer;
    private static final float CHARGE_DURATION = 3.0f;
    private static final float CHARGE_SPEED_MULTIPLIER = 1.8f;

    public Cavalry(float x, float y) {
        super(
            x, y,
            Constants.CAVALRY_HEALTH,
            Constants.CAVALRY_SPEED,
            Constants.CAVALRY_DAMAGE,
            new Color(0.7f, 0.5f, 0.2f, 1),
            "Cavalry"
        );

        this.type = UnitType.CAVALRY;
        this.size = 14;
        this.attackRange = Constants.CAVALRY_ATTACK_RANGE;
        this.attackCooldown = 0.9f;
        this.isCharging = false;
        this.chargeTimer = 0;
        this.spriteKey = "cavalry";
    }

    public Cavalry(float x, float y, String name) {
        this(x, y);
        this.name = name;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isCharging) {
            chargeTimer += delta;
            if (chargeTimer >= CHARGE_DURATION) {
                stopCharge();
            }
        }
    }

    @Override
    public void moveTo(float x, float y) {
        super.moveTo(x, y);
        // Auto-charge when moving long distances
        if (position.dst(x, y) > 150 && !isCharging) {
            startCharge();
        }
    }

    public void startCharge() {
        if (!isCharging) {
            isCharging = true;
            chargeTimer = 0;
            speedMultiplier *= CHARGE_SPEED_MULTIPLIER;
            damageMultiplier *= 1.3f;
        }
    }

    private void stopCharge() {
        isCharging = false;
        chargeTimer = 0;
    }

    @Override
    public void attack(Unit target) {
        float bonusDamage = isCharging ? attackDamage * 0.5f : 0;
        super.attack(target);
        if (bonusDamage > 0 && target != null) {
            target.takeDamage(bonusDamage);
        }
    }

    public boolean isCharging() {
        return isCharging;
    }
}
