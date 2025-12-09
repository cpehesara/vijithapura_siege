package com.vijithapura.siege.entities;

public class Projectile {
    private float x, y;
    private float targetX, targetY;
    private float speed;
    private float damage;
    private boolean active;
    private Unit target;

    public Projectile(float x, float y, Unit target, float damage) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.targetX = target.getPosition().x;
        this.targetY = target.getPosition().y;
        this.damage = damage;
        this.speed = 400f;
        this.active = true;
    }

    public void update(float delta) {
        if (!active) return;

        if (target != null && target.isAlive()) {
            targetX = target.getPosition().x;
            targetY = target.getPosition().y;
        }

        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float)Math.sqrt(dx*dx + dy*dy);

        if (distance < 5) {
            if (target != null && target.isAlive()) {
                target.takeDamage(damage);
            }
            active = false;
            return;
        }

        float moveDistance = speed * delta;
        if (moveDistance > distance) moveDistance = distance;

        x += (dx / distance) * moveDistance;
        y += (dy / distance) * moveDistance;
    }

    public boolean isActive() {
        return active;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
