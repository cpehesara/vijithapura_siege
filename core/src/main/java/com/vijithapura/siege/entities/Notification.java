package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;

// Notification class for UI messages
public class Notification {
    public String message;
    public Color color;
    public float alpha;
    float lifetime;
    private float timer;

    public Notification(String message, Color color) {
        this.message = message;
        this.color = new Color(color);
        this.alpha = 1.0f;
        this.lifetime = 3.0f;
        this.timer = 0;
    }

    public void update(float delta) {
        timer += delta;
        if (timer > lifetime - 1.0f) {
            alpha = lifetime - timer;
        }
    }

    public boolean isExpired() {
        return timer >= lifetime;
    }

    // --- ADDED GETTER METHODS TO FIX ERRORS ---

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }
}
