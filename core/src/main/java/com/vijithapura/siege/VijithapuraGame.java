package com.vijithapura.siege;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vijithapura.siege.screens.MenuScreen;
import com.vijithapura.siege.utils.TextureManager;

public class VijithapuraGame extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public TextureManager textureManager;

    @Override
    public void create() {
        // --- SAFETY BLOCK START ---
        try {
            Gdx.app.log("VijithapuraGame", "--- GAME STARTING ---");

            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);

            font = new BitmapFont();
            font.getData().setScale(1.5f);

            // Initialize Asset Manager
            textureManager = new TextureManager();

            Gdx.app.log("VijithapuraGame", "Assets initialized. Loading MenuScreen...");

            // Load the main menu first
            this.setScreen(new MenuScreen(this));

        } catch (Exception e) {
            // THIS WILL PRINT THE REAL ERROR IF THE GAME CRASHES
            Gdx.app.error("FATAL", "Game crashed during startup:", e);
            e.printStackTrace(); // Prints to standard error
        }
        // --- SAFETY BLOCK END ---
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        try {
            if (batch != null) batch.dispose();
            if (shapeRenderer != null) shapeRenderer.dispose();
            if (font != null) font.dispose();
            if (textureManager != null) textureManager.dispose();
        } catch (Exception e) {
            Gdx.app.error("VijithapuraGame", "Error disposing resources", e);
        }
    }
}
