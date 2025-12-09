package com.vijithapura.siege.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.vijithapura.siege.entities.*;
import com.vijithapura.siege.screens.GameScreen;
import com.vijithapura.siege.utils.Constants;

import java.util.ArrayList;

/**
 * Age of Empires 3 Inspired UI Manager
 * Features: Rich medieval theme, ornate borders, aged parchment look
 */
public class UIManager {
    private BitmapFont font;
    private GlyphLayout layout;

    // UI Panels
    private UIPanel topBar;
    private UIPanel bottomBar;
    private UIPanel selectionPanel;
    private UIPanel buildMenuPanel;
    private UIPanel minimapFrame;
    private UIPanel waveWarningPanel;

    // Animation
    private float pulseTimer;
    private float goldShimmer;

    // AoE3 Inspired Color Palette
    private static final Color WOOD_DARK = new Color(0.15f, 0.10f, 0.05f, 0.95f);
    private static final Color WOOD_MEDIUM = new Color(0.25f, 0.18f, 0.10f, 1f);
    private static final Color WOOD_LIGHT = new Color(0.35f, 0.25f, 0.15f, 1f);

    private static final Color GOLD_SHINE = new Color(0.95f, 0.85f, 0.35f, 1f);
    private static final Color GOLD_DARK = new Color(0.65f, 0.50f, 0.15f, 1f);
    private static final Color BRONZE = new Color(0.55f, 0.35f, 0.20f, 1f);

    private static final Color PARCHMENT = new Color(0.90f, 0.85f, 0.70f, 1f);
    private static final Color PARCHMENT_DARK = new Color(0.75f, 0.70f, 0.55f, 1f);

    private static final Color STEEL_BLUE = new Color(0.30f, 0.40f, 0.55f, 1f);
    private static final Color BLOOD_RED = new Color(0.65f, 0.15f, 0.15f, 1f);

    private static final Color TEXT_LIGHT = new Color(0.95f, 0.92f, 0.85f, 1f);
    private static final Color TEXT_DARK = new Color(0.20f, 0.15f, 0.10f, 1f);

    // Health bar colors
    private static final Color HP_FULL = new Color(0.25f, 0.65f, 0.25f, 1f);
    private static final Color HP_MID = new Color(0.85f, 0.70f, 0.20f, 1f);
    private static final Color HP_LOW = new Color(0.75f, 0.20f, 0.15f, 1f);

    public static class UIPanel {
        public Rectangle bounds;
        public Color bgColor;
        public Color borderColor;
        public Color accentColor;
        public boolean visible;
        public String title;
        public boolean ornate; // Fancy decorative borders

        public UIPanel(float x, float y, float w, float h, boolean ornate) {
            bounds = new Rectangle(x, y, w, h);
            bgColor = WOOD_DARK;
            borderColor = GOLD_SHINE;
            accentColor = GOLD_DARK;
            visible = true;
            title = "";
            this.ornate = ornate;
        }

        public void render(ShapeRenderer sr) {
            if (!visible) return;

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Wood panel background with gradient effect
            drawWoodPanel(sr, bounds.x, bounds.y, bounds.width, bounds.height);

            sr.end();

            // Ornate borders
            sr.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);

            if (ornate) {
                drawOrnateBorder(sr, bounds);
            } else {
                // Simple gold border
                sr.setColor(borderColor);
                sr.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            Gdx.gl.glLineWidth(1);
            sr.end();
        }

        private void drawWoodPanel(ShapeRenderer sr, float x, float y, float w, float h) {
            // Base wood color
            sr.setColor(bgColor);
            sr.rect(x, y, w, h);

            // Wood grain effect (subtle horizontal lines)
            sr.setColor(WOOD_MEDIUM.r, WOOD_MEDIUM.g, WOOD_MEDIUM.b, 0.3f);
            for (int i = 0; i < h; i += 8) {
                sr.rect(x, y + i, w, 2);
            }

            // Darker edges for depth
            sr.setColor(0, 0, 0, 0.3f);
            sr.rect(x, y, w, 4); // Bottom shadow
            sr.rect(x, y, 4, h); // Left shadow
        }

