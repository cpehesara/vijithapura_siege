package com.vijithapura.siege.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vijithapura.siege.entities.*;
import com.vijithapura.siege.utils.Constants;
import java.util.ArrayList;

/**
 * Professional Age of Empires 3 inspired UI rendering
 * Separated from GameScreen for better code organization
 */
public class UIRenderer {
    private BitmapFont font;
    private GlyphLayout layout;

    // AoE3 Color Palette - Richer, More Atmospheric
    private static final Color WOOD_DARK = new Color(0.12f, 0.08f, 0.04f, 0.98f);
    private static final Color WOOD_MEDIUM = new Color(0.22f, 0.15f, 0.08f, 1f);
    private static final Color LEATHER_BROWN = new Color(0.35f, 0.25f, 0.15f, 1f);

    private static final Color GOLD_BRIGHT = new Color(1.0f, 0.90f, 0.40f, 1f);
    private static final Color GOLD_DARK = new Color(0.70f, 0.55f, 0.20f, 1f);
    private static final Color BRONZE = new Color(0.60f, 0.40f, 0.25f, 1f);

    private static final Color PARCHMENT = new Color(0.95f, 0.90f, 0.80f, 1f);
    private static final Color TEXT_SHADOW = new Color(0.1f, 0.08f, 0.05f, 0.8f);

    private static final int TOP_BAR_HEIGHT = 50;
    private static final int BOTTOM_PANEL_HEIGHT = 200;

    private float animTimer = 0;

    public UIRenderer(BitmapFont font) {
        this.font = font;
        this.layout = new GlyphLayout();
    }

    public void update(float delta) {
        animTimer += delta;
    }

    public void renderGameUI(SpriteBatch batch, ShapeRenderer sr, GameUIData data) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        // Draw panels
        renderTopResourceBar(sr, data);
        renderBottomCommandPanel(sr, data);

