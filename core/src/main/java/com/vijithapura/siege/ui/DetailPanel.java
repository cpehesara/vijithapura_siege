package com.vijithapura.siege.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Building;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.ResourceNode;
import com.vijithapura.siege.entities.Unit;

/**
 * Detailed information panel that appears when clicking on any selectable entity
 */
public class DetailPanel {
    private Rectangle bounds;
    private boolean visible;
    private Object selectedEntity;
    private Vector2 clickPosition;
    
    private static final Color BG_COLOR = new Color(0.1f, 0.08f, 0.06f, 0.95f);
    private static final Color BORDER_COLOR = new Color(0.95f, 0.85f, 0.35f, 1f);
    private static final Color TITLE_COLOR = new Color(0.95f, 0.92f, 0.85f, 1f);
    private static final Color TEXT_COLOR = new Color(0.85f, 0.82f, 0.75f, 1f);
    private static final Color STAT_GOOD = new Color(0.25f, 0.65f, 0.25f, 1f);
    private static final Color STAT_BAD = new Color(0.75f, 0.20f, 0.15f, 1f);
    
    public DetailPanel() {
        bounds = new Rectangle(0, 0, 300, 400);
        visible = false;
        selectedEntity = null;
        clickPosition = new Vector2();
    }
    
    public void show(Object entity, float screenX, float screenY) {
        this.selectedEntity = entity;
        this.visible = true;
        
        // Position panel near click but keep it on screen
        float x = Math.min(screenX + 20, Gdx.graphics.getWidth() - bounds.width - 10);
        float y = Math.min(screenY - 20, Gdx.graphics.getHeight() - bounds.height - 10);
        x = Math.max(10, x);
        y = Math.max(10, y);
        
        bounds.setPosition(x, y);
    }
    
