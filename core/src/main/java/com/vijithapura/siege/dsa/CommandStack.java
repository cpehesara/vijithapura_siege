package com.vijithapura.siege.dsa;

import com.badlogic.gdx.math.Vector2;
import java.util.Stack;

/**
 * DSA: STACK - Command undo system
 * Uses Stack data structure for undo functionality
 */
public class CommandStack {
    private Stack<Command> commandHistory;
    private static final int MAX_HISTORY = 20;

    public CommandStack() {
        this.commandHistory = new Stack<>();
    }

    /**
     * Push command to stack
     */
    public void push(Command command) {
        commandHistory.push(command);

        // Limit stack size
        if (commandHistory.size() > MAX_HISTORY) {
            commandHistory.remove(0); // Remove oldest
        }

        System.out.println("[STACK] Command pushed: " + command.type + " (Stack size: " + commandHistory.size() + ")");
    }

    /**
     * Pop and execute undo
     */
    public Command pop() {
        if (!commandHistory.isEmpty()) {
            Command cmd = commandHistory.pop();
            System.out.println("[STACK] Command popped: " + cmd.type);
            return cmd;
        }
        return null;
    }

    /**
     * Peek at top command without removing
     */
    public Command peek() {
        if (!commandHistory.isEmpty()) {
            return commandHistory.peek();
        }
        return null;
    }

    /**
     * Check if stack is empty
     */
    public boolean isEmpty() {
        return commandHistory.isEmpty();
    }

    /**
     * Get stack size
     */
    public int size() {
        return commandHistory.size();
    }

    /**
     * Clear all commands
     */
    public void clear() {
        commandHistory.clear();
        System.out.println("[STACK] Command history cleared");
    }

    /**
     * Command class for storing actions
     */
    public static class Command {
        public enum Type {
            MOVE,
            ATTACK,
            BUILD,
            TRAIN
        }

        public Type type;
        public int unitId;
        public Vector2 position;
        public Vector2 previousPosition;
        public String data;

        public Command(Type type, int unitId, Vector2 position, Vector2 previousPosition) {
            this.type = type;
            this.unitId = unitId;
            this.position = new Vector2(position);
            this.previousPosition = new Vector2(previousPosition);
        }

        public Command(Type type, String data) {
            this.type = type;
            this.data = data;
        }
    }
}