        private void drawOrnateBorder(ShapeRenderer sr, Rectangle bounds) {
            float x = bounds.x;
            float y = bounds.y;
            float w = bounds.width;
            float h = bounds.height;

            // Outer gold frame
            sr.setColor(borderColor);
            sr.rect(x, y, w, h);

            // Inner bronze accent
            sr.setColor(accentColor);
            sr.rect(x + 3, y + 3, w - 6, h - 6);

            // Corner decorations (diagonal lines creating ornate corners)
            sr.setColor(GOLD_SHINE);
            float cornerSize = 20;

            // Top-left corner
            sr.line(x, y + h, x + cornerSize, y + h);
            sr.line(x, y + h, x, y + h - cornerSize);
            sr.line(x + 5, y + h - 5, x + cornerSize - 5, y + h - 5);
            sr.line(x + 5, y + h - 5, x + 5, y + h - cornerSize + 5);

            // Top-right corner
            sr.line(x + w, y + h, x + w - cornerSize, y + h);
            sr.line(x + w, y + h, x + w, y + h - cornerSize);
            sr.line(x + w - 5, y + h - 5, x + w - cornerSize + 5, y + h - 5);
            sr.line(x + w - 5, y + h - 5, x + w - 5, y + h - cornerSize + 5);

            // Bottom-left corner
            sr.line(x, y, x + cornerSize, y);
            sr.line(x, y, x, y + cornerSize);
            sr.line(x + 5, y + 5, x + cornerSize - 5, y + 5);
            sr.line(x + 5, y + 5, x + 5, y + cornerSize - 5);

            // Bottom-right corner
            sr.line(x + w, y, x + w - cornerSize, y);
            sr.line(x + w, y, x + w, y + cornerSize);
            sr.line(x + w - 5, y + 5, x + w - cornerSize + 5, y + 5);
            sr.line(x + w - 5, y + 5, x + w - 5, y + cornerSize - 5);
        }
    }

    public UIManager(BitmapFont font) {
        this.font = font;
        this.layout = new GlyphLayout();

        float screenW = Constants.SCREEN_WIDTH;
        float screenH = Constants.SCREEN_HEIGHT;

        // Top bar - Resources and game info (ornate)
        topBar = new UIPanel(0, screenH - 80, screenW, 80, true);
        topBar.title = "Resources";

        // Bottom action bar (ornate)
        bottomBar = new UIPanel(0, 0, screenW, 180, true);
        bottomBar.title = "Command Panel";

        // Selection info panel (right side, ornate)
        selectionPanel = new UIPanel(screenW - 340, 190, 320, 420, true);
        selectionPanel.title = "Unit Details";
        selectionPanel.visible = false;

        // Build menu (left side, ornate)
        buildMenuPanel = new UIPanel(20, 190, 280, 380, true);
        buildMenuPanel.title = "Production";

        // Minimap frame (ornate)
        minimapFrame = new UIPanel(screenW - 240, 20, 220, 220, true);
        minimapFrame.title = "Map";

        // Wave warning
        waveWarningPanel = new UIPanel(screenW/2 - 200, screenH - 150, 400, 70, true);
        waveWarningPanel.bgColor = new Color(0.4f, 0.05f, 0.05f, 0.95f);
        waveWarningPanel.borderColor = BLOOD_RED;
        waveWarningPanel.visible = false;

        pulseTimer = 0;
        goldShimmer = 0;
    }

    public void update(float delta) {
        pulseTimer += delta;
        goldShimmer += delta * 2;
    }

    public void render(ShapeRenderer sr, SpriteBatch batch, GameScreen screen) {
        // Render all panels
        topBar.render(sr);
        bottomBar.render(sr);
        buildMenuPanel.render(sr);
        minimapFrame.render(sr);

        if (selectionPanel.visible) {
            selectionPanel.render(sr);
        }

        if (waveWarningPanel.visible) {
            waveWarningPanel.render(sr);
        }

        // Render text content
        batch.begin();
        renderTopBar(batch, screen);
        renderBottomBar(batch, screen);
        renderBuildMenu(batch, screen);
        renderSelectionInfo(batch, screen);
        renderWaveWarning(batch, screen);
        renderNotifications(batch, screen);
        batch.end();
    }

