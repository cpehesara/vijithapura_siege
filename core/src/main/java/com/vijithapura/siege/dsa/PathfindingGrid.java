package com.vijithapura.siege.dsa;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Grid-based pathfinding system using A* algorithm
 * Supports obstacle detection and efficient path calculation
 */
public class PathfindingGrid {
    private int width;
    private int height;
    private int cellSize;
    private int gridWidth;
    private int gridHeight;
    private boolean[][] obstacles;

    /**
     * Node class for A* pathfinding
     */
    private static class Node {
        int x, y;
        float gCost; // Distance from start
        float hCost; // Heuristic distance to end
        float fCost; // Total cost (g + h)
        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.gCost = 0;
            this.hCost = 0;
            this.fCost = 0;
            this.parent = null;
        }

        public void calculateFCost() {
            this.fCost = this.gCost + this.hCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    /**
     * Constructor
     * @param width World width in pixels
     * @param height World height in pixels
     * @param cellSize Size of each grid cell in pixels
     */
    public PathfindingGrid(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.gridWidth = (int) Math.ceil((float) width / cellSize);
        this.gridHeight = (int) Math.ceil((float) height / cellSize);

        obstacles = new boolean[gridWidth][gridHeight];

        // Initialize all cells as walkable
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                obstacles[x][y] = false;
            }
        }
    }

    /**
     * Set a cell as obstacle or walkable
     */
    public void setObstacle(int gridX, int gridY, boolean isObstacle) {
        if (isValidGridPosition(gridX, gridY)) {
            obstacles[gridX][gridY] = isObstacle;
        }
    }

    /**
     * Set an area as obstacle
     */
    public void setObstacleArea(float worldX, float worldY, float width, float height, boolean isObstacle) {
        int startX = worldToGridX(worldX);
        int startY = worldToGridY(worldY);
        int endX = worldToGridX(worldX + width);
        int endY = worldToGridY(worldY + height);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                setObstacle(x, y, isObstacle);
            }
        }
    }

    /**
     * Check if a grid position is walkable
     */
    public boolean isWalkable(int gridX, int gridY) {
        if (!isValidGridPosition(gridX, gridY)) {
            return false;
        }
        return !obstacles[gridX][gridY];
    }

    /**
     * Check if a world position is walkable
     */
    public boolean isWalkableWorld(float worldX, float worldY) {
        int gridX = worldToGridX(worldX);
        int gridY = worldToGridY(worldY);
        return isWalkable(gridX, gridY);
    }

    /**
     * Find path using A* algorithm
     * @return List of waypoints in world coordinates, or null if no path found
     */
    public ArrayList<Vector2> findPath(float startX, float startY, float endX, float endY) {
        int startGridX = worldToGridX(startX);
        int startGridY = worldToGridY(startY);
        int endGridX = worldToGridX(endX);
        int endGridY = worldToGridY(endY);

        // Check if start and end are valid
        if (!isValidGridPosition(startGridX, startGridY) || !isValidGridPosition(endGridX, endGridY)) {
            return null;
        }

        // If end is not walkable, find nearest walkable cell
        if (!isWalkable(endGridX, endGridY)) {
            Vector2 nearest = findNearestWalkable(endGridX, endGridY);
            if (nearest == null) return null;
            endGridX = (int) nearest.x;
            endGridY = (int) nearest.y;
        }

        Node startNode = new Node(startGridX, startGridY);
        Node endNode = new Node(endGridX, endGridY);

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        HashSet<Node> closedSet = new HashSet<>();

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            closedSet.add(currentNode);

            // Check if we reached the goal
            if (currentNode.equals(endNode)) {
                return retracePath(startNode, currentNode);
            }

            // Check all neighbors
            for (Node neighbor : getNeighbors(currentNode)) {
                if (!isWalkable(neighbor.x, neighbor.y) || closedSet.contains(neighbor)) {
                    continue;
                }

                float newGCost = currentNode.gCost + getDistance(currentNode, neighbor);

                boolean inOpenSet = openSet.contains(neighbor);

                if (newGCost < neighbor.gCost || !inOpenSet) {
                    neighbor.gCost = newGCost;
                    neighbor.hCost = getDistance(neighbor, endNode);
                    neighbor.calculateFCost();
                    neighbor.parent = currentNode;

                    if (!inOpenSet) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // No path found
        return null;
    }

    /**
     * Find nearest walkable cell to the given position
     */
    private Vector2 findNearestWalkable(int gridX, int gridY) {
        int maxSearchRadius = 10;

        for (int radius = 1; radius <= maxSearchRadius; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (Math.abs(dx) == radius || Math.abs(dy) == radius) {
                        int checkX = gridX + dx;
                        int checkY = gridY + dy;
                        if (isWalkable(checkX, checkY)) {
                            return new Vector2(checkX, checkY);
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Get all valid neighbors of a node (8-directional)
     */
    private ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int checkX = node.x + dx;
                int checkY = node.y + dy;

                if (isValidGridPosition(checkX, checkY)) {
                    neighbors.add(new Node(checkX, checkY));
                }
            }
        }

        return neighbors;
    }

    /**
     * Retrace path from end to start
     */
    private ArrayList<Vector2> retracePath(Node startNode, Node endNode) {
        ArrayList<Vector2> path = new ArrayList<>();
        Node currentNode = endNode;

        while (currentNode != null && !currentNode.equals(startNode)) {
            path.add(gridToWorld(currentNode.x, currentNode.y));
            currentNode = currentNode.parent;
        }

        Collections.reverse(path);

        // Simplify path by removing unnecessary waypoints
        return simplifyPath(path);
    }

    /**
     * Simplify path by removing collinear points
     */
    private ArrayList<Vector2> simplifyPath(ArrayList<Vector2> path) {
        if (path.size() <= 2) return path;

        ArrayList<Vector2> simplified = new ArrayList<>();
        simplified.add(path.get(0));

        for (int i = 1; i < path.size() - 1; i++) {
            Vector2 prev = path.get(i - 1);
            Vector2 current = path.get(i);
            Vector2 next = path.get(i + 1);

            // Check if current point is necessary
            Vector2 dir1 = new Vector2(current.x - prev.x, current.y - prev.y).nor();
            Vector2 dir2 = new Vector2(next.x - current.x, next.y - current.y).nor();

            // If direction changes significantly, keep the point
            if (dir1.dot(dir2) < 0.9f) {
                simplified.add(current);
            }
        }

        simplified.add(path.get(path.size() - 1));
        return simplified;
    }

    /**
     * Calculate heuristic distance between two nodes (Manhattan distance)
     */
    private float getDistance(Node a, Node b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);

        // Diagonal distance
        if (dx > dy) {
            return 14 * dy + 10 * (dx - dy);
        }
        return 14 * dx + 10 * (dy - dx);
    }

    /**
     * Convert world coordinates to grid coordinates
     */
    public int worldToGridX(float worldX) {
        return (int) (worldX / cellSize);
    }

    public int worldToGridY(float worldY) {
        return (int) (worldY / cellSize);
    }

    /**
     * Convert grid coordinates to world coordinates (center of cell)
     */
    public Vector2 gridToWorld(int gridX, int gridY) {
        float worldX = gridX * cellSize + cellSize / 2f;
        float worldY = gridY * cellSize + cellSize / 2f;
        return new Vector2(worldX, worldY);
    }

    /**
     * Check if grid position is valid
     */
    private boolean isValidGridPosition(int gridX, int gridY) {
        return gridX >= 0 && gridX < gridWidth && gridY >= 0 && gridY < gridHeight;
    }

    /**
     * Clear all obstacles
     */
    public void clearObstacles() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                obstacles[x][y] = false;
            }
        }
    }

    /**
     * Get grid dimensions
     */
    public int getGridWidth() { return gridWidth; }
    public int getGridHeight() { return gridHeight; }
    public int getCellSize() { return cellSize; }

    /**
     * Debug: Print grid to console
     */
    public void printGrid() {
        System.out.println("PathfindingGrid [" + gridWidth + "x" + gridHeight + "]");
        for (int y = gridHeight - 1; y >= 0; y--) {
            for (int x = 0; x < gridWidth; x++) {
                System.out.print(obstacles[x][y] ? "X " : ". ");
            }
            System.out.println();
        }
    }
}
