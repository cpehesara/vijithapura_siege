package com.vijithapura.siege.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.vijithapura.siege.entities.*;
import com.vijithapura.siege.utils.Constants;

import java.util.ArrayList;

/**
 * Age of Empires 3 Inspired Minimap
 * Features: Aged parchment map, detailed terrain, clear unit indicators
 */
public class MinimapRenderer {
    private Rectangle bounds;
    private float scaleX;
    private float scaleY;

    // AoE3 Color Palette
    private static final Color MAP_BG = new Color(0.82f, 0.77f, 0.62f, 1f);
    private static final Color MAP_BORDER_OUTER = new Color(0.25f, 0.18f, 0.10f, 1f);
    private static final Color MAP_BORDER_INNER = new Color(0.65f, 0.50f, 0.15f, 1f);

    private static final Color TERRAIN_DARK = new Color(0.70f, 0.65f, 0.50f, 1f);
    private static final Color TERRAIN_LIGHT = new Color(0.85f, 0.80f, 0.65f, 1f);

    private static final Color FORTRESS_STONE = new Color(0.35f, 0.35f, 0.40f, 1f);
    private static final Color FORTRESS_DARK = new Color(0.20f, 0.20f, 0.25f, 1f);

    private static final Color GATE_INTACT = new Color(0.45f, 0.35f, 0.25f, 1f);
    private static final Color GATE_DAMAGED = new Color(0.75f, 0.45f, 0.20f, 1f);
    private static final Color GATE_DESTROYED = new Color(0.50f, 0.20f, 0.15f, 1f);

    private static final Color FOREST_GREEN = new Color(0.35f, 0.55f, 0.35f, 1f);
    private static final Color STONE_GRAY = new Color(0.55f, 0.55f, 0.60f, 1f);

    private static final Color BUILDING_FRIENDLY = new Color(0.40f, 0.50f, 0.65f, 1f);

    // Unit colors with AoE3 style
    private static final Color PLAYER_INFANTRY = new Color(0.35f, 0.55f, 0.80f, 1f);
    private static final Color PLAYER_CAVALRY = new Color(0.45f, 0.65f, 0.90f, 1f);
    private static final Color PLAYER_SIEGE = new Color(0.30f, 0.45f, 0.65f, 1f);
    private static final Color PLAYER_WORKER = new Color(0.55f, 0.70f, 0.85f, 1f);

    private static final Color ENEMY_UNIT = new Color(0.75f, 0.25f, 0.20f, 1f);
    private static final Color ENEMY_ELITE = new Color(0.90f, 0.35f, 0.25f, 1f);

    private static final Color CHAMPION_GOLD = new Color(0.90f, 0.75f, 0.30f, 1f);

    private static final Color CAMERA_FRAME = new Color(0.95f, 0.85f, 0.35f, 0.9f);

    private float animTimer;
    private float mapRotation; // For aesthetic touch

    public MinimapRenderer(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
        scaleX = bounds.width / Constants.MAP_WIDTH;
        scaleY = bounds.height / Constants.MAP_HEIGHT;
        animTimer = 0;
        mapRotation = 0;
    }

    public void update(float delta) {
        animTimer += delta;
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, OrthographicCamera camera,
                       ArrayList<Unit> playerUnits, ArrayList<Unit> enemyUnits,
                       ArrayList<Gate> gates, ArrayList<Building> buildings,
                       ArrayList<ResourceNode> resourceNodes) {

        sr.setProjectionMatrix(batch.getProjectionMatrix());

        // Parchment map background
        renderParchmentBackground(sr);

        // Terrain features
        renderTerrain(sr);

        // Strategic features
        renderResourceNodes(sr, resourceNodes);
        renderBuildings(sr, buildings);
        renderFortress(sr);
        renderGates(sr, gates);

        // Military units
        renderUnits(sr, playerUnits, true);
        renderUnits(sr, enemyUnits, false);

        // Camera viewport
        renderCameraViewport(sr, camera);

        // Ornate border
        renderOrnateBorder(sr);

        // Battle indicators
        renderCombatFlashes(sr, playerUnits, enemyUnits);
    }

    private void renderParchmentBackground(ShapeRenderer sr) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Main parchment color
        sr.setColor(MAP_BG);
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Paper texture effect (subtle lines)
        sr.setColor(0.78f, 0.73f, 0.58f, 0.3f);
        for (int i = 0; i < bounds.height; i += 6) {
            float variation = (float)Math.sin(i * 0.1f) * 0.5f + 0.5f;
            sr.setColor(0.78f, 0.73f, 0.58f, 0.15f * variation);
            sr.rectLine(bounds.x, bounds.y + i, bounds.x + bounds.width, bounds.y + i, 1);
        }