    private void renderTopBar(SpriteBatch batch, GameScreen screen) {
        float y = Constants.SCREEN_HEIGHT - 30;
        float x = 40;

        // Resources with medieval styling
        font.getData().setScale(1.2f);

        // Gold with shimmer effect
        Color goldColor = new Color(
            GOLD_SHINE.r,
            GOLD_SHINE.g,
            GOLD_SHINE.b,
            0.85f + (float)Math.sin(goldShimmer) * 0.15f
        );
        font.setColor(goldColor);
        drawTextWithEmboss(batch, "⚜ " + screen.getPlayerGold(), x, y, TEXT_DARK);

        // Wood
        x += 180;
        font.setColor(new Color(0.55f, 0.35f, 0.20f, 1f));
        drawTextWithEmboss(batch, "⚒ " + screen.getPlayerWood(), x, y, TEXT_DARK);

        // Stone
        x += 180;
        font.setColor(new Color(0.60f, 0.60f, 0.65f, 1f));
        drawTextWithEmboss(batch, "◆ " + screen.getPlayerStone(), x, y, TEXT_DARK);

        // Food
        x += 180;
        font.setColor(new Color(0.85f, 0.65f, 0.30f, 1f));
        drawTextWithEmboss(batch, "🌾 " + screen.getPlayerFood(), x, y, TEXT_DARK);

        // Population with color coding
        x += 200;
        int pop = screen.getCurrentPopulation();
        int maxPop = screen.getMaxPopulation();
        Color popColor;
        if (pop >= maxPop) {
            popColor = BLOOD_RED;
        } else if (pop >= maxPop * 0.8f) {
            popColor = new Color(0.90f, 0.60f, 0.20f, 1f);
        } else {
            popColor = PARCHMENT;
        }
        font.setColor(popColor);
        drawTextWithEmboss(batch, "👥 " + pop + "/" + maxPop, x, y, TEXT_DARK);

        // Game timer (center)
        float gameTime = screen.getGameTime();
        int minutes = (int)(gameTime / 60);
        int seconds = (int)(gameTime % 60);
        String timeStr = String.format("%02d:%02d", minutes, seconds);

        font.getData().setScale(1.4f);
        font.setColor(PARCHMENT);
        layout.setText(font, timeStr);
        drawTextWithEmboss(batch, timeStr,
            Constants.SCREEN_WIDTH/2 - layout.width/2, y, TEXT_DARK);

        // Army strength (right side)
        y -= 30;
        x = Constants.SCREEN_WIDTH - 350;
        font.getData().setScale(1.0f);

        int playerCount = countAlive(screen.getPlayerUnits());
        font.setColor(STEEL_BLUE);
        drawTextWithEmboss(batch, "⚔ Your Army: " + playerCount, x, y, TEXT_DARK);

        x += 160;
        int enemyCount = countAlive(screen.getEnemyUnits());
        font.setColor(BLOOD_RED);
        drawTextWithEmboss(batch, "☠ Enemies: " + enemyCount, x, y, TEXT_DARK);
    }

