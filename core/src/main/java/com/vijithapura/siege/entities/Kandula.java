package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.utils.Constants;

/**
 * Kandula - King's royal elephant
 * Special ability: Charge attack to break gates
 */
public class Kandula extends Unit {
    private boolean isCharging;
    private float chargeTimer;
    private float chargeCooldown;
    private static final float CHARGE_DURATION = 2.0f;
    private static final float CHARGE_COOLDOWN_TIME = 10.0f;
    private static final float CHARGE_DAMAGE_MULTIPLIER = 5.0f;

    public Kandula(float x, float y) {
        super(
            x, y,
            300,
            Constants.ELEPHANT_SPEED,
            50,
            new Color(0.4f, 0.4f, 0.4f, 1),
            "Kandula"
        );

        this.type = UnitType.ELEPHANT;
        this.size = 35;
        this.attackRange = 30f;
        this.attackCooldown = 1.5f;
        this.isCharging = false;
        this.chargeTimer = 0;
        this.chargeCooldown = 0;
        this.spriteKey = "elephant"; // Link to elephant.png sprite
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (chargeCooldown > 0) {
            chargeCooldown -= delta;
        }

        if (isCharging) {
            chargeTimer += delta;

            if (isMoving) {
                speed = Constants.ELEPHANT_SPEED * 2.0f;
            }

            if (chargeTimer >= CHARGE_DURATION) {
                stopCharge();
            }
        }
    }

    public boolean startCharge() {
        if (chargeCooldown <= 0) {
            isCharging = true;
            chargeTimer = 0;
            chargeCooldown = CHARGE_COOLDOWN_TIME;
            System.out.println("[KANDULA] Charge activated!");
            return true;
        }
        return false;
    }

    private void stopCharge() {
        isCharging = false;
        chargeTimer = 0;
        speed = Constants.ELEPHANT_SPEED;
        System.out.println("[KANDULA] Charge ended");
    }

    public float getChargeDamage() {
        if (isCharging) {
            return attackDamage * CHARGE_DAMAGE_MULTIPLIER;
        }
        return attackDamage;
    }

    public boolean isChargeReady() {
        return chargeCooldown <= 0;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public float getChargeCooldown() {
        return chargeCooldown;
    }

    public float getChargeProgress() {
        return isCharging ? (chargeTimer / CHARGE_DURATION) : 0;
    }
}