        // Aged corners (darker edges)
        sr.setColor(0.60f, 0.55f, 0.45f, 0.3f);
        float vignette = 15;
        // Top-left
        sr.triangle(bounds.x, bounds.y + bounds.height,
            bounds.x + vignette, bounds.y + bounds.height,
            bounds.x, bounds.y + bounds.height - vignette);
        // Top-right
        sr.triangle(bounds.x + bounds.width, bounds.y + bounds.height,
            bounds.x + bounds.width - vignette, bounds.y + bounds.height,
            bounds.x + bounds.width, bounds.y + bounds.height - vignette);
        // Bottom-left
        sr.triangle(bounds.x, bounds.y,
            bounds.x + vignette, bounds.y,
            bounds.x, bounds.y + vignette);
        // Bottom-right
        sr.triangle(bounds.x + bounds.width, bounds.y,
            bounds.x + bounds.width - vignette, bounds.y,
            bounds.x + bounds.width, bounds.y + vignette);

        sr.end();
    }

    private void renderTerrain(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Decorative terrain patches (forests, hills)
        sr.setColor(TERRAIN_DARK);
        drawMapCircle(sr, 200, 300, 70);
        drawMapCircle(sr, 850, 750, 90);
        drawMapCircle(sr, 1100, 400, 60);

        sr.setColor(TERRAIN_LIGHT);
        drawMapCircle(sr, 600, 600, 50);
        drawMapCircle(sr, 1300, 900, 65);

        sr.end();
    }

    private void renderResourceNodes(ShapeRenderer sr, ArrayList<ResourceNode> nodes) {
        if (nodes == null) return;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (ResourceNode node : nodes) {
            if (node == null) continue;

            Color resourceColor = node.getType() == ResourceNode.ResourceType.WOOD ?
                FOREST_GREEN : STONE_GRAY;

            float x = node.getPosition().x * scaleX + bounds.x;
            float y = node.getPosition().y * scaleY + bounds.y;
            float size = node.isDepleted() ? 2.5f : 4f;

            if (node.isDepleted()) {
                sr.setColor(resourceColor.r * 0.5f, resourceColor.g * 0.5f,
                    resourceColor.b * 0.5f, 0.5f);
            } else {
                // Cluster effect for resources
                sr.setColor(resourceColor.r * 0.7f, resourceColor.g * 0.7f,
                    resourceColor.b * 0.7f, 0.6f);
                sr.circle(x, y, size + 2);

                sr.setColor(resourceColor);
                sr.circle(x, y, size);
            }
        }

        sr.end();
    }

    private void renderBuildings(ShapeRenderer sr, ArrayList<Building> buildings) {
        if (buildings == null) return;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (Building b : buildings) {
            if (b == null) continue;

            Rectangle bBounds = b.getBounds();
            float x = bBounds.x * scaleX + bounds.x;
            float y = bBounds.y * scaleY + bounds.y;
            float w = bBounds.width * scaleX;
            float h = bBounds.height * scaleY;

            // Shadow
            sr.setColor(0, 0, 0, 0.3f);
            sr.rect(x + 1, y - 1, w, h);

            // Building body
            sr.setColor(BUILDING_FRIENDLY);
            sr.rect(x, y, w, h);

            // Roof/detail
            sr.setColor(BUILDING_FRIENDLY.r * 0.8f, BUILDING_FRIENDLY.g * 0.8f,
                BUILDING_FRIENDLY.b * 0.8f, 1f);
            sr.rect(x + 1, y + h - 2, w - 2, 2);
        }

        sr.end();

        // Building borders
        sr.begin(ShapeRenderer.ShapeType.Line);
        for (Building b : buildings) {
            if (b == null) continue;
            Rectangle bBounds = b.getBounds();
            float x = bBounds.x * scaleX + bounds.x;
            float y = bBounds.y * scaleY + bounds.y;
            float w = bBounds.width * scaleX;
            float h = bBounds.height * scaleY;

            sr.setColor(MAP_BORDER_OUTER);
            sr.rect(x, y, w, h);
        }
        sr.end();
    }

    private void renderFortress(ShapeRenderer sr) {
        float fortressX = Constants.FORTRESS_X * scaleX + bounds.x;
        float fortressY = Constants.FORTRESS_Y * scaleY + bounds.y;
        float fortressSize = Constants.FORTRESS_SIZE * scaleX;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Fortress shadow
        sr.setColor(0, 0, 0, 0.4f);
        sr.rect(fortressX - fortressSize/2 + 2, fortressY - fortressSize/2 - 2,
            fortressSize, fortressSize);

        // Outer walls
        sr.setColor(FORTRESS_STONE);
        sr.rect(fortressX - fortressSize/2, fortressY - fortressSize/2,
            fortressSize, fortressSize);

        // Inner fortress
        float innerSize = fortressSize * 0.75f;
        sr.setColor(FORTRESS_DARK);
        sr.rect(fortressX - innerSize/2, fortressY - innerSize/2,
            innerSize, innerSize);

        // Keep (center structure)
        float keepSize = fortressSize * 0.4f;
        sr.setColor(FORTRESS_STONE.r * 0.9f, FORTRESS_STONE.g * 0.9f,
            FORTRESS_STONE.b * 0.9f, 1f);
        sr.rect(fortressX - keepSize/2, fortressY - keepSize/2,
            keepSize, keepSize);

        sr.end();

        // Fortress borders
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        sr.setColor(MAP_BORDER_OUTER);
        sr.rect(fortressX - fortressSize/2, fortressY - fortressSize/2,
            fortressSize, fortressSize);
        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void renderGates(ShapeRenderer sr, ArrayList<Gate> gates) {
        if (gates == null) return;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (Gate gate : gates) {
            if (gate == null) continue;

            Rectangle gBounds = gate.getBounds();
            float x = gBounds.x * scaleX + bounds.x;
            float y = gBounds.y * scaleY + bounds.y;
            float w = gBounds.width * scaleX;
            float h = gBounds.height * scaleY;

            // Gate status color
            Color gateColor;
            if (gate.isDestroyed()) {
                gateColor = GATE_DESTROYED;
            } else {
                float healthPercent = gate.getHealth() / gate.getMaxHealth();
                if (healthPercent > 0.6f) {
                    gateColor = GATE_INTACT;
                } else {
                    gateColor = GATE_DAMAGED;
                    // Pulsing for damaged gates
                    float pulse = (float)Math.sin(animTimer * 5) * 0.15f + 0.85f;
                    gateColor = new Color(gateColor.r * pulse, gateColor.g * pulse,
                        gateColor.b * pulse, 1f);
                }
            }

            // Gate shadow
            sr.setColor(0, 0, 0, 0.4f);
            sr.rect(x + 1, y - 1, w, h);

            // Gate body
            sr.setColor(gateColor);
            sr.rect(x, y, w, h);

            // Gate detail (reinforcement)
            if (!gate.isDestroyed()) {
                sr.setColor(gateColor.r * 0.8f, gateColor.g * 0.8f,
                    gateColor.b * 0.8f, 1f);
                if (w > h) { // Horizontal gate
                    sr.rect(x + w * 0.25f, y, w * 0.5f, h);
                } else { // Vertical gate
                    sr.rect(x, y + h * 0.25f, w, h * 0.5f);
                }
            }
        }

        sr.end();

        // Gate borders
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(1.5f);
        for (Gate gate : gates) {
            if (gate == null) continue;
            Rectangle gBounds = gate.getBounds();
            float x = gBounds.x * scaleX + bounds.x;
            float y = gBounds.y * scaleY + bounds.y;
            float w = gBounds.width * scaleX;
            float h = gBounds.height * scaleY;

            sr.setColor(MAP_BORDER_OUTER);
            sr.rect(x, y, w, h);
        }
        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void renderUnits(ShapeRenderer sr, ArrayList<Unit> units, boolean isPlayer) {
        if (units == null) return;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (Unit unit : units) {
            if (unit == null || !unit.isAlive()) continue;

            float x = unit.getPosition().x * scaleX + bounds.x;
            float y = unit.getPosition().y * scaleY + bounds.y;

            Color unitColor;
            float size = 2.5f;

            if (isPlayer) {
                // Player units - different colors by type
                if (unit instanceof Worker) {
                    unitColor = PLAYER_WORKER;
                    size = 2f;
                } else if (unit instanceof Cavalry) {
                    unitColor = PLAYER_CAVALRY;
                    size = 3f;
                } else if (unit instanceof SiegeRam) {
                    unitColor = PLAYER_SIEGE;
                    size = 4f;
                } else if (unit instanceof Champion) {
                    unitColor = CHAMPION_GOLD;
                    size = 3.5f;
                    // Pulsing champion
                    float pulse = (float)Math.sin(animTimer * 3) * 0.2f + 0.8f;
                    unitColor = new Color(unitColor.r * pulse, unitColor.g * pulse,
                        unitColor.b * pulse, 1f);
                } else {
                    unitColor = PLAYER_INFANTRY;
                }
            } else {
                // Enemy units
                if (unit instanceof Champion) {
                    unitColor = CHAMPION_GOLD;
                    size = 3.5f;
                } else if (unit instanceof SiegeRam) {
                    unitColor = ENEMY_ELITE;
                    size = 4f;
                } else {
                    unitColor = ENEMY_UNIT;
                    size = 2.5f;
                }
            }

            // Unit dot with border for clarity
            sr.setColor(0, 0, 0, 0.6f);
            sr.circle(x, y, size + 0.5f);

            sr.setColor(unitColor);
            sr.circle(x, y, size);

            // Highlight for selected/important units
            if (isPlayer && (unit.isSelected() || unit instanceof Champion)) {
                sr.setColor(unitColor.r, unitColor.g, unitColor.b, 0.4f);
                sr.circle(x, y, size + 1.5f);
            }
        }

        sr.end();
    }

    private void renderCameraViewport(ShapeRenderer sr, OrthographicCamera camera) {
        sr.begin(ShapeRenderer.ShapeType.Line);

        float camWidth = (camera.viewportWidth * camera.zoom) * scaleX;
        float camHeight = (camera.viewportHeight * camera.zoom) * scaleY;
        float camX = camera.position.x * scaleX + bounds.x - camWidth/2;
        float camY = camera.position.y * scaleY + bounds.y - camHeight/2;

        // Animated camera frame
        float pulse = (float)Math.sin(animTimer * 2) * 0.1f + 0.9f;

        Gdx.gl.glLineWidth(2.5f);
        sr.setColor(CAMERA_FRAME.r * pulse, CAMERA_FRAME.g * pulse,
            CAMERA_FRAME.b * pulse, CAMERA_FRAME.a);
        sr.rect(camX, camY, camWidth, camHeight);

        // Inner frame for emphasis
        Gdx.gl.glLineWidth(1);
        sr.setColor(CAMERA_FRAME.r * pulse, CAMERA_FRAME.g * pulse,
            CAMERA_FRAME.b * pulse, 0.5f);
        sr.rect(camX + 2, camY + 2, camWidth - 4, camHeight - 4);

        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void renderOrnateBorder(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Line);

        // Outer wood frame
        Gdx.gl.glLineWidth(4);
        sr.setColor(MAP_BORDER_OUTER);
        sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Middle brass/bronze trim
        Gdx.gl.glLineWidth(2);
        sr.setColor(MAP_BORDER_INNER);
        sr.rect(bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 6);

        // Inner fine detail
        Gdx.gl.glLineWidth(1);
        sr.setColor(MAP_BORDER_INNER.r * 1.2f, MAP_BORDER_INNER.g * 1.2f,
            MAP_BORDER_INNER.b * 1.2f, 0.6f);
        sr.rect(bounds.x + 5, bounds.y + 5, bounds.width - 10, bounds.height - 10);

        // Corner ornaments
        float cornerSize = 12;
        Gdx.gl.glLineWidth(2);
        sr.setColor(MAP_BORDER_INNER);

        // Top-left
        sr.line(bounds.x, bounds.y + bounds.height,
            bounds.x + cornerSize, bounds.y + bounds.height);
        sr.line(bounds.x, bounds.y + bounds.height,
            bounds.x, bounds.y + bounds.height - cornerSize);

        // Top-right
        sr.line(bounds.x + bounds.width, bounds.y + bounds.height,
            bounds.x + bounds.width - cornerSize, bounds.y + bounds.height);
        sr.line(bounds.x + bounds.width, bounds.y + bounds.height,
            bounds.x + bounds.width, bounds.y + bounds.height - cornerSize);

        // Bottom-left
        sr.line(bounds.x, bounds.y, bounds.x + cornerSize, bounds.y);
        sr.line(bounds.x, bounds.y, bounds.x, bounds.y + cornerSize);

        // Bottom-right
        sr.line(bounds.x + bounds.width, bounds.y,
            bounds.x + bounds.width - cornerSize, bounds.y);
        sr.line(bounds.x + bounds.width, bounds.y,
            bounds.x + bounds.width, bounds.y + cornerSize);

        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void renderCombatFlashes(ShapeRenderer sr, ArrayList<Unit> playerUnits,
                                     ArrayList<Unit> enemyUnits) {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Show active combat areas
        for (Unit player : playerUnits) {
            if (player == null || !player.isAlive()) continue;
            if (player.getCurrentTarget() == null) continue;

            float x = player.getPosition().x * scaleX + bounds.x;
            float y = player.getPosition().y * scaleY + bounds.y;

            // Combat flash effect
            float pulse = (float)Math.abs(Math.sin(animTimer * 6));
            sr.setColor(1f, 0.9f, 0.3f, pulse * 0.5f);
            sr.circle(x, y, 5);
        }

        sr.end();
    }

    private void drawMapCircle(ShapeRenderer sr, float worldX, float worldY,
                               float worldRadius) {
        float x = worldX * scaleX + bounds.x;
        float y = worldY * scaleY + bounds.y;
        float radius = worldRadius * scaleX;
        sr.circle(x, y, radius);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);
        scaleX = bounds.width / Constants.MAP_WIDTH;
        scaleY = bounds.height / Constants.MAP_HEIGHT;
    }
}
