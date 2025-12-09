package com.vijithapura.siege.systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Building;
import com.vijithapura.siege.entities.Gate;
import com.vijithapura.siege.entities.ResourceNode;
import com.vijithapura.siege.utils.Constants;

public class BuildingManager {
    private ArrayList<Building> buildings;
    private ArrayList<Gate> gates;
    private ArrayList<ResourceNode> resourceNodes;

    public BuildingManager() {
        buildings = new ArrayList<>();
        gates = new ArrayList<>();
        resourceNodes = new ArrayList<>();
    }

    public void initializeStartingBuildings() {
        buildings.add(new Building(Building.BuildingType.COMMAND_CENTER, 200, 200, 180, 180));
        buildings.add(new Building(Building.BuildingType.BARRACKS, 400, 200, 150, 150));
        buildings.add(new Building(Building.BuildingType.RESOURCE_DEPOT, 570, 200, 130, 130));

        initializeGates();
        initializeResourceNodes();
    }

    private void initializeGates() {
        float fortressLeft = Constants.FORTRESS_X - Constants.FORTRESS_SIZE / 2;
        float fortressBottom = Constants.FORTRESS_Y - Constants.FORTRESS_SIZE / 2;
        float wallThickness = 30;

        gates.add(new Gate(Gate.GatePosition.SOUTH,
            Constants.FORTRESS_X - Constants.GATE_WIDTH / 2,
            fortressBottom - 5,
            Constants.GATE_WIDTH, wallThickness + 10));

        gates.add(new Gate(Gate.GatePosition.NORTH,
            Constants.FORTRESS_X - Constants.GATE_WIDTH / 2,
            fortressBottom + Constants.FORTRESS_SIZE - wallThickness - 5,
            Constants.GATE_WIDTH, wallThickness + 10));

        gates.add(new Gate(Gate.GatePosition.WEST,
            fortressLeft - 5,
            Constants.FORTRESS_Y - Constants.GATE_WIDTH / 2,
            wallThickness + 10, Constants.GATE_WIDTH));

        gates.add(new Gate(Gate.GatePosition.EAST,
            fortressLeft + Constants.FORTRESS_SIZE - wallThickness - 5,
            Constants.FORTRESS_Y - Constants.GATE_WIDTH / 2,
            wallThickness + 10, Constants.GATE_WIDTH));
    }

