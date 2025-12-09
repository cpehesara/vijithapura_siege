package com.vijithapura.siege.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.vijithapura.siege.VijithapuraGame;
import com.vijithapura.siege.utils.Constants;

/**
 * Age of Empires 3 Inspired Main Menu
 * Features: Rich medieval theme, ornate UI, atmospheric presentation
 */
public class MenuScreen implements Screen {
    private VijithapuraGame game;
    private OrthographicCamera camera;
    private BitmapFont titleFont;
    private BitmapFont menuFont;
    private GlyphLayout layout;

    // Background
    private Texture backgroundTexture;

    // AoE3 Color Palette
    private static final Color WOOD_DARK = new Color(0.15f, 0.10f, 0.05f, 0.95f);
    private static final Color WOOD_MEDIUM = new Color(0.25f, 0.18f, 0.10f, 1f);
    private static final Color GOLD_SHINE = new Color(0.95f, 0.85f, 0.35f, 1f);
    private static final Color GOLD_DARK = new Color(0.65f, 0.50f, 0.15f, 1f);
    private static final Color PARCHMENT = new Color(0.90f, 0.85f, 0.70f, 1f);
    private static final Color BRONZE = new Color(0.55f, 0.35f, 0.20f, 1f);

    private static final Color EASY_COLOR = new Color(0.45f, 0.75f, 0.45f, 1f);
    private static final Color NORMAL_COLOR = new Color(0.75f, 0.70f, 0.40f, 1f);
    private static final Color HARD_COLOR = new Color(0.85f, 0.35f, 0.25f, 1f);

    // Menu buttons
    private MenuButton easyButton;
    private MenuButton normalButton;
    private MenuButton hardButton;
    private MenuButton exitButton;

    // UI State
    private int hoveredButton = -1;
    private float animationTimer = 0;
    private boolean transitioning = false;
    private float transitionAlpha = 0;

    // Atmospheric particles
    private float[] dustX = new float[40];
    private float[] dustY = new float[40];
    private float[] dustSpeed = new float[40];
    private float[] dustSize = new float[40];

    private static class MenuButton {
        Rectangle bounds;
        String label;
        String description;
        Color color;
        Color hoverColor;
        boolean ornate;

        MenuButton(float x, float y, float w, float h, String label, String desc, Color color) {
            bounds = new Rectangle(x, y, w, h);
            this.label = label;
            this.description = desc;
            this.color = color;
            this.hoverColor = new Color(color.r * 1.3f, color.g * 1.3f, color.b * 1.3f, 1f);
            this.ornate = true;
        }

        void render(ShapeRenderer sr, boolean isHovered, float pulse) {
            float x = bounds.x;
            float y = bounds.y;
            float w = bounds.width;
            float h = bounds.height;

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sr.begin(ShapeRenderer.ShapeType.Filled);

            // Button shadow
            sr.setColor(0, 0, 0, 0.5f);
            sr.rect(x + 4, y - 4, w, h);

            // Wood panel background
            sr.setColor(WOOD_DARK);
            sr.rect(x, y, w, h);

            // Wood grain effect
            sr.setColor(WOOD_MEDIUM.r, WOOD_MEDIUM.g, WOOD_MEDIUM.b, 0.4f);
            for (int i = 0; i < h; i += 6) {
                sr.rect(x, y + i, w, 2);
            }

            // Colored overlay for button type
            Color btnColor = isHovered ? hoverColor : color;
            sr.setColor(btnColor.r, btnColor.g, btnColor.b, isHovered ? 0.5f : 0.3f);
            sr.rect(x + 4, y + 4, w - 8, h - 8);

            // Highlight effect when hovered
            if (isHovered) {
                sr.setColor(GOLD_SHINE.r, GOLD_SHINE.g, GOLD_SHINE.b, pulse * 0.3f);
                sr.rect(x + 2, y + h - 6, w - 4, 4);
            }

            sr.end();

            // Ornate border
            sr.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);

            // Outer gold border
            sr.setColor(isHovered ? GOLD_SHINE : GOLD_DARK);
            sr.rect(x, y, w, h);

            // Inner bronze trim
            Gdx.gl.glLineWidth(1.5f);
            sr.setColor(BRONZE);
            sr.rect(x + 3, y + 3, w - 6, h - 6);

