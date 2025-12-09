package com.vijithapura.siege.systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.VijithapuraGame;
import com.vijithapura.siege.entities.Building;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.Projectile;
import com.vijithapura.siege.entities.ResourceNode;
import com.vijithapura.siege.entities.Unit;
import com.vijithapura.siege.utils.Constants;

public class WorldRenderer {

    public void render(VijithapuraGame game, OrthographicCamera camera,
                       ArrayList<Unit> playerUnits, ArrayList<Unit> enemyUnits,
                       ArrayList<Building> buildings, ArrayList<Gate> gates,
                       ArrayList<ResourceNode> resourceNodes, ArrayList<Projectile> projectiles,
                       boolean isSelecting, Vector2 selectionStart, Vector2 selectionEnd) {

        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        // 1. Background
        renderBackground(game);

        // 2. Fortress
        renderFortress(game);

        // 3. Resource nodes
        renderResourceNodes(game, resourceNodes);

        // 4. Buildings
        renderBuildings(game, buildings);

        // 5. Gates
        renderGates(game, gates);

        // 6. Units
        renderUnits(game, playerUnits);
        renderUnits(game, enemyUnits);

        // 7. Projectiles
        renderProjectiles(game, projectiles);

        // 8. Selection box
        if (isSelecting) {
            renderSelectionBox(game, selectionStart, selectionEnd);
        }
    }

    private void renderBackground(VijithapuraGame game) {
        game.batch.begin();
        if (game.textureManager.hasTexture("grass")) {
            Texture grass = game.textureManager.getTexture("grass");
            grass.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            
            // Draw base grass with slight color variation for depth
            game.batch.setColor(0.95f, 1.0f, 0.95f, 1.0f);
            game.batch.draw(grass, 0, 0, Constants.MAP_WIDTH, Constants.MAP_HEIGHT,
                0, 0, Constants.MAP_WIDTH/64, Constants.MAP_HEIGHT/64);
            
            // Add subtle darker grass overlay for variation
            game.batch.setColor(0.85f, 0.95f, 0.85f, 0.3f);
            game.batch.draw(grass, 0, 0, Constants.MAP_WIDTH, Constants.MAP_HEIGHT,
                0.5f, 0.5f, Constants.MAP_WIDTH/64 + 0.5f, Constants.MAP_HEIGHT/64 + 0.5f);
            
            // Reset color
            game.batch.setColor(1, 1, 1, 1);
        }
        game.batch.end();
    }

    private void renderFortress(VijithapuraGame game) {
        float x = Constants.FORTRESS_X - Constants.FORTRESS_SIZE / 2;
        float y = Constants.FORTRESS_Y - Constants.FORTRESS_SIZE / 2;

        game.batch.begin();
        if (game.textureManager.hasTexture("castle")) {
            Sprite castle = game.textureManager.getSprite("castle");
            castle.setPosition(x, y);
            castle.setSize(Constants.FORTRESS_SIZE, Constants.FORTRESS_SIZE);
            castle.draw(game.batch);
        }
        game.batch.end();

        if (!game.textureManager.hasTexture("castle")) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
            game.shapeRenderer.rect(x, y, Constants.FORTRESS_SIZE, Constants.FORTRESS_SIZE);
            game.shapeRenderer.end();
        }
    }

    private void renderResourceNodes(VijithapuraGame game, ArrayList<ResourceNode> nodes) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (ResourceNode node : nodes) {
            node.render(game.shapeRenderer, game.batch, game.textureManager);
        }
        game.shapeRenderer.end();
    }

    private void renderBuildings(VijithapuraGame game, ArrayList<Building> buildings) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Building b : buildings) {
            b.render(game.shapeRenderer, game.batch, game.textureManager);
        }
        game.shapeRenderer.end();
    }

    private void renderGates(VijithapuraGame game, ArrayList<Gate> gates) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Gate g : gates) {
            g.render(game.shapeRenderer, game.batch, game.textureManager);
        }
        game.shapeRenderer.end();
    }

    private void renderUnits(VijithapuraGame game, ArrayList<Unit> units) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Unit u : units) {
            if (u.isAlive()) {
                u.render(game.shapeRenderer, game.batch, game.textureManager);
            }
        }
        game.shapeRenderer.end();
    }

    private void renderProjectiles(VijithapuraGame game, ArrayList<Projectile> projectiles) {
        if (game.textureManager.hasTexture("arrow")) {
            game.batch.begin();
            Sprite arrow = game.textureManager.getSprite("arrow");
            for (Projectile p : projectiles) {
                arrow.setPosition(p.getX() - 5, p.getY() - 5);
                arrow.setSize(10, 20);
                arrow.draw(game.batch);
            }
            game.batch.end();
        } else {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(Color.WHITE);
            for (Projectile p : projectiles) {
                game.shapeRenderer.circle(p.getX(), p.getY(), 3);
            }
            game.shapeRenderer.end();
        }
    }

    private void renderSelectionBox(VijithapuraGame game, Vector2 start, Vector2 end) {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        game.shapeRenderer.setColor(Color.YELLOW);
        float x = Math.min(start.x, end.x);
        float y = Math.min(start.y, end.y);
        game.shapeRenderer.rect(x, y,
            Math.abs(end.x - start.x),
            Math.abs(end.y - start.y));
        Gdx.gl.glLineWidth(1);
        game.shapeRenderer.end();
    }
}
