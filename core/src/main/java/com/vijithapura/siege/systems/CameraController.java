package com.vijithapura.siege.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.vijithapura.siege.utils.Constants;

public class CameraController {

    public static void handleCameraMovement(OrthographicCamera camera, float delta) {
        float speed = 400 * delta;

        // WASD movement
        if (Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += speed;

        // Edge scrolling
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        int edgeMargin = 30;

        if (mouseX < edgeMargin) camera.position.x -= speed;
        if (mouseX > Constants.SCREEN_WIDTH - edgeMargin) camera.position.x += speed;
        if (mouseY < edgeMargin) camera.position.y += speed;
        if (mouseY > Constants.SCREEN_HEIGHT - edgeMargin) camera.position.y -= speed;

        clampCamera(camera);
        camera.update();
    }

    private static void clampCamera(OrthographicCamera camera) {
        float halfWidth = camera.viewportWidth * camera.zoom / 2;
        float halfHeight = camera.viewportHeight * camera.zoom / 2;

        camera.position.x = Math.max(halfWidth,
            Math.min(Constants.MAP_WIDTH - halfWidth, camera.position.x));
        camera.position.y = Math.max(halfHeight,
            Math.min(Constants.MAP_HEIGHT - halfHeight, camera.position.y));
    }
}