    private void renderBottomBar(SpriteBatch batch, GameScreen screen) {
        float baseY = 145;
        float leftX = 40;
        float centerX = Constants.SCREEN_WIDTH / 2 - 200;
        float rightX = Constants.SCREEN_WIDTH - 350;

        // Left section - Quick commands
        font.getData().setScale(1.1f);
        font.setColor(GOLD_SHINE);
        drawTextWithEmboss(batch, "⚔ Commands", leftX, baseY, TEXT_DARK);

        float y = baseY - 25;
        font.getData().setScale(0.85f);
        renderCommandButton(batch, leftX, y, "Q", "Attack Gate", BLOOD_RED);
        y -= 24;
        renderCommandButton(batch, leftX, y, "E", "Stop", new Color(0.90f, 0.70f, 0.30f, 1f));
        y -= 24;
        renderCommandButton(batch, leftX, y, "H", "Hold", HP_FULL);
        y -= 24;
        renderCommandButton(batch, leftX, y, "F", "Formation", STEEL_BLUE);

        // Center section - Control groups
        font.getData().setScale(1.1f);
        font.setColor(GOLD_SHINE);
        drawTextWithEmboss(batch, "⚡ Groups", centerX, baseY, TEXT_DARK);

        y = baseY - 25;
        font.getData().setScale(0.75f);
        font.setColor(PARCHMENT_DARK);
        drawTextWithEmboss(batch, "Ctrl+[1-9] Save Group", centerX, y, TEXT_DARK);
        y -= 20;
        drawTextWithEmboss(batch, "[1-9] Select Group", centerX, y, TEXT_DARK);
        y -= 20;
        drawTextWithEmboss(batch, "Double-tap: Center View", centerX, y, TEXT_DARK);

        // Right section - Wave info
        font.getData().setScale(1.1f);
        font.setColor(GOLD_SHINE);
        drawTextWithEmboss(batch, "🏰 Battle", rightX, baseY, TEXT_DARK);

        y = baseY - 25;
        font.getData().setScale(0.9f);
        font.setColor(new Color(0.90f, 0.75f, 0.40f, 1f));
        drawTextWithEmboss(batch, "Wave: " + screen.getCurrentWave(), rightX, y, TEXT_DARK);

        y -= 22;
        font.setColor(PARCHMENT_DARK);
        String waveStatus = screen.isWaveInProgress() ? "In Progress" : "Preparing...";
        drawTextWithEmboss(batch, "Status: " + waveStatus, rightX, y, TEXT_DARK);

        y -= 22;
        font.setColor(new Color(0.70f, 0.85f, 0.70f, 1f));
        drawTextWithEmboss(batch, "Defeated: " + screen.getWavesDefeated(), rightX, y, TEXT_DARK);
    }

    private void renderBuildMenu(SpriteBatch batch, GameScreen screen) {
        float x = buildMenuPanel.bounds.x + 20;
        float y = buildMenuPanel.bounds.y + buildMenuPanel.bounds.height - 35;

        // Title
        font.getData().setScale(1.2f);
        font.setColor(GOLD_SHINE);
        drawTextWithEmboss(batch, "⚒ RECRUIT", x, y, TEXT_DARK);

        y -= 40;
        font.getData().setScale(0.85f);

        // Unit buttons with medieval styling
        renderRecruitButton(batch, x, y, "1", "Worker", 50, 0, 0, 10,
            new Color(0.75f, 0.65f, 0.45f, 1f));
        y -= 50;

        renderRecruitButton(batch, x, y, "2", "Soldier", 100, 20, 0, 15,
            new Color(0.50f, 0.55f, 0.70f, 1f));
        y -= 50;

        renderRecruitButton(batch, x, y, "3", "Archer", 120, 30, 0, 15,
            new Color(0.45f, 0.65f, 0.45f, 1f));
        y -= 50;

        renderRecruitButton(batch, x, y, "4", "Cavalry", 200, 0, 0, 25,
            new Color(0.75f, 0.60f, 0.40f, 1f));
        y -= 50;

        renderRecruitButton(batch, x, y, "5", "Champion", 300, 50, 0, 30,
            new Color(0.75f, 0.45f, 0.70f, 1f));
        y -= 50;

        renderRecruitButton(batch, x, y, "6", "Siege Ram", 250, 100, 0, 0,
            new Color(0.45f, 0.40f, 0.40f, 1f));
    }

    private void renderRecruitButton(SpriteBatch batch, float x, float y,
                                     String key, String name,
                                     int gold, int wood, int stone, int food,
                                     Color nameColor) {
        // Hotkey badge
        font.getData().setScale(0.9f);
        font.setColor(GOLD_DARK);
        drawTextWithEmboss(batch, "[" + key + "]", x, y, TEXT_DARK);

        // Unit name
        font.setColor(nameColor);
        font.getData().setScale(0.95f);
        drawTextWithEmboss(batch, name, x + 40, y, TEXT_DARK);

        // Cost on next line
        y -= 18;
        font.getData().setScale(0.7f);
        font.setColor(PARCHMENT_DARK);

        StringBuilder cost = new StringBuilder();
        if (gold > 0) cost.append("⚜").append(gold).append(" ");
        if (wood > 0) cost.append("⚒").append(wood).append(" ");
        if (stone > 0) cost.append("◆").append(stone).append(" ");
        if (food > 0) cost.append("🌾").append(food);

        drawTextWithEmboss(batch, cost.toString(), x + 40, y, TEXT_DARK);
    }

