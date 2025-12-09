package com.vijithapura.siege.systems;

import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.entities.*;
import com.vijithapura.siege.screens.GameScreen.GameDifficulty;
import com.vijithapura.siege.utils.Constants;
import java.util.ArrayList;

public class WaveManager {
    private int currentWave;
    private float waveTimer;
    private float timeBetweenWaves;
    private boolean waveInProgress;
    private GameDifficulty difficulty;

    public WaveManager(GameDifficulty difficulty) {
        this.difficulty = difficulty;
        this.currentWave = 0;
        this.waveTimer = 0;
        this.timeBetweenWaves = Constants.WAVE_INTERVAL;
        this.waveInProgress = false;
    }

    public void startFirstWave(UnitManager unitManager) {
        spawnWave(1, unitManager);
    }

    public void update(float delta, UnitManager unitManager, ResourceManager resourceManager,
                       ArrayList<Notification> notifications) {

        if (waveInProgress && unitManager.getEnemyUnits().stream().noneMatch(Unit::isAlive)) {
            waveInProgress = false;
            waveTimer = 0;
            int bonus = 100 * currentWave;
            resourceManager.addGold(bonus);
            notifications.add(new Notification(
                "Wave " + currentWave + " cleared! +" + bonus + " gold", Color.GREEN));
        }

        if (!waveInProgress) {
            waveTimer += delta;
            if (waveTimer >= timeBetweenWaves) {
                spawnWave(currentWave + 1, unitManager);
                waveTimer = 0;
            }
        }
    }

    private void spawnWave(int wave, UnitManager unitManager) {
        currentWave = wave;
        waveInProgress = true;

        double scaling = difficulty == GameDifficulty.HARD ? 1.5 : 1.3;
        int count = (int)(5 * Math.pow(scaling, wave));

        for (int i = 0; i < count; i++) {
            float angle = (float)(Math.PI * 2 * i / count);
            float x = Constants.FORTRESS_X + (float)Math.cos(angle) * 200;
            float y = Constants.FORTRESS_Y + (float)Math.sin(angle) * 200;

            Soldier enemy = new Soldier(x, y, "Invader");
            enemy.setTeamId(1);
            enemy.setColor(Color.RED);
            applyDifficultyModifiers(enemy);
            unitManager.getEnemyUnits().add(enemy);
        }
    }

    private void applyDifficultyModifiers(Unit unit) {
        unit.setMaxHealth(unit.getMaxHealth() * difficulty.enemyHealthMultiplier);
        unit.setHealth(unit.getMaxHealth());
        unit.setAttackDamage(unit.getAttackDamage() * difficulty.enemyDamageMultiplier);
    }

    public int getCurrentWave() { return currentWave; }
    public boolean isWaveInProgress() { return waveInProgress; }
    public float getTimeToNextWave() { return timeBetweenWaves - waveTimer; }
}
