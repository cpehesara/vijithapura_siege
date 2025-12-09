package com.vijithapura.siege.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.effects.VisualEffectsManager;
import com.vijithapura.siege.entities.*;
import java.util.ArrayList;

public class SelectionManager {
    private ArrayList<Unit> selectedUnits;
    private Vector2 selectionStart;
    private Vector2 selectionEnd;
    private boolean isSelecting;

    public SelectionManager() {
        selectedUnits = new ArrayList<>();
        selectionStart = new Vector2();
        selectionEnd = new Vector2();
        isSelecting = false;
    }

    public void update(float delta) {
        // Update selection state
    }

    public void handleLeftClick(float worldX, float worldY, ArrayList<Unit> playerUnits,
                                ArrayList<Building> buildings, VisualEffectsManager effects,
                                ArrayList<Notification> notifications) {

        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            clearSelection();
        }

        // Try to select a unit
        for (Unit unit : playerUnits) {
            if (unit == null || !unit.isAlive()) continue;

            float dist = Vector2.dst(worldX, worldY,
                unit.getPosition().x, unit.getPosition().y);

            if (dist < unit.getSize() * 2 + 10) {
                selectUnit(unit, effects);
                notifications.add(new Notification(unit.getName() + " selected", Color.YELLOW));
                return;
            }
        }
    }

    public void handleRightClick(float worldX, float worldY, ArrayList<Unit> enemyUnits,
                                 ArrayList<Gate> gates, ArrayList<ResourceNode> resourceNodes,
                                 ArrayList<Notification> notifications) {

        if (selectedUnits.isEmpty()) return;

        // Check for resource nodes
        for (ResourceNode node : resourceNodes) {
            if (node.isNearby(new Vector2(worldX, worldY), 30)) {
                assignWorkersToGather(node);
                notifications.add(new Notification("Gathering " + node.getType(), Color.CYAN));
                return;
            }
        }

        // Check for enemy units
        for (Unit enemy : enemyUnits) {
            if (enemy == null || !enemy.isAlive()) continue;
            float dist = Vector2.dst(worldX, worldY,
                enemy.getPosition().x, enemy.getPosition().y);
            if (dist < enemy.getSize() * 2) {
                attackTarget(enemy);
                notifications.add(new Notification("Attack " + enemy.getName(), Color.ORANGE));
                return;
            }
        }

        // Move command
        moveUnits(worldX, worldY);
        notifications.add(new Notification("Moving " + selectedUnits.size() + " units", Color.CYAN));
    }

    private void assignWorkersToGather(ResourceNode node) {
        for (Unit unit : selectedUnits) {
            if (unit instanceof Worker) {
                ((Worker) unit).gatherFrom(node);
            }
        }
    }

    private void attackTarget(Unit target) {
        for (Unit unit : selectedUnits) {
            unit.setCurrentTarget(target);
        }
    }

    private void moveUnits(float x, float y) {
        for (Unit unit : selectedUnits) {
            unit.moveTo(x, y);
        }
    }

    public void startSelection(Vector2 pos) {
        selectionStart.set(pos);
        selectionEnd.set(pos);
        isSelecting = true;
    }

    public void updateSelection(Vector2 pos) {
        selectionEnd.set(pos);
    }

    public void finishSelection(ArrayList<Unit> playerUnits, VisualEffectsManager effects) {
        if (!isSelecting) return;

        Rectangle box = new Rectangle(
            Math.min(selectionStart.x, selectionEnd.x),
            Math.min(selectionStart.y, selectionEnd.y),
            Math.abs(selectionEnd.x - selectionStart.x),
            Math.abs(selectionEnd.y - selectionStart.y)
        );

        if (box.width > 5 && box.height > 5) {
            for (Unit unit : playerUnits) {
                if (unit != null && unit.isAlive() &&
                    box.contains(unit.getPosition().x, unit.getPosition().y)) {
                    selectUnit(unit, effects);
                }
            }
        }

        isSelecting = false;
    }

    private void selectUnit(Unit unit, VisualEffectsManager effects) {
        unit.setSelected(true);
        if (!selectedUnits.contains(unit)) {
            selectedUnits.add(unit);
        }
        effects.createSelectionRing(unit);
    }

    private void clearSelection() {
        for (Unit unit : selectedUnits) {
            if (unit != null) unit.setSelected(false);
        }
        selectedUnits.clear();
    }

    public ArrayList<Unit> getSelectedUnits() { return selectedUnits; }
    public boolean isSelecting() { return isSelecting; }
    public Vector2 getSelectionStart() { return selectionStart; }
    public Vector2 getSelectionEnd() { return selectionEnd; }
}