    private void renderCommandButton(SpriteBatch batch, float x, float y,
                                     String key, String label, Color color) {
        // Hotkey
        font.setColor(GOLD_DARK);
        drawTextWithEmboss(batch, "[" + key + "]", x, y, TEXT_DARK);

        // Command
        font.setColor(color);
        drawTextWithEmboss(batch, label, x + 40, y, TEXT_DARK);
    }

    private void renderSelectionInfo(SpriteBatch batch, GameScreen screen) {
        ArrayList<Unit> selected = screen.getSelectedUnits();

        if (selected.isEmpty()) {
            selectionPanel.visible = false;
            return;
        }

        selectionPanel.visible = true;
        float x = selectionPanel.bounds.x + 20;
        float y = selectionPanel.bounds.y + selectionPanel.bounds.height - 35;

        // Title
        font.getData().setScale(1.2f);
        font.setColor(GOLD_SHINE);
        drawTextWithEmboss(batch, "⚔ SELECTED", x, y, TEXT_DARK);

        y -= 35;
        font.getData().setScale(0.9f);
        font.setColor(PARCHMENT);
        drawTextWithEmboss(batch, "Units: " + selected.size(), x, y, TEXT_DARK);

        if (selected.size() == 1) {
            // Single unit details
            Unit unit = selected.get(0);
            y -= 30;

            font.getData().setScale(1.1f);
            font.setColor(GOLD_SHINE);
            drawTextWithEmboss(batch, unit.getName(), x, y, TEXT_DARK);

            y -= 25;
            font.getData().setScale(0.8f);
            font.setColor(PARCHMENT_DARK);
            String unitType = unit.getClass().getSimpleName();
            drawTextWithEmboss(batch, "Level " + unit.getLevel() + " " + unitType, x, y, TEXT_DARK);

            // Health bar
            y -= 35;
            renderMedievalHealthBar(batch, x, y, unit.getHealth(), unit.getMaxHealth(), 280);

            // Stats with icons
            y -= 40;
            font.getData().setScale(0.85f);

            font.setColor(new Color(0.85f, 0.50f, 0.50f, 1f));
            drawTextWithEmboss(batch, "⚔ Attack: " + (int)unit.getAttackDamage(), x, y, TEXT_DARK);

            y -= 24;
            font.setColor(new Color(0.60f, 0.65f, 0.70f, 1f));
            drawTextWithEmboss(batch, "🛡 Armor: " + (int)unit.getArmor(), x, y, TEXT_DARK);

            y -= 24;
            font.setColor(new Color(0.65f, 0.75f, 0.55f, 1f));
            drawTextWithEmboss(batch, "🎯 Range: " + (int)unit.getAttackRange(), x, y, TEXT_DARK);

            y -= 24;
            font.setColor(new Color(0.75f, 0.80f, 0.55f, 1f));
            drawTextWithEmboss(batch, "⚡ Speed: " + (int)unit.getMoveSpeed(), x, y, TEXT_DARK);

            // Experience bar
            y -= 35;
            font.setColor(new Color(0.70f, 0.55f, 0.85f, 1f));
            drawTextWithEmboss(batch, "Experience:", x, y, TEXT_DARK);
            y -= 8;
            renderExperienceBar(batch, x, y, unit.getExperience(),
                unit.getExperienceToNextLevel(), 280);

            y -= 28;
            font.getData().setScale(0.8f);
            font.setColor(PARCHMENT_DARK);
            drawTextWithEmboss(batch, "Kills: " + unit.getKillCount(), x, y, TEXT_DARK);

        } else {
            // Group composition
            y -= 35;
            int[] types = countUnitTypes(selected);

            font.getData().setScale(0.85f);

            if (types[0] > 0) {
                font.setColor(new Color(0.75f, 0.65f, 0.45f, 1f));
                drawTextWithEmboss(batch, "👷 Workers: " + types[0], x, y, TEXT_DARK);
                y -= 24;
            }
            if (types[1] > 0) {
                font.setColor(new Color(0.50f, 0.55f, 0.70f, 1f));
                drawTextWithEmboss(batch, "⚔ Soldiers: " + types[1], x, y, TEXT_DARK);
                y -= 24;
            }
            if (types[2] > 0) {
                font.setColor(new Color(0.45f, 0.65f, 0.45f, 1f));
                drawTextWithEmboss(batch, "🏹 Archers: " + types[2], x, y, TEXT_DARK);
                y -= 24;
            }
            if (types[3] > 0) {
                font.setColor(new Color(0.75f, 0.60f, 0.40f, 1f));
                drawTextWithEmboss(batch, "🐴 Cavalry: " + types[3], x, y, TEXT_DARK);
                y -= 24;
            }
            if (types[4] > 0) {
                font.setColor(new Color(0.75f, 0.45f, 0.70f, 1f));
                drawTextWithEmboss(batch, "👑 Champions: " + types[4], x, y, TEXT_DARK);
                y -= 24;
            }
            if (types[5] > 0) {
                font.setColor(new Color(0.45f, 0.40f, 0.40f, 1f));
                drawTextWithEmboss(batch, "🏰 Rams: " + types[5], x, y, TEXT_DARK);
                y -= 24;
            }

            y -= 10;
            float avgHealth = calculateAverageHealth(selected);
            font.setColor(PARCHMENT);
            drawTextWithEmboss(batch, "Average Health: " + (int)avgHealth + "%", x, y, TEXT_DARK);
        }
    }

