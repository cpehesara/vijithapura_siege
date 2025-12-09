package com.vijithapura.siege.systems;

import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.Unit;
import com.vijithapura.siege.dsa.SearchAlgorithms;

import java.util.ArrayList;

/**
 * Simple AI for enemy units
 */
public class EnemyAI {
    private static final float ATTACK_RANGE = 200f;
    private static final float DEFEND_RANGE = 300f;

    /**
     * Update enemy behavior
     */
    public static void updateEnemies(ArrayList<Unit> enemies, ArrayList<Unit> playerUnits,
                                     ArrayList<Gate> gates, float delta) {
        for (Unit enemy : enemies) {
            if (!enemy.isAlive()) continue;

            // Find nearest player unit
            Unit nearestPlayer = SearchAlgorithms.findNearestEnemy(playerUnits, enemy.getPosition());

            if (nearestPlayer != null) {
                float distance = Vector2.dst(
                    enemy.getPosition().x, enemy.getPosition().y,
                    nearestPlayer.getPosition().x, nearestPlayer.getPosition().y
                );

                // If player is within attack range, move towards them
                if (distance < ATTACK_RANGE) {
                    // Move towards player
                    if (distance > enemy.getAttackRange()) {
                        enemy.moveTo(nearestPlayer.getPosition().x, nearestPlayer.getPosition().y);
                    }
                }
                // Otherwise, guard nearest gate
                else {
                    Gate nearestGate = findNearestGate(enemy.getPosition(), gates);
                    if (nearestGate != null && !nearestGate.isDestroyed()) {
                        Vector2 gatePos = nearestGate.getCenter();
                        float gateDistance = Vector2.dst(
                            enemy.getPosition().x, enemy.getPosition().y,
                            gatePos.x, gatePos.y
                        );

                        // Stay near gate
                        if (gateDistance > DEFEND_RANGE) {
                            enemy.moveTo(gatePos.x, gatePos.y);
                        }
                    }
                }
            }
        }
    }

    /**
     * Find nearest gate to position
     */
    private static Gate findNearestGate(Vector2 position, ArrayList<Gate> gates) {
        Gate nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Gate gate : gates) {
            if (gate.isDestroyed()) continue;

            float distance = Vector2.dst(position.x, position.y,
                gate.getCenter().x, gate.getCenter().y);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = gate;
            }
        }

        return nearest;
    }
}
