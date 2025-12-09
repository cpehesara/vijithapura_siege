package com.vijithapura.siege.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Handles all input for the game
 */
public class InputHandler extends InputAdapter {
    private OrthographicCamera camera;
    private ClickListener clickListener;
    private Vector3 touchPos;

    // Drag handling
    private boolean isDragging;
    private Vector2 dragStart;
    private Vector2 lastDragPos;
    private static final float DRAG_THRESHOLD = 10f;

    public interface ClickListener {
        void onLeftClick(float worldX, float worldY);
        void onRightClick(float worldX, float worldY);
    }

    public InputHandler(OrthographicCamera camera) {
        this.camera = camera;
        this.touchPos = new Vector3();
        this.dragStart = new Vector2();
        this.lastDragPos = new Vector2();
        this.isDragging = false;
    }

    public void setClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPos = screenToWorld(screenX, screenY);
        dragStart.set(worldPos);
        lastDragPos.set(worldPos);
        isDragging = false;

        Gdx.app.log("InputHandler", "Touch down at screen: " + screenX + ", " + screenY +
            " -> world: " + worldPos.x + ", " + worldPos.y + " button: " + button);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        Gdx.app.log("InputHandler", "Touch up at screen: " + screenX + ", " + screenY +
            " -> world: " + worldPos.x + ", " + worldPos.y + " button: " + button);

        // Check if this was a drag or a click
        float dragDistance = dragStart.dst(worldPos);

        if (dragDistance < DRAG_THRESHOLD) {
            // It's a click, not a drag
            if (clickListener != null) {
                if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
                    Gdx.app.log("InputHandler", "LEFT CLICK at: " + worldPos.x + ", " + worldPos.y);
                    clickListener.onLeftClick(worldPos.x, worldPos.y);
                } else if (button == com.badlogic.gdx.Input.Buttons.RIGHT) {
                    Gdx.app.log("InputHandler", "RIGHT CLICK at: " + worldPos.x + ", " + worldPos.y);
                    clickListener.onRightClick(worldPos.x, worldPos.y);
                }
            }
        } else {
            Gdx.app.log("InputHandler", "Drag detected (distance: " + dragDistance + "), not firing click");
        }

        isDragging = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldPos = screenToWorld(screenX, screenY);

        // Calculate drag distance from start
        float dragDistance = dragStart.dst(worldPos);

        if (dragDistance > DRAG_THRESHOLD) {
            isDragging = true;

            // Camera panning with middle mouse or right mouse drag
            if (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.MIDDLE) ||
                (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.RIGHT) &&
                    Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SHIFT_LEFT))) {

                float deltaX = lastDragPos.x - worldPos.x;
                float deltaY = lastDragPos.y - worldPos.y;

                camera.position.x += deltaX;
                camera.position.y += deltaY;
                camera.update();
            }
        }

        lastDragPos.set(worldPos);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Zoom in/out with mouse wheel
        float zoomAmount = amountY * 0.1f;
        camera.zoom += zoomAmount;

        // Clamp zoom
        camera.zoom = Math.max(Constants.CAMERA_ZOOM_MIN, Math.min(camera.zoom, Constants.CAMERA_ZOOM_MAX));
        camera.update();

        Gdx.app.log("InputHandler", "Zoom: " + camera.zoom);
        return true;
    }

    /**
     * Convert screen coordinates to world coordinates
     */
    public Vector2 screenToWorld(int screenX, int screenY) {
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        return new Vector2(touchPos.x, touchPos.y);
    }

    /**
     * Check if currently dragging
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Get drag start position
     */
    public Vector2 getDragStart() {
        return dragStart;
    }
}
