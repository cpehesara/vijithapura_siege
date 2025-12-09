package com.vijithapura.siege.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.utils.TextureManager;

/**
 * Resource nodes - Now renders Trees and Stones!
 */
public class ResourceNode {
    public enum ResourceType {
        WOOD,
        STONE,
        GOLD
    }

    private ResourceType type;
    private Vector2 position;
    private int resourceAmount;
    private int maxAmount;
    private boolean depleted;
    private Color color;
    private float size;
    private String spriteKey;

    public ResourceNode(ResourceType type, float x, float y, int amount) {
        this.type = type;
        this.position = new Vector2(x, y);
        this.resourceAmount = amount;
        this.maxAmount = amount;
        this.depleted = false;
        this.size = 30;

        if (type == ResourceType.WOOD) {
            this.color = new Color(0.3f, 0.6f, 0.2f, 1);
            this.spriteKey = "tree";
        } else if (type == ResourceType.STONE) {
            this.color = new Color(0.5f, 0.5f, 0.5f, 1);
            this.spriteKey = "stone";
        } else {
            this.color = new Color(1.0f, 0.85f, 0.0f, 1);
            this.spriteKey = "wood_pile";
        }
    }

    public int gather(int amount) {
        if (depleted) return 0;
        int gathered = Math.min(amount, resourceAmount);
        resourceAmount -= gathered;
        if (resourceAmount <= 0) {
            resourceAmount = 0;
            depleted = true;
        }
        return gathered;
    }

    public void render(ShapeRenderer renderer, SpriteBatch batch, TextureManager textureManager) {
        boolean hasSprite = textureManager.hasTexture(spriteKey);

        if (hasSprite) {
            renderer.end();
            batch.begin();

            Sprite sprite = textureManager.getSprite(spriteKey);
            if (sprite != null) {
                // Center the sprite
                float renderSize = size * 1.5f;
                sprite.setPosition(position.x - renderSize/2, position.y - renderSize/2);
                sprite.setSize(renderSize, renderSize);

                if (depleted) {
                    sprite.setColor(0.4f, 0.4f, 0.4f, 0.5f); // Greyed out
                } else {
                    sprite.setColor(Color.WHITE);
                }
                sprite.draw(batch);
            }

            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        } else {
            // Fallback Shape
            if (depleted) {
                renderer.setColor(color.r * 0.3f, color.g * 0.3f, color.b * 0.3f, 0.5f);
            } else {
                renderer.setColor(color);
            }
            renderer.circle(position.x, position.y, size);
        }
    }

    public boolean isNearby(Vector2 workerPos, float range) {
        return Vector2.dst(position.x, position.y, workerPos.x, workerPos.y) <= range + size;
    }

    public ResourceType getType() { return type; }
    public Vector2 getPosition() { return position; }
    public boolean isDepleted() { return depleted; }
    public int getResourceAmount() { return resourceAmount; }
    public int getMaxAmount() { return maxAmount; }
}
