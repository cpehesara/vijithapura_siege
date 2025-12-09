package com.vijithapura.siege.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.HashMap;

/**
 * Manages all game textures and sprites.
 * Handles loading from specific asset folders.
 */
public class TextureManager {
    private HashMap<String, Texture> textures;
    private HashMap<String, Sprite> sprites;

    public TextureManager() {
        textures = new HashMap<>();
        sprites = new HashMap<>();
        loadAssets();
    }

    private void loadAssets() {
        Gdx.app.log("TextureManager", "Starting to load textures...");

        // --- Units ---
        loadTexture("worker", "units/worker.png");
        loadTexture("soldier", "units/soldier.png");
        loadTexture("archer", "units/archer.png"); // Make sure this exists
        loadTexture("cavalry", "units/cavalry.png"); // Make sure this exists
        loadTexture("champion", "units/champion.png");
        loadTexture("elephant", "units/elephant.png");
        loadTexture("ram", "units/ram.png"); // Make sure this exists

        // --- Buildings ---
        loadTexture("barracks", "buildings/barracks.png");
        loadTexture("command_center", "buildings/command_center.png");
        loadTexture("resource_depot", "buildings/resource_depot.png");
        loadTexture("workshop", "buildings/workshop.png"); // Optional: if you make one
        loadTexture("castle", "buildings/castle.png"); // New Castle Image
        loadTexture("tower", "buildings/tower.png");   // For Defensive Gates

        // --- Resources ---
        loadTexture("tree", "resources/tree.png");
        loadTexture("stone", "resources/stone.png");
        loadTexture("wood_pile", "resources/wood_pile.png");

        // --- Terrain ---
        loadTexture("grass", "terrain/grass_tile.png");

        // --- Effects ---
        // Create an 'effects' folder or put these in the root/units if preferred
        loadTexture("arrow", "effects/arrow.png");
        loadTexture("blood", "effects/blood.png");
        loadTexture("dust", "effects/dust.png");

        Gdx.app.log("TextureManager", "Asset loading complete. Loaded: " + textures.size() + " textures");
    }

    private void loadTexture(String key, String path) {
        try {
            if (Gdx.files.internal(path).exists()) {
                Texture texture = new Texture(Gdx.files.internal(path));
                // Set filters for pixel art or smooth scaling depending on your art style
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

                textures.put(key, texture);
                sprites.put(key, new Sprite(texture));
                Gdx.app.log("TextureManager", "✓ Loaded: " + key + " from " + path);
            } else {
                Gdx.app.log("TextureManager", "✗ Not found: " + path + " (will use shapes)");
            }
        } catch (Exception e) {
            Gdx.app.log("TextureManager", "✗ Error loading: " + path + " - " + e.getMessage());
        }
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }

    public Sprite getSprite(String key) {
        Sprite sprite = sprites.get(key);
        if (sprite != null) {
            return new Sprite(sprite); // Return copy
        }
        return null;
    }

    public boolean hasTexture(String key) {
        return textures.containsKey(key);
    }

    public void dispose() {
        Gdx.app.log("TextureManager", "Disposing textures...");
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
        sprites.clear();
    }
}
