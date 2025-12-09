package com.vijithapura.siege.systems;

import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.dsa.PathNode;

import java.util.ArrayList;
import java.util.Collections;

/**
 * DSA: RECURSION - Pathfinding with A* algorithm
 * Uses recursive backtracking to find path
 */
public class PathfindingSystem {

    /**
     * Find path using A* algorithm
     */
    public static ArrayList<Vector2> findPath(Vector2 start, Vector2 goal, ArrayList<Vector2> obstacles) {
        ArrayList<PathNode> openList = new ArrayList<>();
        ArrayList<PathNode> closedList = new ArrayList<>();

        PathNode startNode = new PathNode(start);
        PathNode goalNode = new PathNode(goal);

        startNode.gCost = 0;
        startNode.calculateHeuristic(goal);
        startNode.calculateFCost();

        openList.add(startNode);

        while (!openList.isEmpty()) {
            // Find node with lowest F cost
            PathNode current = getLowestFCost(openList);

            // Check if reached goal
            if (current.position.dst(goalNode.position) < 10) {
                return reconstructPath(current);
            }

            openList.remove(current);
            closedList.add(current);

            // Check neighbors (8 directions)
            ArrayList<PathNode> neighbors = getNeighbors(current, obstacles);

            for (PathNode neighbor : neighbors) {
                if (isInList(neighbor, closedList)) continue;

                float tentativeG = current.gCost + current.position.dst(neighbor.position);

                if (!isInList(neighbor, openList) || tentativeG < neighbor.gCost) {
                    neighbor.parent = current;
                    neighbor.gCost = tentativeG;
                    neighbor.calculateHeuristic(goal);
                    neighbor.calculateFCost();

                    if (!isInList(neighbor, openList)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // No path found - return direct line
        ArrayList<Vector2> directPath = new ArrayList<>();
        directPath.add(new Vector2(goal));
        return directPath;
    }

    /**
     * Recursive function to reconstruct path
     */
    private static ArrayList<Vector2> reconstructPath(PathNode node) {
        ArrayList<Vector2> path = new ArrayList<>();
        reconstructPathRecursive(node, path);
        Collections.reverse(path);
        return path;
    }

    /**
     * DSA: RECURSION - Recursive backtracking through parent nodes
     */
    private static void reconstructPathRecursive(PathNode node, ArrayList<Vector2> path) {
        if (node == null) return;

        path.add(new Vector2(node.position));

        // Recursive call
        if (node.parent != null) {
            reconstructPathRecursive(node.parent, path);
        }
    }

    /**
     * Get neighboring nodes
     */
    private static ArrayList<PathNode> getNeighbors(PathNode node, ArrayList<Vector2> obstacles) {
        ArrayList<PathNode> neighbors = new ArrayList<>();
        float step = 50; // Distance between nodes

        // 8 directions
        Vector2[] directions = {
            new Vector2(step, 0), new Vector2(-step, 0),
            new Vector2(0, step), new Vector2(0, -step),
            new Vector2(step, step), new Vector2(-step, -step),
            new Vector2(step, -step), new Vector2(-step, step)
        };

        for (Vector2 dir : directions) {
            Vector2 newPos = new Vector2(node.position).add(dir);

            // Check if position is blocked by obstacle
            boolean blocked = false;
            for (Vector2 obstacle : obstacles) {
                if (newPos.dst(obstacle) < 30) {
                    blocked = true;
                    break;
                }
            }

            if (!blocked) {
                neighbors.add(new PathNode(newPos));
            }
        }

        return neighbors;
    }

    /**
     * Find node with lowest F cost
     */
    private static PathNode getLowestFCost(ArrayList<PathNode> list) {
        PathNode lowest = list.get(0);
        for (PathNode node : list) {
            if (node.fCost < lowest.fCost) {
                lowest = node;
            }
        }
        return lowest;
    }

    /**
     * Check if node is in list
     */
    private static boolean isInList(PathNode node, ArrayList<PathNode> list) {
        for (PathNode n : list) {
            if (n.position.epsilonEquals(node.position, 0.1f)) {
                return true;
            }
        }
        return false;
    }
}