        // Draw text content
        batch.begin();
        renderTopBarContent(batch, data);
        renderBottomPanelContent(batch, data);
        renderNotifications(batch, data.notifications);
        batch.end();
    }

    private void renderTopResourceBar(ShapeRenderer sr, GameUIData data) {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        float y = Constants.SCREEN_HEIGHT - TOP_BAR_HEIGHT;

        // Main wooden bar with gradient
        drawWoodenPanel(sr, 0, y, Constants.SCREEN_WIDTH, TOP_BAR_HEIGHT);

        sr.end();

        // Ornate border
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        sr.setColor(GOLD_BRIGHT);
        sr.line(0, y, Constants.SCREEN_WIDTH, y);

        sr.setColor(GOLD_DARK);
        sr.line(0, y + TOP_BAR_HEIGHT - 2, Constants.SCREEN_WIDTH, y + TOP_BAR_HEIGHT - 2);

        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void renderBottomCommandPanel(ShapeRenderer sr, GameUIData data) {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Main panel
        drawWoodenPanel(sr, 0, 0, Constants.SCREEN_WIDTH, BOTTOM_PANEL_HEIGHT);

        // Minimap section (left)
        drawFramedSection(sr, 15, 15, 200, 170, BRONZE);

        // Action buttons section (center)
        drawFramedSection(sr, 230, 15, 450, 170, LEATHER_BROWN);

        // Info section (right)
        drawFramedSection(sr, 695, 15, 370, 170, LEATHER_BROWN);

        sr.end();

        // Golden border on top
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(4);
        sr.setColor(GOLD_BRIGHT);
        sr.line(0, BOTTOM_PANEL_HEIGHT, Constants.SCREEN_WIDTH, BOTTOM_PANEL_HEIGHT);

        Gdx.gl.glLineWidth(2);
        sr.setColor(GOLD_DARK);
        sr.line(0, BOTTOM_PANEL_HEIGHT - 3, Constants.SCREEN_WIDTH, BOTTOM_PANEL_HEIGHT - 3);

        Gdx.gl.glLineWidth(1);
        sr.end();
    }

    private void drawWoodenPanel(ShapeRenderer sr, float x, float y, float w, float h) {
        // Base dark wood
        sr.setColor(WOOD_DARK);
        sr.rect(x, y, w, h);

        // Wood grain effect
        sr.setColor(WOOD_MEDIUM.r, WOOD_MEDIUM.g, WOOD_MEDIUM.b, 0.4f);
        for (int i = 0; i < h; i += 8) {
            float offset = (float)Math.sin(i * 0.2f) * 2;
            sr.rect(x + offset, y + i, w, 2);
        }

        // Edge shadows for depth
        sr.setColor(0, 0, 0, 0.4f);
        sr.rect(x, y, w, 3); // Bottom shadow
        sr.rect(x, y, 3, h); // Left shadow
    }

    private void drawFramedSection(ShapeRenderer sr, float x, float y, float w, float h, Color frameColor) {
        // Background
        sr.setColor(WOOD_MEDIUM.r * 0.8f, WOOD_MEDIUM.g * 0.8f, WOOD_MEDIUM.b * 0.8f, 0.9f);
        sr.rect(x, y, w, h);

        // Frame
        sr.setColor(frameColor);
        float thickness = 3;
        sr.rect(x, y, w, thickness); // Bottom
        sr.rect(x, y + h - thickness, w, thickness); // Top
        sr.rect(x, y, thickness, h); // Left
        sr.rect(x + w - thickness, y, thickness, h); // Right

        // Corner decorations
        float cornerSize = 12;
        sr.rect(x, y, cornerSize, cornerSize);
        sr.rect(x + w - cornerSize, y, cornerSize, cornerSize);
        sr.rect(x, y + h - cornerSize, cornerSize, cornerSize);
        sr.rect(x + w - cornerSize, y + h - cornerSize, cornerSize, cornerSize);
    }

    private void renderTopBarContent(SpriteBatch batch, GameUIData data) {
        float y = Constants.SCREEN_HEIGHT - 15;
        float iconGap = 180;
        float startX = 40;

        font.getData().setScale(1.3f);

        // Gold with shimmer
        float shimmer = (float)Math.sin(animTimer * 2) * 0.1f + 0.9f;
        drawTextWithShadow(batch, "⚜ " + data.gold, startX, y,
            new Color(GOLD_BRIGHT.r * shimmer, GOLD_BRIGHT.g * shimmer, GOLD_BRIGHT.b, 1f));

        // Wood
        drawTextWithShadow(batch, "🌲 " + data.wood, startX + iconGap, y,
            new Color(0.55f, 0.40f, 0.25f, 1f));

        // Stone
        drawTextWithShadow(batch, "⛰ " + data.stone, startX + iconGap * 2, y,
            new Color(0.65f, 0.65f, 0.70f, 1f));

        // Food
        drawTextWithShadow(batch, "🌾 " + data.food, startX + iconGap * 3, y,
            new Color(0.90f, 0.70f, 0.35f, 1f));

        // Population
        Color popColor = data.currentPop >= data.maxPop ?
            Color.RED : data.currentPop >= data.maxPop * 0.8f ?
            Color.ORANGE : PARCHMENT;
        drawTextWithShadow(batch, "👥 " + data.currentPop + "/" + data.maxPop,
            startX + iconGap * 4, y, popColor);

        // Game time (centered)
        String timeStr = String.format("%02d:%02d",
            (int)(data.gameTime / 60), (int)(data.gameTime % 60));
        font.getData().setScale(1.5f);
        layout.setText(font, timeStr);
        drawTextWithShadow(batch, timeStr,
            Constants.SCREEN_WIDTH/2 - layout.width/2, y, GOLD_BRIGHT);

        // Wave info (right)
        font.getData().setScale(1.1f);
        String waveText = "Wave " + data.currentWave;
        layout.setText(font, waveText);
        drawTextWithShadow(batch, waveText,
            Constants.SCREEN_WIDTH - layout.width - 40, y,
            data.waveInProgress ? Color.RED : Color.GREEN);
    }

    private void renderBottomPanelContent(SpriteBatch batch, GameUIData data) {
        // Action buttons section
        renderActionButtons(batch, 250, 30);

        // Selection info section
        renderSelectionInfo(batch, data, 710, 30);
    }

    private void renderActionButtons(SpriteBatch batch, float baseX, float baseY) {
        font.getData().setScale(1.2f);
        drawTextWithShadow(batch, "⚔ RECRUIT UNITS", baseX, baseY + 145, GOLD_BRIGHT);

        font.getData().setScale(0.9f);
        float x = baseX;
        float y = baseY + 110;
        float spacing = 140;

        // Row 1
        renderButton(batch, x, y, "[1] Worker", "50g");
        renderButton(batch, x + spacing, y, "[2] Soldier", "100g");
        renderButton(batch, x + spacing * 2, y, "[3] Archer", "120g");

        // Row 2
        y -= 45;
        renderButton(batch, x, y, "[4] Cavalry", "200g");
        renderButton(batch, x + spacing, y, "[5] Champion", "400g");
        renderButton(batch, x + spacing * 2, y, "[6] Ram", "300g");

        // Commands
        y -= 50;
        font.getData().setScale(0.8f);
        drawTextWithShadow(batch, "[Q] Attack Gate  [E] Stop  [H] Hold  [F] Formation",
            baseX, y, PARCHMENT);
    }

    private void renderButton(SpriteBatch batch, float x, float y, String label, String cost) {
        drawTextWithShadow(batch, label, x, y, PARCHMENT);

        font.getData().setScale(0.7f);
        drawTextWithShadow(batch, cost, x, y - 18, GOLD_DARK);
        font.getData().setScale(0.9f);
    }

    private void renderSelectionInfo(SpriteBatch batch, GameUIData data, float x, float y) {
        font.getData().setScale(1.2f);

        if (!data.selectedUnits.isEmpty()) {
            Unit unit = data.selectedUnits.get(0);
            drawTextWithShadow(batch, unit.getName().toUpperCase(), x, y + 145, GOLD_BRIGHT);

            font.getData().setScale(0.95f);
            y += 110;
            drawTextWithShadow(batch, "HP: " + (int)unit.getHealth() + "/" + (int)unit.getMaxHealth(),
                x, y, Color.GREEN);

            y -= 25;
            drawTextWithShadow(batch, "⚔ Attack: " + (int)unit.getAttackDamage(), x, y, Color.RED);

            y -= 25;
            drawTextWithShadow(batch, "🛡 Armor: " + (int)unit.getArmor(), x, y, Color.CYAN);

            if (data.selectedUnits.size() > 1) {
                y -= 30;
                font.getData().setScale(0.85f);
                drawTextWithShadow(batch, "+ " + (data.selectedUnits.size() - 1) + " more units",
                    x, y, Color.GRAY);
            }
        } else {
            drawTextWithShadow(batch, "NO SELECTION", x, y + 145, Color.GRAY);
        }

        // Wave status
        y = 50;
        font.getData().setScale(1.0f);
        String status = data.waveInProgress ?
            "⚠ WAVE " + data.currentWave + " ATTACKING!" :
            "Next wave in: " + (int)(data.timeToNextWave) + "s";
        drawTextWithShadow(batch, status, x, y,
            data.waveInProgress ? Color.RED : Color.YELLOW);
    }

    private void renderNotifications(SpriteBatch batch, ArrayList<Notification> notifications) {
        if (notifications.isEmpty()) return;

        float y = Constants.SCREEN_HEIGHT - 80;
        float maxWidth = 400;

        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification n = notifications.get(i);

            font.getData().setScale(1.0f);
            String text = n.getMessage();
            layout.setText(font, text);

            float x = Constants.SCREEN_WIDTH - layout.width - 30;

            // Notification background
            batch.end();
            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Filled);

            sr.setColor(WOOD_DARK.r, WOOD_DARK.g, WOOD_DARK.b, n.alpha * 0.9f);
            sr.rect(x - 15, y - 25, layout.width + 30, 35);

            sr.setColor(GOLD_DARK.r, GOLD_DARK.g, GOLD_DARK.b, n.alpha);
            sr.rect(x - 15, y - 25, 3, 35);
            sr.rect(x + layout.width + 12, y - 25, 3, 35);

            sr.end();
            sr.dispose();
            batch.begin();

            // Text
            drawTextWithShadow(batch, text, x, y,
                new Color(n.getColor().r, n.getColor().g, n.getColor().b, n.alpha));

            y -= 45;
        }
    }

    private void drawTextWithShadow(SpriteBatch batch, String text, float x, float y, Color color) {
        // Shadow
        font.setColor(TEXT_SHADOW);
        font.draw(batch, text, x + 2, y - 2);

        // Main text
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    /**
     * Data class to pass UI information from GameScreen
     */
    public static class GameUIData {
        public int gold, wood, stone, food;
        public int currentPop, maxPop;
        public float gameTime;
        public int currentWave;
        public boolean waveInProgress;
        public float timeToNextWave;
        public ArrayList<Unit> selectedUnits;
        public ArrayList<Notification> notifications;

        public GameUIData() {
            selectedUnits = new ArrayList<>();
            notifications = new ArrayList<>();
        }
    }
}