    private void initializeResourceNodes() {
        // MASSIVE WOOD CLUSTERS - Northwest Forest
        for (int i = 0; i < 25; i++) {
            float x = 100 + (i % 5) * 60 + (float)(Math.random() * 30);
            float y = 400 + (i / 5) * 70 + (float)(Math.random() * 30);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 800 + (int)(Math.random() * 400)));
        }
        
        // Northeast Forest
        for (int i = 0; i < 30; i++) {
            float x = 2000 + (i % 6) * 65 + (float)(Math.random() * 40);
            float y = 300 + (i / 6) * 75 + (float)(Math.random() * 40);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 800 + (int)(Math.random() * 400)));
        }
        
        // Central Forest
        for (int i = 0; i < 20; i++) {
            float x = 800 + (i % 5) * 70 + (float)(Math.random() * 35);
            float y = 550 + (i / 5) * 80 + (float)(Math.random() * 35);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 800 + (int)(Math.random() * 400)));
        }
        
        // South Forest
        for (int i = 0; i < 35; i++) {
            float x = 900 + (i % 7) * 60 + (float)(Math.random() * 30);
            float y = 1700 + (i / 7) * 70 + (float)(Math.random() * 30);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 800 + (int)(Math.random() * 400)));
        }
        
        // East Forest
        for (int i = 0; i < 25; i++) {
            float x = 2400 + (i % 5) * 55 + (float)(Math.random() * 25);
            float y = 1000 + (i / 5) * 75 + (float)(Math.random() * 25);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 800 + (int)(Math.random() * 400)));
        }
        
        // Scattered trees throughout map
        for (int i = 0; i < 40; i++) {
            float x = 300 + (float)(Math.random() * 2400);
            float y = 300 + (float)(Math.random() * 1900);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.WOOD, x, y, 600 + (int)(Math.random() * 600)));
        }
        
        // STONE DEPOSITS - Northwest Quarry
        for (int i = 0; i < 15; i++) {
            float x = 500 + (i % 5) * 80 + (float)(Math.random() * 40);
            float y = 250 + (i / 5) * 90 + (float)(Math.random() * 40);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.STONE, x, y, 600 + (int)(Math.random() * 400)));
        }
        
        // Northeast Quarry
        for (int i = 0; i < 18; i++) {
            float x = 2100 + (i % 6) * 75 + (float)(Math.random() * 35);
            float y = 1800 + (i / 6) * 85 + (float)(Math.random() * 35);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.STONE, x, y, 600 + (int)(Math.random() * 400)));
        }
        
        // Southwest Quarry
        for (int i = 0; i < 12; i++) {
            float x = 300 + (i % 4) * 85 + (float)(Math.random() * 40);
            float y = 1900 + (i / 4) * 90 + (float)(Math.random() * 40);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.STONE, x, y, 600 + (int)(Math.random() * 400)));
        }
        
        // Central Mountain Range
        for (int i = 0; i < 20; i++) {
            float x = 1300 + (i % 5) * 90 + (float)(Math.random() * 45);
            float y = 1200 + (i / 5) * 95 + (float)(Math.random() * 45);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.STONE, x, y, 600 + (int)(Math.random() * 400)));
        }
        
        // Scattered stone formations
        for (int i = 0; i < 25; i++) {
            float x = 400 + (float)(Math.random() * 2200);
            float y = 400 + (float)(Math.random() * 1800);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.STONE, x, y, 500 + (int)(Math.random() * 500)));
        }
        
        // GOLD MINES - Rich deposits (fewer but valuable)
        // Northwest Gold Mine
        for (int i = 0; i < 8; i++) {
            float x = 1100 + (i % 4) * 50 + (float)(Math.random() * 20);
            float y = 250 + (i / 4) * 60 + (float)(Math.random() * 20);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.GOLD, x, y, 1000 + (int)(Math.random() * 500)));
        }
        
        // Northeast Gold Mine
        for (int i = 0; i < 10; i++) {
            float x = 2400 + (i % 5) * 45 + (float)(Math.random() * 20);
            float y = 1100 + (i / 5) * 55 + (float)(Math.random() * 20);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.GOLD, x, y, 1000 + (int)(Math.random() * 500)));
        }
        
        // Southwest Gold Mine
        for (int i = 0; i < 7; i++) {
            float x = 450 + (i % 3) * 55 + (float)(Math.random() * 25);
            float y = 1550 + (i / 3) * 65 + (float)(Math.random() * 25);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.GOLD, x, y, 1000 + (int)(Math.random() * 500)));
        }
        
        // Central Gold Mine (near fortress - strategic)
        for (int i = 0; i < 6; i++) {
            float x = 1700 + (i % 3) * 60 + (float)(Math.random() * 30);
            float y = 800 + (i / 3) * 70 + (float)(Math.random() * 30);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.GOLD, x, y, 1200 + (int)(Math.random() * 600)));
        }
        
        // Scattered gold veins
        for (int i = 0; i < 12; i++) {
            float x = 500 + (float)(Math.random() * 2000);
            float y = 500 + (float)(Math.random() * 1700);
            resourceNodes.add(new ResourceNode(ResourceNode.ResourceType.GOLD, x, y, 800 + (int)(Math.random() * 700)));
        }
    }

    public void update(float delta) {
        buildings.forEach(b -> b.update(delta));
        gates.forEach(g -> g.update(delta, new ArrayList<>(), new ArrayList<>()));
    }

    public Vector2 getSpawnPoint() {
        return new Vector2(250, 300);
    }

    public ArrayList<Building> getBuildings() { return buildings; }
    public ArrayList<Gate> getGates() { return gates; }
    public ArrayList<ResourceNode> getResourceNodes() { return resourceNodes; }
}