    private void renderMedievalHealthBar(SpriteBatch batch, float x, float y,
                                         float current, float max, float width) {
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        float height = 22;
        float healthPercent = current / max;

        // Background (dark wood)
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(WOOD_DARK);
        sr.rect(x, y - height, width, height);

        // Health fill with gradient
        Color hpColor = healthPercent > 0.6f ? HP_FULL :
            healthPercent > 0.3f ? HP_MID : HP_LOW;

        sr.setColor(hpColor.r * 0.7f, hpColor.g * 0.7f, hpColor.b * 0.7f, 1f);
        sr.rect(x + 3, y - height + 3, (width - 6) * healthPercent, height - 6);

        // Highlight on health bar
        sr.setColor(hpColor.r * 1.2f, hpColor.g * 1.2f, hpColor.b * 1.2f, 0.5f);
        sr.rect(x + 3, y - height + 3, (width - 6) * healthPercent, 4);

        sr.end();

        // Gold border
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        sr.setColor(GOLD_DARK);
        sr.rect(x, y - height, width, height);

        sr.setColor(GOLD_SHINE);
        sr.rect(x + 2, y - height + 2, width - 4, height - 4);
        Gdx.gl.glLineWidth(1);
        sr.end();

        sr.dispose();
        batch.begin();

        // Health text
        font.getData().setScale(0.75f);
        font.setColor(TEXT_LIGHT);
        String hpText = (int)current + " / " + (int)max;
        layout.setText(font, hpText);

        // Text shadow
        font.setColor(TEXT_DARK);
        font.draw(batch, hpText, x + width/2 - layout.width/2 + 1, y - height/2 + 5);

        font.setColor(TEXT_LIGHT);
        font.draw(batch, hpText, x + width/2 - layout.width/2, y - height/2 + 6);
    }

    private void renderExperienceBar(SpriteBatch batch, float x, float y,
                                     int current, int max, float width) {
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        float height = 14;
        float xpPercent = (float)current / max;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        sr.setColor(0.15f, 0.10f, 0.20f, 0.9f);
        sr.rect(x, y - height, width, height);

        // XP fill (purple/gold gradient)
        sr.setColor(0.55f, 0.35f, 0.70f, 1f);
        sr.rect(x + 2, y - height + 2, (width - 4) * xpPercent, height - 4);

        // Shine effect
        sr.setColor(0.75f, 0.55f, 0.85f, 0.5f);
        sr.rect(x + 2, y - height + 2, (width - 4) * xpPercent, 3);

        sr.end();

        // Border
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(BRONZE);
        sr.rect(x, y - height, width, height);
        sr.end();

        sr.dispose();
        batch.begin();

        // XP text
        font.getData().setScale(0.65f);
        font.setColor(PARCHMENT);
        String xpText = current + " / " + max;
        layout.setText(font, xpText);
        font.draw(batch, xpText, x + width/2 - layout.width/2, y - height/2 + 4);
    }

