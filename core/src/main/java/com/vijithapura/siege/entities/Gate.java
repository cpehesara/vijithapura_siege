package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.utils.Constants;
import com.vijithapura.siege.utils.TextureManager;
import java.util.ArrayList;

public class Gate {
    public enum GatePosition {
        NORTH, SOUTH, EAST, WEST
    }

    private GatePosition position;
    private Rectangle bounds;
    private float health;
    private float maxHealth;
    private boolean isDestroyed;
    private Color color;

    // Offensive capabilities
    private float attackRange = 200f;
    private float attackDamage = 15f;
    private float attackCooldown = 1.5f;
    private float attackTimer = 0;
    private Unit currentTarget;

    public Gate(GatePosition position, float x, float y, float width, float height) {
        this.position = position;
        this.bounds = new Rectangle(x, y, width, height);
        this.maxHealth = Constants.GATE_HEALTH * 1.5f;
        this.health = maxHealth;
        this.isDestroyed = false;
        this.color = new Color(0.8f, 0.4f, 0.1f, 1);
    }

    public void update(float delta, ArrayList<Unit> enemies, ArrayList<Projectile> projectiles) {
        if (isDestroyed) return;
        if (attackTimer > 0) attackTimer -= delta;
        else {
            if (currentTarget == null || !currentTarget.isAlive() || !isNearby(currentTarget.getPosition(), attackRange)) {
                currentTarget = findNearestEnemy(enemies);
            }
            if (currentTarget != null) {
                fireProjectile(projectiles);
                attackTimer = attackCooldown;
            }
        }
    }

    private Unit findNearestEnemy(ArrayList<Unit> enemies) {
        Unit nearest = null;
        float minDst = attackRange;
        Vector2 center = getCenter();
        for (Unit enemy : enemies) {
            if (enemy.isAlive()) {
                float dst = center.dst(enemy.getPosition());
                if (dst < minDst) {
                    minDst = dst;
                    nearest = enemy;
                }
            }
        }
        return nearest;
    }

    private void fireProjectile(ArrayList<Projectile> projectiles) {
        if (currentTarget != null) {
            Vector2 center = getCenter();
            projectiles.add(new Projectile(center.x, center.y, currentTarget, attackDamage));
        }
    }

    public void takeDamage(float damage) {
        if (isDestroyed) return;
        health -= damage;
        if (health <= 0) {
            health = 0;
            isDestroyed = true;
        }
    }

    public void render(ShapeRenderer renderer, SpriteBatch batch, TextureManager textureManager) {
        if (isDestroyed) {
            renderer.setColor(0.3f, 0.2f, 0.1f, 1);
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            renderer.setColor(color);
            renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

            // Render Offensive Tower using Sprite
            if (textureManager.hasTexture("tower")) {
                renderer.end();
                batch.begin();
                Sprite tower = textureManager.getSprite("tower");
                float size = 40;
                tower.setSize(size, size);
                tower.setPosition(bounds.x + bounds.width/2 - size/2, bounds.y + bounds.height/2 - size/2);
                tower.draw(batch);
                batch.end();
                renderer.begin(ShapeRenderer.ShapeType.Filled);
            } else {
                // Fallback Circle
                renderer.setColor(Color.DARK_GRAY);
                renderer.circle(bounds.x + bounds.width/2, bounds.y + bounds.height/2, 10);
            }

            // Health Bar
            float barWidth = bounds.width;
            float barHeight = 5;
            float barX = bounds.x;
            float barY = bounds.y + bounds.height + 5;
            renderer.setColor(Color.RED);
            renderer.rect(barX, barY, barWidth, barHeight);
            renderer.setColor(Color.GREEN);
            renderer.rect(barX, barY, barWidth * (health / maxHealth), barHeight);
        }
    }

    public boolean contains(float x, float y) { return bounds.contains(x, y); }
    public boolean isNearby(Vector2 unitPos, float range) { return getCenter().dst(unitPos) <= range; }
    public Vector2 getCenter() { return new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2); }
    public boolean isDestroyed() { return isDestroyed; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public GatePosition getPosition() { return position; }

    // === ADDED MISSING METHOD ===
    public Rectangle getBounds() {
        return bounds;
    }
}
