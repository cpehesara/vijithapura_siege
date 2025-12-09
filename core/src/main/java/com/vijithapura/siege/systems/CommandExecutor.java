package com.vijithapura.siege.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.*;
import java.util.ArrayList;

public class CommandExecutor {

    public static void attackNearestGate(ArrayList<Unit> selectedUnits,
                                         ArrayList<Gate> gates,
                                         ArrayList<Notification> notifications) {
        if (selectedUnits.isEmpty()) return;

        Gate nearest = findNearestGate(selectedUnits, gates);
        if (nearest != null) {
            for (Unit unit : selectedUnits) {
                unit.moveTo(nearest.getCenter().x, nearest.getCenter().y);
            }
            notifications.add(new Notification(
                "Attack " + nearest.getPosition() + " gate!", Color.ORANGE));
        }
    }

    public static void stopUnits(ArrayList<Unit> selectedUnits,
                                 ArrayList<Notification> notifications) {
        for (Unit unit : selectedUnits) {
            unit.moveTo(unit.getPosition().x, unit.getPosition().y);
        }
        if (!selectedUnits.isEmpty()) {
            notifications.add(new Notification("Units stopped", Color.CYAN));
        }
    }

    public static void holdPosition(ArrayList<Unit> selectedUnits,
                                    ArrayList<Notification> notifications) {
        for (Unit unit : selectedUnits) {
            unit.setStance(Unit.UnitStance.STAND_GROUND);
        }
        if (!selectedUnits.isEmpty()) {
            notifications.add(new Notification("Hold position!", Color.GREEN));
        }
    }

    public static void formationMove(ArrayList<Unit> selectedUnits,
                                     ArrayList<Notification> notifications) {
        if (selectedUnits.isEmpty()) return;

        Vector2 center = calculateCenter(selectedUnits);
        int cols = (int)Math.ceil(Math.sqrt(selectedUnits.size()));
        float spacing = 45f;

        for (int i = 0; i < selectedUnits.size(); i++) {
            Unit unit = selectedUnits.get(i);
            int row = i / cols;
            int col = i % cols;

            float offsetX = (col - cols/2) * spacing;
            float offsetY = (row - selectedUnits.size()/cols/2) * spacing;

            unit.moveTo(center.x + offsetX, center.y + offsetY);
        }

        notifications.add(new Notification(
            "Formation move - " + selectedUnits.size() + " units", Color.CYAN));
    }

    private static Gate findNearestGate(ArrayList<Unit> units, ArrayList<Gate> gates) {
        Vector2 center = calculateCenter(units);
        Gate nearest = null;
        float minDist = Float.MAX_VALUE;

        for (Gate gate : gates) {
            if (gate.isDestroyed()) continue;
            float dist = center.dst(gate.getCenter());
            if (dist < minDist) {
                minDist = dist;
                nearest = gate;
            }
        }

        return nearest;
    }

    private static Vector2 calculateCenter(ArrayList<Unit> units) {
        Vector2 center = new Vector2();
        for (Unit unit : units) {
            center.add(unit.getPosition());
        }
        return center.scl(1f / units.size());
    }
}