    private void renderWaveWarning(SpriteBatch batch, GameScreen screen) {
        if (!screen.isWaveInProgress()) {
            waveWarningPanel.visible = false;
            return;
        }

        waveWarningPanel.visible = true;

        float x = waveWarningPanel.bounds.x + waveWarningPanel.bounds.width / 2;
        float y = waveWarningPanel.bounds.y + waveWarningPanel.bounds.height / 2 + 15;

        // Pulsing warning
        float pulse = (float)Math.sin(pulseTimer * 4) * 0.15f + 0.85f;

        font.getData().setScale(1.6f * pulse);
        font.setColor(new Color(1f, 0.85f, 0.70f, 1f));
        String waveText = "⚠ WAVE " + screen.getCurrentWave() + " ⚠";
        layout.setText(font, waveText);

        drawTextWithEmboss(batch, waveText, x - layout.width/2, y, BLOOD_RED);

        y -= 22;
        font.getData().setScale(0.95f);
        font.setColor(new Color(0.95f, 0.75f, 0.60f, 1f));
        String enemyText = "⚔ Enemies Remaining: " + countAlive(screen.getEnemyUnits());
        layout.setText(font, enemyText);
        drawTextWithEmboss(batch, enemyText, x - layout.width/2, y, TEXT_DARK);
    }

    private void renderNotifications(SpriteBatch batch, GameScreen screen) {
        ArrayList<Notification> notifications = screen.getNotifications();
        if (notifications == null || notifications.isEmpty()) return;

        float y = Constants.SCREEN_HEIGHT / 2 + 100;
        float x = Constants.SCREEN_WIDTH / 2;

        for (Notification n : notifications) {
            font.getData().setScale(1.15f);

            // Create parchment-style notification background
            float textWidth = 300;
            batch.end();

            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(batch.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Parchment background
            sr.setColor(PARCHMENT.r, PARCHMENT.g, PARCHMENT.b, n.alpha * 0.9f);
            sr.rect(x - textWidth/2 - 10, y - 25, textWidth + 20, 35);

            sr.end();

            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(GOLD_DARK.r, GOLD_DARK.g, GOLD_DARK.b, n.alpha);
            sr.rect(x - textWidth/2 - 10, y - 25, textWidth + 20, 35);
            sr.end();

            sr.dispose();
            batch.begin();

            // Message text
            layout.setText(font, n.message);
            font.setColor(TEXT_DARK.r, TEXT_DARK.g, TEXT_DARK.b, n.alpha);
            font.draw(batch, n.message, x - layout.width/2, y);

            y -= 45;
        }
    }

    private void drawTextWithEmboss(SpriteBatch batch, String text,
                                    float x, float y, Color shadowColor) {
        Color original = font.getColor().cpy();

        // Shadow/emboss effect
        font.setColor(shadowColor.r, shadowColor.g, shadowColor.b, original.a * 0.5f);
        font.draw(batch, text, x + 2, y - 2);

        // Main text
        font.setColor(original);
        font.draw(batch, text, x, y);
    }

    private int countAlive(ArrayList<Unit> units) {
        int count = 0;
        for (Unit u : units) {
            if (u != null && u.isAlive()) count++;
        }
        return count;
    }

    private int[] countUnitTypes(ArrayList<Unit> units) {
        int[] counts = new int[6];
        for (Unit u : units) {
            if (u == null || !u.isAlive()) continue;
            if (u instanceof Worker) counts[0]++;
            else if (u instanceof Soldier) counts[1]++;
            else if (u instanceof Archer) counts[2]++;
            else if (u instanceof Cavalry) counts[3]++;
            else if (u instanceof Champion) counts[4]++;
            else if (u instanceof SiegeRam) counts[5]++;
        }
        return counts;
    }

    private float calculateAverageHealth(ArrayList<Unit> units) {
        if (units.isEmpty()) return 0;
        float total = 0;
        int count = 0;
        for (Unit u : units) {
            if (u != null && u.isAlive()) {
                total += (u.getHealth() / u.getMaxHealth()) * 100;
                count++;
            }
        }
        return count > 0 ? total / count : 0;
    }

    public void setSelectionVisible(boolean visible) {
        selectionPanel.visible = visible;
    }

    public UIPanel getMinimapPanel() {
        return minimapFrame;
    }
}
