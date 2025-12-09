package com.vijithapura.siege.dsa;

import com.badlogic.gdx.math.Vector2;

/**
 * DSA: Node class for pathfinding
 * Used in recursive pathfinding algorithms
 */
public class PathNode {
    public Vector2 position;
    public PathNode parent;
    public float gCost; // Distance from start
    public float hCost; // Distance to target (heuristic)
    public float fCost; // Total cost (g + h)

    public PathNode(Vector2 position) {
        this.position = new Vector2(position);
        this.parent = null;
        this.gCost = 0;
        this.hCost = 0;
        this.fCost = 0;
    }

    public PathNode(float x, float y) {
        this(new Vector2(x, y));
    }

    /**
     * Calculate heuristic (Manhattan distance)
     */
    public void calculateHeuristic(Vector2 target) {
        this.hCost = Math.abs(position.x - target.x) + Math.abs(position.y - target.y);
    }

    /**
     * Calculate F cost
     */
    public void calculateFCost() {
        this.fCost = gCost + hCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PathNode) {
            PathNode other = (PathNode) obj;
            return position.epsilonEquals(other.position, 0.1f);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int)(position.x * 73 + position.y * 97);
    }
}
