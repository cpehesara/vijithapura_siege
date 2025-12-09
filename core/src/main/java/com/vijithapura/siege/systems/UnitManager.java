package com.vijithapura.siege.systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.effects.VisualEffectsManager;
import com.vijithapura.siege.entities.Archer;
import com.vijithapura.siege.entities.Cavalry;
import com.vijithapura.siege.entities.Champion;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.Projectile;
import com.vijithapura.siege.entities.SiegeRam;
import com.vijithapura.siege.entities.Soldier;
import com.vijithapura.siege.entities.Unit;
import com.vijithapura.siege.entities.Worker;

public class UnitManager {
    private ArrayList<Unit> playerUnits;
    private ArrayList<Unit> enemyUnits;
    private ArrayList<Projectile> projectiles;

    public UnitManager() {
        playerUnits = new ArrayList<>();
        enemyUnits = new ArrayList<>();
        projectiles = new ArrayList<>();
    }

    public void spawnStartingUnits() {
        for (int i = 0; i < 5; i++) {
            Worker w = new Worker(100 + i * 50, 150);
            w.setSprite("worker");
            playerUnits.add(w);
        }

        for (int i = 0; i < 8; i++) {
            Soldier s = new Soldier(100 + i * 55, 220, "Soldier " + (i+1));
            s.setSprite("soldier");
            playerUnits.add(s);
        }
    }

    public void update(float delta, ArrayList<Gate> gates, VisualEffectsManager effects) {
        // Update all units
        playerUnits.forEach(u -> u.update(delta));
        enemyUnits.forEach(u -> u.update(delta));

        // Update projectiles
        projectiles.removeIf(p -> {
            p.update(delta);
            return !p.isActive();
        });

        // Process combat
        CombatSystem.processCombat(playerUnits, enemyUnits, gates, delta);
        EnemyAI.updateEnemies(enemyUnits, playerUnits, gates, delta);

        // Remove dead units with effects
        playerUnits.removeIf(u -> {
            if (!u.isAlive()) {
                effects.createDeathExplosion(u.getPosition());
                return true;
            }
            return false;
        });

        enemyUnits.removeIf(u -> {
            if (!u.isAlive()) {
                effects.createDeathExplosion(u.getPosition());
                return true;
            }
            return false;
        });
    }

    public void queueUnit(Unit.UnitType type, Vector2 spawnPos) {
        // Simplified - instantly spawn for now
        Unit unit = createUnit(type, spawnPos.x, spawnPos.y);
        if (unit != null) playerUnits.add(unit);
    }

    private Unit createUnit(Unit.UnitType type, float x, float y) {
        switch (type) {
            case WORKER: return new Worker(x, y);
            case SOLDIER: return new Soldier(x, y, "Soldier");
            case ARCHER: return new Archer(x, y, "Archer");
            case CAVALRY: return new Cavalry(x, y, "Cavalry");
            case CHAMPION: return new Champion(x, y, Champion.ChampionType.NANDIMITHRA);
            case SIEGE_RAM: return new SiegeRam(x, y);
            default: return null;
        }
    }

    public ArrayList<Unit> getPlayerUnits() { return playerUnits; }
    public ArrayList<Unit> getEnemyUnits() { return enemyUnits; }
    public ArrayList<Projectile> getProjectiles() { return projectiles; }
}
