package com.vijithapura.siege.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Worker unit with FIXED resource gathering mechanics
 */
public class Worker extends Unit {

    public enum WorkerState {
        IDLE,
        MOVING_TO_RESOURCE,
        GATHERING,
        RETURNING_RESOURCES,
        BUILDING
    }

    private WorkerState state;
    private ResourceNode targetResourceNode;
    private Building targetBuilding;
    private float gatherTimer;
    private int carriedResources;
    private static final int MAX_CARRY = 10;
    private static final float GATHER_TIME = 2.0f;

    public Worker(float x, float y) {
        super(x, y, 80, 85, 5, new Color(0.7f, 0.6f, 0.4f, 1f), "Worker");
        this.state = WorkerState.IDLE;
        this.type = UnitType.WORKER;
        this.attackRange = 20f;
        this.carriedResources = 0;
        this.gatherTimer = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (!isAlive) return;

        switch (state) {
            case MOVING_TO_RESOURCE:
                if (!isMoving && targetResourceNode != null) {
                    float dist = position.dst(targetResourceNode.getPosition());
                    if (dist < 40f) {
                        setState(WorkerState.GATHERING);
                        Gdx.app.log("Worker", name + " started gathering");
                    }
                }
                break;

            case GATHERING:
                if (targetResourceNode != null && !targetResourceNode.isDepleted()) {
                    gatherTimer += delta;

                    if (gatherTimer >= GATHER_TIME) {
                        gatherTimer = 0;
                        // The ResourceManager will actually collect the resources
                        // Worker just needs to stay in GATHERING state near the node
                        Gdx.app.log("Worker", name + " is gathering...");
                    }
                } else {
                    setState(WorkerState.IDLE);
                }
                break;

            case BUILDING:
                if (targetBuilding != null) {
                    // Building logic here
                }
                break;
        }
    }

    public void gatherFrom(ResourceNode node) {
        this.targetResourceNode = node;
        this.setState(WorkerState.MOVING_TO_RESOURCE);

        // Move to the resource node
        Vector2 nodePos = node.getPosition();
        this.moveTo(nodePos.x, nodePos.y);

        Gdx.app.log("Worker", name + " moving to gather " + node.getType());
    }

    public void setState(WorkerState newState) {
        this.state = newState;

        if (newState == WorkerState.IDLE) {
            targetResourceNode = null;
            targetBuilding = null;
            carriedResources = 0;
            gatherTimer = 0;
        }
    }

    public WorkerState getState() {
        return state;
    }

    public ResourceNode getTargetResourceNode() {
        return targetResourceNode;
    }

    public int getCarriedResources() {
        return carriedResources;
    }
}