            // Corner decorations
            if (ornate) {
                float cornerSize = 15;
                Gdx.gl.glLineWidth(2);
                sr.setColor(isHovered ? GOLD_SHINE : GOLD_DARK);

                // Top-left
                sr.line(x, y + h, x + cornerSize, y + h);
                sr.line(x, y + h, x, y + h - cornerSize);

                // Top-right
                sr.line(x + w, y + h, x + w - cornerSize, y + h);
                sr.line(x + w, y + h, x + w, y + h - cornerSize);

                // Bottom-left
                sr.line(x, y, x + cornerSize, y);
                sr.line(x, y, x, y + cornerSize);

                // Bottom-right
                sr.line(x + w, y, x + w - cornerSize, y);
                sr.line(x + w, y, x + w, y + cornerSize);
            }

            Gdx.gl.glLineWidth(1);
            sr.end();
        }
    }

    public MenuScreen(VijithapuraGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // Load wallpaper
        try {
            backgroundTexture = new Texture(Gdx.files.internal("wallpaper.png"));
        } catch (Exception e) {
            Gdx.app.log("MenuScreen", "Wallpaper not found, using gradient");
        }

        // Create fonts
        titleFont = new BitmapFont();
        titleFont.getData().setScale(3.5f);
        titleFont.setColor(GOLD_SHINE);

        menuFont = new BitmapFont();
        menuFont.getData().setScale(1.8f);
        menuFont.setColor(PARCHMENT);

        layout = new GlyphLayout();

        // Initialize buttons
        float buttonWidth = 380;
        float buttonHeight = 75;
        float centerX = Constants.SCREEN_WIDTH / 2 - buttonWidth / 2;
        float startY = Constants.SCREEN_HEIGHT / 2 - 50;
        float spacing = 95;

        easyButton = new MenuButton(centerX, startY, buttonWidth, buttonHeight,
            "EASY", "Recommended for beginners", EASY_COLOR);

        normalButton = new MenuButton(centerX, startY - spacing, buttonWidth, buttonHeight,
            "NORMAL", "Balanced challenge", NORMAL_COLOR);

        hardButton = new MenuButton(centerX, startY - spacing * 2, buttonWidth, buttonHeight,
            "HARD", "For experienced commanders", HARD_COLOR);

        exitButton = new MenuButton(centerX, startY - spacing * 3 - 20, buttonWidth, buttonHeight,
            "EXIT", "Return to system", new Color(0.5f, 0.4f, 0.4f, 1f));

        // Initialize atmospheric particles
        for (int i = 0; i < dustX.length; i++) {
            dustX[i] = (float)(Math.random() * Constants.SCREEN_WIDTH);
            dustY[i] = (float)(Math.random() * Constants.SCREEN_HEIGHT);
            dustSpeed[i] = 15 + (float)(Math.random() * 35);
            dustSize[i] = 1 + (float)(Math.random() * 2);
        }

        Gdx.app.log("MenuScreen", "AoE3-style menu initialized");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        animationTimer += delta;

        // Update atmospheric particles
        for (int i = 0; i < dustX.length; i++) {
            dustY[i] -= dustSpeed[i] * delta;
            dustX[i] += (float)Math.sin(animationTimer + i) * 10 * delta;

            if (dustY[i] < 0) {
                dustY[i] = Constants.SCREEN_HEIGHT;
                dustX[i] = (float)(Math.random() * Constants.SCREEN_WIDTH);
            }
        }

        // Check mouse hover
        int mouseX = Gdx.input.getX();
        int mouseY = Constants.SCREEN_HEIGHT - Gdx.input.getY();

        hoveredButton = -1;
        if (easyButton.bounds.contains(mouseX, mouseY)) hoveredButton = 0;
        else if (normalButton.bounds.contains(mouseX, mouseY)) hoveredButton = 1;
        else if (hardButton.bounds.contains(mouseX, mouseY)) hoveredButton = 2;
        else if (exitButton.bounds.contains(mouseX, mouseY)) hoveredButton = 3;

        // Handle clicks
        if (Gdx.input.justTouched()) {
            if (hoveredButton == 0) startGame(GameScreen.GameDifficulty.EASY);
            else if (hoveredButton == 1) startGame(GameScreen.GameDifficulty.NORMAL);
            else if (hoveredButton == 2) startGame(GameScreen.GameDifficulty.HARD);
            else if (hoveredButton == 3) Gdx.app.exit();
        }

        // Keyboard shortcuts
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) startGame(GameScreen.GameDifficulty.EASY);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) startGame(GameScreen.GameDifficulty.NORMAL);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) startGame(GameScreen.GameDifficulty.HARD);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();

        // Transition effect
        if (transitioning) {
            transitionAlpha += delta * 2;
            if (transitionAlpha >= 1.0f) {
                transitionAlpha = 1.0f;
            }
        }
    }

    private void draw() {
        // Clear with dark medieval color
        Gdx.gl.glClearColor(0.08f, 0.06f, 0.04f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw background image with vignette
        if (backgroundTexture != null) {
            game.batch.begin();
            // Show wallpaper at full brightness to display the title in the image
            game.batch.setColor(1f, 1f, 1f, 1f);
            game.batch.draw(backgroundTexture, 0, 0,
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
            game.batch.end();

            // Subtle vignette overlay (lighter than before)
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glEnable(GL20.GL_BLEND);

            // Very subtle dark edges to add depth without covering wallpaper content
            float vignetteSize = 150;
            game.shapeRenderer.setColor(0, 0, 0, 0.2f);
            game.shapeRenderer.rect(0, 0, Constants.SCREEN_WIDTH, vignetteSize);
            game.shapeRenderer.rect(0, Constants.SCREEN_HEIGHT - vignetteSize,
                Constants.SCREEN_WIDTH, vignetteSize);
            game.shapeRenderer.rect(0, 0, vignetteSize, Constants.SCREEN_HEIGHT);
            game.shapeRenderer.rect(Constants.SCREEN_WIDTH - vignetteSize, 0,
                vignetteSize, Constants.SCREEN_HEIGHT);

            game.shapeRenderer.end();
        } else {
            // Fallback gradient background
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (int i = 0; i < Constants.SCREEN_HEIGHT; i += 5) {
                float t = (float)i / Constants.SCREEN_HEIGHT;
                float r = 0.08f + t * 0.05f;
                float g = 0.06f + t * 0.04f;
                float b = 0.04f + t * 0.03f;
                game.shapeRenderer.setColor(r, g, b, 1);
                game.shapeRenderer.rect(0, i, Constants.SCREEN_WIDTH, 5);
            }

            game.shapeRenderer.end();
        }

        // Draw atmospheric particles (reduced opacity to not interfere with wallpaper)
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < dustX.length; i++) {
            float alpha = 0.05f + (float)Math.sin(animationTimer * 2 + i) * 0.03f;
            game.shapeRenderer.setColor(GOLD_SHINE.r, GOLD_SHINE.g, GOLD_SHINE.b, alpha);
            game.shapeRenderer.circle(dustX[i], dustY[i], dustSize[i]);
        }
        game.shapeRenderer.end();

        // Don't draw title panel - wallpaper already has the game title
        
        // Draw menu buttons
        float pulse = (float)Math.sin(animationTimer * 3) * 0.3f + 0.7f;

        easyButton.render(game.shapeRenderer, hoveredButton == 0, pulse);
        normalButton.render(game.shapeRenderer, hoveredButton == 1, pulse);
        hardButton.render(game.shapeRenderer, hoveredButton == 2, pulse);
        exitButton.render(game.shapeRenderer, hoveredButton == 3, pulse);

        // Draw button text
        game.batch.begin();

        drawButtonText(easyButton, hoveredButton == 0, pulse);
        drawButtonText(normalButton, hoveredButton == 1, pulse);
        drawButtonText(hardButton, hoveredButton == 2, pulse);
        drawButtonText(exitButton, hoveredButton == 3, pulse);

        // Draw footer instructions
        menuFont.getData().setScale(0.9f);
        menuFont.setColor(PARCHMENT.r, PARCHMENT.g, PARCHMENT.b, 0.7f);
        String instructions = "Use [1-3] keys or click to select difficulty • [ESC] to exit";
        layout.setText(menuFont, instructions);
        menuFont.draw(game.batch, instructions,
            Constants.SCREEN_WIDTH/2 - layout.width/2, 50);

        game.batch.end();

        // Transition overlay
        if (transitioning) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(0, 0, 0, transitionAlpha);
            game.shapeRenderer.rect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
            game.shapeRenderer.end();
        }
    }

    private void drawTitlePanel() {
        float panelWidth = 700;
        float panelHeight = 140;
        float panelX = Constants.SCREEN_WIDTH / 2 - panelWidth / 2;
        float panelY = Constants.SCREEN_HEIGHT - 200;

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Panel shadow
        game.shapeRenderer.setColor(0, 0, 0, 0.6f);
        game.shapeRenderer.rect(panelX + 6, panelY - 6, panelWidth, panelHeight);

        // Panel background (wood)
        game.shapeRenderer.setColor(WOOD_DARK);
        game.shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);

        // Wood grain
        game.shapeRenderer.setColor(WOOD_MEDIUM.r, WOOD_MEDIUM.g, WOOD_MEDIUM.b, 0.3f);
        for (int i = 0; i < panelHeight; i += 8) {
            game.shapeRenderer.rect(panelX, panelY + i, panelWidth, 2);
        }

        game.shapeRenderer.end();

        // Ornate border
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        Gdx.gl.glLineWidth(4);
        game.shapeRenderer.setColor(GOLD_SHINE);
        game.shapeRenderer.rect(panelX, panelY, panelWidth, panelHeight);

        Gdx.gl.glLineWidth(2);
        game.shapeRenderer.setColor(BRONZE);
        game.shapeRenderer.rect(panelX + 5, panelY + 5, panelWidth - 10, panelHeight - 10);

        // Decorative corners
        float cornerSize = 25;
        Gdx.gl.glLineWidth(3);
        game.shapeRenderer.setColor(GOLD_SHINE);

        // All four corners with ornate lines
        game.shapeRenderer.line(panelX, panelY + panelHeight,
            panelX + cornerSize, panelY + panelHeight);
        game.shapeRenderer.line(panelX, panelY + panelHeight,
            panelX, panelY + panelHeight - cornerSize);

        game.shapeRenderer.line(panelX + panelWidth, panelY + panelHeight,
            panelX + panelWidth - cornerSize, panelY + panelHeight);
        game.shapeRenderer.line(panelX + panelWidth, panelY + panelHeight,
            panelX + panelWidth, panelY + panelHeight - cornerSize);

        game.shapeRenderer.line(panelX, panelY, panelX + cornerSize, panelY);
        game.shapeRenderer.line(panelX, panelY, panelX, panelY + cornerSize);

        game.shapeRenderer.line(panelX + panelWidth, panelY,
            panelX + panelWidth - cornerSize, panelY);
        game.shapeRenderer.line(panelX + panelWidth, panelY,
            panelX + panelWidth, panelY + cornerSize);

        Gdx.gl.glLineWidth(1);
        game.shapeRenderer.end();

        // Title text
        game.batch.begin();

        titleFont.getData().setScale(3.5f);

        // Shadow
        titleFont.setColor(0, 0, 0, 0.8f);
        String title = "VIJITHAPURA SIEGE";
        layout.setText(titleFont, title);
        titleFont.draw(game.batch, title,
            panelX + panelWidth/2 - layout.width/2 + 3,
            panelY + panelHeight/2 + layout.height/2 - 3);

        // Glowing title
        float glow = (float)Math.sin(animationTimer * 2) * 0.15f + 0.85f;
        titleFont.setColor(GOLD_SHINE.r * glow, GOLD_SHINE.g * glow,
            GOLD_SHINE.b * glow, 1f);
        titleFont.draw(game.batch, title,
            panelX + panelWidth/2 - layout.width/2,
            panelY + panelHeight/2 + layout.height/2);

        // Subtitle
        menuFont.getData().setScale(1.1f);
        menuFont.setColor(PARCHMENT);
        String subtitle = "⚔ A Medieval RTS Adventure ⚔";
        layout.setText(menuFont, subtitle);
        menuFont.draw(game.batch, subtitle,
            panelX + panelWidth/2 - layout.width/2,
            panelY + 30);

        game.batch.end();
    }

    private void drawButtonText(MenuButton button, boolean isHovered, float pulse) {
        float x = button.bounds.x + button.bounds.width / 2;
        float y = button.bounds.y + button.bounds.height / 2;

        // Button label
        menuFont.getData().setScale(isHovered ? 2.0f * pulse : 1.8f);

        // Text shadow
        menuFont.setColor(0, 0, 0, 0.7f);
        layout.setText(menuFont, button.label);
        menuFont.draw(game.batch, button.label,
            x - layout.width/2 + 2, y + 12);

        // Main text
        Color textColor = isHovered ? GOLD_SHINE : PARCHMENT;
        menuFont.setColor(textColor);
        menuFont.draw(game.batch, button.label,
            x - layout.width/2, y + 14);

        // Description
        menuFont.getData().setScale(0.75f);
        menuFont.setColor(PARCHMENT.r, PARCHMENT.g, PARCHMENT.b, 0.8f);
        layout.setText(menuFont, button.description);
        menuFont.draw(game.batch, button.description,
            x - layout.width/2, y - 12);
    }

    private void startGame(GameScreen.GameDifficulty difficulty) {
        if (transitioning) return;
        transitioning = true;

        // Wait for transition to complete
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new GameScreen(game, difficulty));
                    dispose();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (titleFont != null) titleFont.dispose();
        if (menuFont != null) menuFont.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
