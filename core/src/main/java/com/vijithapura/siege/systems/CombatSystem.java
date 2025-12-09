package com.vijithapura.siege.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.Kandula;
import com.vijithapura.siege.entities.Unit;
import com.vijithapura.siege.dsa.SearchAlgorithms;

import java.util.ArrayList;

/**
 * Handles all combat logic
 */
public class CombatSystem {

    /**
     * Process combat between units and gates
     */
    public static void processCombat(ArrayList<Unit> playerUnits, ArrayList<Unit> enemyUnits,
                                     ArrayList<Gate> gates, float delta) {
        // Player units attack enemies and gates
        for (Unit player : playerUnits) {
            if (!player.isAlive()) continue;

            // Check if near any gate
            for (Gate gate : gates) {
                if (!gate.isDestroyed() && gate.isNearby(player.getPosition(), player.getAttackRange())) {
                    if (player.canAttack()) {
                        float damage = player.getAttackDamage();

                        // Kandula does extra damage when charging
                        if (player instanceof Kandula) {
                            Kandula elephant = (Kandula) player;
                            damage = elephant.getChargeDamage();
                        }

                        gate.takeDamage(damage);
                        player.attack(null); // Reset attack timer
                        Gdx.app.log("CombatSystem", player.getName() + " attacked " +
                            gate.getPosition() + " gate!");
                    }
                }
            }

            // Find and attack nearest enemy
            Unit nearestEnemy = SearchAlgorithms.findNearestEnemy(enemyUnits, player.getPosition());
            if (nearestEnemy != null) {
                float distance = Vector2.dst(
                    player.getPosition().x, player.getPosition().y,
                    nearestEnemy.getPosition().x, nearestEnemy.getPosition().y
                );

                if (distance <= player.getAttackRange() && player.canAttack()) {
                    player.attack(nearestEnemy);
                    Gdx.app.log("CombatSystem", player.getName() + " attacked " + nearestEnemy.getName());
                }
            }
        }

        // Enemies attack player units
        for (Unit enemy : enemyUnits) {
            if (!enemy.isAlive()) continue;

            Unit nearestPlayer = SearchAlgorithms.findNearestEnemy(playerUnits, enemy.getPosition());
            if (nearestPlayer != null) {
                float distance = Vector2.dst(
                    enemy.getPosition().x, enemy.getPosition().y,
                    nearestPlayer.getPosition().x, nearestPlayer.getPosition().y
                );

                if (distance <= enemy.getAttackRange() && enemy.canAttack()) {
                    enemy.attack(nearestPlayer);
                    Gdx.app.log("CombatSystem", enemy.getName() + " attacked " + nearestPlayer.getName());
                }
            }
        }
    }

    /**
     * Check victory conditions
     */
    public static boolean checkVictory(ArrayList<Gate> gates) {
        for (Gate gate : gates) {
            if (!gate.isDestroyed()) {
                return false;
            }
        }
        return true; // All gates destroyed = Victory!
    }

    /**
     * Check defeat conditions
     */
    public static boolean checkDefeat(ArrayList<Unit> playerUnits) {
        for (Unit unit : playerUnits) {
            if (unit.isAlive()) {
                return false;
            }
        }
        return true; // All units dead = Defeat
    }
}
