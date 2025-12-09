package com.vijithapura.siege.dsa;

import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Unit;
import java.util.ArrayList;

/**
 * DSA: SEARCHING ALGORITHMS
 * Implements Linear Search and Binary Search
 */
public class SearchAlgorithms {

    /**
     * Linear Search - Find unit by name
     * Time Complexity: O(n)
     */
    public static Unit linearSearchByName(ArrayList<Unit> units, String name) {
        for (Unit unit : units) {
            if (unit.getName().equals(name)) {
                System.out.println("[LINEAR SEARCH] Found unit: " + name);
                return unit;
            }
        }
        System.out.println("[LINEAR SEARCH] Unit not found: " + name);
        return null;
    }

    /**
     * Binary Search - Find unit in sorted array by health
     * Time Complexity: O(log n)
     * NOTE: Array must be sorted first!
     */
    public static Unit binarySearchByHealth(ArrayList<Unit> sortedUnits, float targetHealth) {
        int left = 0;
        int right = sortedUnits.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            float midHealth = sortedUnits.get(mid).getHealth();

            if (Math.abs(midHealth - targetHealth) < 0.1f) {
                System.out.println("[BINARY SEARCH] Found unit with health: " + targetHealth);
                return sortedUnits.get(mid);
            }

            if (midHealth < targetHealth) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        System.out.println("[BINARY SEARCH] No unit found with exact health: " + targetHealth);
        return null;
    }

    /**
     * Find nearest enemy to a position
     * Uses linear search with distance calculation
     */
    public static Unit findNearestEnemy(ArrayList<Unit> enemies, Vector2 position) {
        Unit nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Unit enemy : enemies) {
            if (!enemy.isAlive()) continue;

            float distance = Vector2.dst(position.x, position.y,
                enemy.getPosition().x, enemy.getPosition().y);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }

    /**
     * Find all units within range
     */
    public static ArrayList<Unit> findUnitsInRange(ArrayList<Unit> units, Vector2 center, float range) {
        ArrayList<Unit> unitsInRange = new ArrayList<>();

        for (Unit unit : units) {
            if (!unit.isAlive()) continue;

            float distance = Vector2.dst(center.x, center.y,
                unit.getPosition().x, unit.getPosition().y);

            if (distance <= range) {
                unitsInRange.add(unit);
            }
        }

        System.out.println("[SEARCH] Found " + unitsInRange.size() + " units in range: " + range);
        return unitsInRange;
    }
}
