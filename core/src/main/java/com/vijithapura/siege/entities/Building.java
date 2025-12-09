package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.utils.TextureManager;

/**
 * Building base class - Now renders with Sprites!
 */
public class Building {
    public enum BuildingType {
        BARRACKS,       // Train soldiers
        WORKSHOP,       // Train champions
        COMMAND_CENTER, // Main base
        RESOURCE_DEPOT  // Drop off resources
    }

    protected BuildingType type;
    protected Rectangle bounds;
    protected float health;
    protected float maxHealth;
    protected boolean isConstructed;
    protected float constructionProgress;
    protected Color color;
    protected String name;
    protected String spriteKey;

    public Building(BuildingType type, float x, float y, float width, float height) {
        this.type = type;
        this.bounds = new Rectangle(x, y, width, height);
        this.maxHealth = 500;
        this.health = maxHealth;
        this.isConstructed = true;
        this.constructionProgress = 1.0f;

        // Set color, name, and Sprite Key
        switch (type) {
            case BARRACKS:
                color = new Color(0.6f, 0.3f, 0.1f, 1);
                name = "Barracks";
                spriteKey = "barracks";
                break;
            case WORKSHOP:
                color = new Color(0.4f, 0.4f, 0.6f, 1);
                name = "Workshop";
                spriteKey = "workshop"; // uses command_center if missing usually, or specific
                break;
            case COMMAND_CENTER:
                color = new Color(0.7f, 0.6f, 0.2f, 1);
                name = "Command Center";
                maxHealth = 1000;
                health = maxHealth;
                spriteKey = "command_center";
                break;
            case RESOURCE_DEPOT:
                color = new Color(0.5f, 0.5f, 0.3f, 1);
                name = "Resource Depot";
                spriteKey = "resource_depot";
                break;
        }
    }

    public void update(float delta) {
        if (!isConstructed) {
            constructionProgress += delta * 0.2f;
            if (constructionProgress >= 1.0f) {
                constructionProgress = 1.0f;
                isConstructed = true;
            }
        }
    }

    // New Render method utilizing SpriteBatch
    public void render(ShapeRenderer renderer, SpriteBatch batch, TextureManager textureManager) {
        boolean hasSprite = textureManager.hasTexture(spriteKey);

        if (hasSprite && isConstructed) {
            // Draw Sprite
            renderer.end(); // Stop shape renderer
            batch.begin();

            Sprite sprite = textureManager.getSprite(spriteKey);
            if (sprite != null) {
                sprite.setPosition(bounds.x, bounds.y);
                sprite.setSize(bounds.width, bounds.height);
                sprite.setColor(Color.WHITE);
                sprite.draw(batch);
            }

            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled); // Restart shape renderer
        } else {
            // Fallback to Shapes (or for construction)
            if (!isConstructed) {
                renderer.setColor(color.r * 0.5f, color.g * 0.5f, color.b * 0.5f, 0.5f);
                renderer.rect(bounds.x, bounds.y, bounds.width * constructionProgress, bounds.height);
                // Outline
                renderer.setColor(Color.WHITE);
                float thickness = 2;
                renderer.rect(bounds.x, bounds.y, bounds.width, thickness);
                renderer.rect(bounds.x, bounds.y + bounds.height - thickness, bounds.width, thickness);
                renderer.rect(bounds.x, bounds.y, thickness, bounds.height);
                renderer.rect(bounds.x + bounds.width - thickness, bounds.y, thickness, bounds.height);
            } else {
                renderer.setColor(color);
                renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }

        // Always draw selection border if needed, or health bar logic (omitted for brevity)
    }

    public boolean contains(float x, float y) { return bounds.contains(x, y); }
    public Vector2 getSpawnPosition() { return new Vector2(bounds.x + bounds.width / 2, bounds.y - 20); }
    public Vector2 getCenter() { return new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2); }
    public BuildingType getType() { return type; }
    public boolean isConstructed() { return isConstructed; }
    public String getName() { return name; }
    public Rectangle getBounds() { return bounds; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public boolean isUnderConstruction() { return !isConstructed; }
    public float getConstructionProgress() { return constructionProgress; }
}
