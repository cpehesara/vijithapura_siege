package com.vijithapura.siege.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.vijithapura.siege.entities.*;
import java.util.ArrayList;

/**
 * Manages resource gathering and storage
 * Fixes the worker resource collection bug
 */
public class ResourceManager {
    private int gold;
    private int wood;
    private int stone;
    private int food;

    private float gatherTimer;
    private static final float GATHER_INTERVAL = 2.0f;

    private ArrayList<Notification> notifications;

    public ResourceManager(int startGold, int startWood, int startStone, int startFood) {
        this.gold = startGold;
        this.wood = startWood;
        this.stone = startStone;
        this.food = startFood;
        this.gatherTimer = 0;
        this.notifications = new ArrayList<>();
    }

    public void update(float delta, ArrayList<Unit> playerUnits, ArrayList<ResourceNode> resourceNodes) {
        gatherTimer += delta;

        if (gatherTimer >= GATHER_INTERVAL) {
            gatherTimer = 0;
            processResourceGathering(playerUnits, resourceNodes);
        }
    }

    private void processResourceGathering(ArrayList<Unit> playerUnits, ArrayList<ResourceNode> resourceNodes) {
        int woodGathered = 0;
        int stoneGathered = 0;

        for (Unit unit : playerUnits) {
            if (!(unit instanceof Worker) || !unit.isAlive()) continue;

            Worker worker = (Worker) unit;

            // Check if worker is gathering from a resource node
            if (worker.getState() == Worker.WorkerState.GATHERING) {
                ResourceNode targetNode = findNearestResourceNode(worker, resourceNodes);

                if (targetNode != null && !targetNode.isDepleted()) {
                    int gathered = targetNode.gather(10); // Gather 10 units

                    if (gathered > 0) {
                        if (targetNode.getType() == ResourceNode.ResourceType.WOOD) {
                            woodGathered += gathered;
                        } else if (targetNode.getType() == ResourceNode.ResourceType.STONE) {
                            stoneGathered += gathered;
                        }

                        Gdx.app.log("ResourceManager", worker.getName() + " gathered " +
                            gathered + " " + targetNode.getType());
                    }

                    // If node depleted, worker goes idle
                    if (targetNode.isDepleted()) {
                        worker.setState(Worker.WorkerState.IDLE);
                    }
                }
            }
        }

        // Add gathered resources
        if (woodGathered > 0) {
            addWood(woodGathered);
            Gdx.app.log("ResourceManager", "Total wood gathered this cycle: " + woodGathered);
        }
        if (stoneGathered > 0) {
            addStone(stoneGathered);
            Gdx.app.log("ResourceManager", "Total stone gathered this cycle: " + stoneGathered);
        }
    }

    private ResourceNode findNearestResourceNode(Worker worker, ArrayList<ResourceNode> nodes) {
        ResourceNode nearest = null;
        float minDist = 50f; // Gathering range

        for (ResourceNode node : nodes) {
            if (node.isDepleted()) continue;

            float dist = worker.getPosition().dst(node.getPosition());
            if (dist < minDist) {
                minDist = dist;
                nearest = node;
            }
        }

        return nearest;
    }

    // Resource modification methods
    public void addGold(int amount) {
        gold += amount;
        Gdx.app.log("ResourceManager", "Gold: " + gold + " (+" + amount + ")");
    }

    public void addWood(int amount) {
        wood += amount;
        Gdx.app.log("ResourceManager", "Wood: " + wood + " (+" + amount + ")");
    }

    public void addStone(int amount) {
        stone += amount;
        Gdx.app.log("ResourceManager", "Stone: " + stone + " (+" + amount + ")");
    }

    public void addFood(int amount) {
        food += amount;
        Gdx.app.log("ResourceManager", "Food: " + food + " (+" + amount + ")");
    }

    public boolean canAfford(int goldCost, int woodCost, int stoneCost, int foodCost) {
        return gold >= goldCost && wood >= woodCost &&
            stone >= stoneCost && food >= foodCost;
    }

    public void spend(int goldCost, int woodCost, int stoneCost, int foodCost) {
        gold -= goldCost;
        wood -= woodCost;
        stone -= stoneCost;
        food -= foodCost;

        Gdx.app.log("ResourceManager", "Spent - Gold: " + goldCost +
            ", Wood: " + woodCost + ", Stone: " + stoneCost + ", Food: " + foodCost);
    }

    // Getters
    public int getGold() { return gold; }
    public int getWood() { return wood; }
    public int getStone() { return stone; }
    public int getFood() { return food; }
}