    public void hide() {
        this.visible = false;
        this.selectedEntity = null;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void render(ShapeRenderer sr, SpriteBatch batch, BitmapFont font) {
        if (!visible || selectedEntity == null) return;
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        // Background
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(BG_COLOR);
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        sr.end();
        
        // Border
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        sr.setColor(BORDER_COLOR);
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Inner accent border
        sr.setColor(new Color(0.65f, 0.50f, 0.15f, 1f));
        Gdx.gl.glLineWidth(1);
        sr.rect(bounds.x + 5, bounds.y + 5, bounds.width - 10, bounds.height - 10);
        sr.end();
        
        // Content
        batch.begin();
        float textX = bounds.x + 15;
        float textY = bounds.y + bounds.height - 25;
        
        if (selectedEntity instanceof Unit) {
            renderUnitDetails((Unit) selectedEntity, batch, font, textX, textY);
        } else if (selectedEntity instanceof Building) {
            renderBuildingDetails((Building) selectedEntity, batch, font, textX, textY);
        } else if (selectedEntity instanceof ResourceNode) {
            renderResourceDetails((ResourceNode) selectedEntity, batch, font, textX, textY);
        } else if (selectedEntity instanceof Gate) {
            renderGateDetails((Gate) selectedEntity, batch, font, textX, textY);
        }
        
        batch.end();
    }
    
    private void renderUnitDetails(Unit unit, SpriteBatch batch, BitmapFont font, float x, float y) {
        font.setColor(TITLE_COLOR);
        font.draw(batch, "=== UNIT DETAILS ===", x, y);
        y -= 30;
        
        font.setColor(TEXT_COLOR);
        font.draw(batch, "Name: " + unit.getName(), x, y);
        y -= 25;
        
        font.draw(batch, "Type: " + unit.getType(), x, y);
        y -= 25;
        
        // Health bar
        font.setColor(STAT_GOOD);
        float healthPercent = unit.getHealth() / unit.getMaxHealth();
        String healthText = String.format("Health: %.0f / %.0f (%.0f%%)", 
            unit.getHealth(), unit.getMaxHealth(), healthPercent * 100);
        font.draw(batch, healthText, x, y);
        y -= 25;
        
        font.setColor(TEXT_COLOR);
        font.draw(batch, "Speed: " + String.format("%.1f", unit.getSpeed()), x, y);
        y -= 25;
        
        font.draw(batch, "Damage: " + String.format("%.1f", unit.getAttackDamage()), x, y);
        y -= 25;
        
        font.draw(batch, "Attack Range: " + String.format("%.0f", unit.getAttackRange()), x, y);
        y -= 25;
        
        font.draw(batch, "Armor: " + unit.getArmor(), x, y);
        y -= 25;
        
        font.draw(batch, "Team: " + (unit.getTeamId() == 0 ? "Player" : "Enemy"), x, y);
        y -= 30;
        
        // Status
        font.setColor(new Color(0.7f, 0.7f, 0.9f, 1f));
        if (unit.isAttacking()) {
            font.draw(batch, "Status: ATTACKING", x, y);
        } else if (unit.isMoving()) {
            font.draw(batch, "Status: MOVING", x, y);
        } else if (unit.isGathering()) {
            font.draw(batch, "Status: GATHERING", x, y);
        } else {
            font.draw(batch, "Status: IDLE", x, y);
        }
    }
    
    private void renderBuildingDetails(Building building, SpriteBatch batch, BitmapFont font, float x, float y) {
        font.setColor(TITLE_COLOR);
        font.draw(batch, "=== BUILDING ===", x, y);
        y -= 30;
        
        font.setColor(TEXT_COLOR);
        font.draw(batch, "Type: " + building.getType(), x, y);
        y -= 25;
        
        font.setColor(STAT_GOOD);
        float healthPercent = building.getHealth() / building.getMaxHealth();
        String healthText = String.format("Health: %.0f / %.0f (%.0f%%)", 
            building.getHealth(), building.getMaxHealth(), healthPercent * 100);
        font.draw(batch, healthText, x, y);
        y -= 25;
        
        font.setColor(TEXT_COLOR);
        Rectangle b = building.getBounds();
        font.draw(batch, "Size: " + (int)b.width + " x " + (int)b.height, x, y);
        y -= 30;
        
        if (building.isUnderConstruction()) {
            font.setColor(new Color(0.9f, 0.7f, 0.2f, 1f));
            float progress = building.getConstructionProgress() * 100;
            font.draw(batch, "Construction: " + String.format("%.0f%%", progress), x, y);
            y -= 25;
        } else {
            font.setColor(STAT_GOOD);
            font.draw(batch, "Status: OPERATIONAL", x, y);
            y -= 25;
        }
        
        font.setColor(TEXT_COLOR);
        Vector2 pos = building.getCenter();
        font.draw(batch, "Position: " + (int)pos.x + ", " + (int)pos.y, x, y);
    }
    
    private void renderResourceDetails(ResourceNode node, SpriteBatch batch, BitmapFont font, float x, float y) {
        font.setColor(TITLE_COLOR);
        font.draw(batch, "=== RESOURCE ===", x, y);
        y -= 30;
        
        font.setColor(TEXT_COLOR);
        String resourceType = node.getType().toString();
        font.draw(batch, "Type: " + resourceType, x, y);
        y -= 25;
        
        // Resource amount with color coding
        int remaining = node.getResourceAmount();
        int max = node.getMaxAmount();
        float percent = (float) remaining / max;
        
        if (percent > 0.6f) {
            font.setColor(STAT_GOOD);
        } else if (percent > 0.3f) {
            font.setColor(new Color(0.85f, 0.70f, 0.20f, 1f));
        } else {
            font.setColor(STAT_BAD);
        }
        
        String amountText = String.format("Amount: %d / %d (%.0f%%)", remaining, max, percent * 100);
        font.draw(batch, amountText, x, y);
        y -= 30;
        
        if (node.isDepleted()) {
            font.setColor(STAT_BAD);
            font.draw(batch, "Status: DEPLETED", x, y);
        } else {
            font.setColor(STAT_GOOD);
            font.draw(batch, "Status: AVAILABLE", x, y);
        }
        y -= 25;
        
        font.setColor(TEXT_COLOR);
        Vector2 pos = node.getPosition();
        font.draw(batch, "Position: " + (int)pos.x + ", " + (int)pos.y, x, y);
        y -= 30;
        
        // Gathering info
        font.setColor(new Color(0.7f, 0.9f, 0.7f, 1f));
        font.draw(batch, "Right-click with workers", x, y);
        y -= 20;
        font.draw(batch, "to gather resources", x, y);
    }
    
    private void renderGateDetails(Gate gate, SpriteBatch batch, BitmapFont font, float x, float y) {
        font.setColor(TITLE_COLOR);
        font.draw(batch, "=== GATE ===", x, y);
        y -= 30;
        
        font.setColor(TEXT_COLOR);
        font.draw(batch, "Position: " + gate.getPosition(), x, y);
        y -= 25;
        
        font.setColor(STAT_BAD);
        float healthPercent = gate.getHealth() / gate.getMaxHealth();
        String healthText = String.format("Health: %.0f / %.0f (%.0f%%)", 
            gate.getHealth(), gate.getMaxHealth(), healthPercent * 100);
        font.draw(batch, healthText, x, y);
        y -= 30;
        
        if (gate.isDestroyed()) {
            font.setColor(STAT_BAD);
            font.draw(batch, "Status: DESTROYED", x, y);
        } else {
            font.setColor(new Color(0.9f, 0.7f, 0.2f, 1f));
            font.draw(batch, "Status: STANDING", x, y);
        }
        y -= 30;
        
        font.setColor(TEXT_COLOR);
        font.draw(batch, "Objective: Destroy all gates", x, y);
        y -= 20;
        font.draw(batch, "to win the game!", x, y);
    }
    
    public boolean handleClick(float screenX, float screenY) {
        if (visible && bounds.contains(screenX, screenY)) {
            return true; // Click consumed by panel
        }
        return false;
    }
}
