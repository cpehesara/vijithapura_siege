package com.vijithapura.siege.dsa;

import com.vijithapura.siege.entities.Unit;
import java.util.LinkedList;
import java.util.Queue;

/**
 * DSA: QUEUE - Unit training queue
 * Uses Queue data structure for training units
 */
public class UnitQueue {
    private Queue<UnitOrder> trainingQueue;
    private UnitOrder currentTraining;
    private float trainingTimer;

    public UnitQueue() {
        this.trainingQueue = new LinkedList<>();
        this.currentTraining = null;
        this.trainingTimer = 0;
    }

    /**
     * Add unit to training queue
     */
    public void enqueue(String unitType, float trainingTime) {
        UnitOrder order = new UnitOrder(unitType, trainingTime);
        trainingQueue.add(order);
        System.out.println("[QUEUE] Added to queue: " + unitType + " (Queue size: " + trainingQueue.size() + ")");
    }

    /**
     * Update training progress
     */
    public Unit update(float delta) {
        // If nothing is training, start next in queue
        if (currentTraining == null && !trainingQueue.isEmpty()) {
            currentTraining = trainingQueue.poll(); // Dequeue
            trainingTimer = currentTraining.trainingTime;
            System.out.println("[QUEUE] Started training: " + currentTraining.unitType);
        }

        // Update current training
        if (currentTraining != null) {
            trainingTimer -= delta;

            if (trainingTimer <= 0) {
                // Training complete - create unit
                String completedType = currentTraining.unitType;
                currentTraining = null;
                trainingTimer = 0;

                System.out.println("[QUEUE] Training complete: " + completedType);
                return createUnit(completedType);
            }
        }

        return null;
    }

    /**
     * Create unit based on type
     */
    private Unit createUnit(String type) {
        // Return null for now - we'll implement unit creation later
        return null;
    }

    /**
     * Get training progress (0 to 1)
     */
    public float getTrainingProgress() {
        if (currentTraining == null) return 0;
        return 1.0f - (trainingTimer / currentTraining.trainingTime);
    }

    /**
     * Get current training unit type
     */
    public String getCurrentTraining() {
        return currentTraining != null ? currentTraining.unitType : null;
    }

    /**
     * Get queue size (including currently training unit)
     */
    public int getQueueSize() {
        return trainingQueue.size();
    }

    /**
     * Get total size including current training
     * This is the method that was missing - now GameScreen.testDSAClasses() will work
     */
    public int size() {
        int total = trainingQueue.size();
        if (currentTraining != null) {
            total++;
        }
        return total;
    }

    /**
     * Check if queue is empty
     */
    public boolean isEmpty() {
        return trainingQueue.isEmpty() && currentTraining == null;
    }

    /**
     * Clear the entire queue
     */
    public void clear() {
        trainingQueue.clear();
        currentTraining = null;
        trainingTimer = 0;
    }

    /**
     * Cancel current training
     */
    public void cancelCurrent() {
        if (currentTraining != null) {
            System.out.println("[QUEUE] Cancelled training: " + currentTraining.unitType);
            currentTraining = null;
            trainingTimer = 0;
        }
    }

    /**
     * Get remaining time for current training
     */
    public float getRemainingTime() {
        return currentTraining != null ? trainingTimer : 0;
    }

    /**
     * Peek at next unit in queue without removing it
     */
    public String peekNext() {
        UnitOrder next = trainingQueue.peek();
        return next != null ? next.unitType : null;
    }

    /**
     * Get all unit types in queue (for display purposes)
     */
    public String[] getQueuedUnits() {
        String[] units = new String[trainingQueue.size()];
        int i = 0;
        for (UnitOrder order : trainingQueue) {
            units[i++] = order.unitType;
        }
        return units;
    }

    /**
     * Inner class for unit orders
     */
    private static class UnitOrder {
        String unitType;
        float trainingTime;

        UnitOrder(String unitType, float trainingTime) {
            this.unitType = unitType;
            this.trainingTime = trainingTime;
        }
    }
}
