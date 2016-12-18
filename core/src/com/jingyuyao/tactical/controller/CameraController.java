package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CameraController extends InputAdapter {
    private static final float HORIZONTAL_EDGE = 0.2f; // percentage
    private static final float VERTICAL_EDGE = 0.2f; // percentage
    private static final float MOVEMENT_DELTA = 0.1f; // world unit
    private static final long STARTING_DELAY = 200L; // ms
    private static final long NO_DELAY = 0L; // ms
    private static final long REPEAT_DELAY = 10L; // ms
    private static final int NO_POINTER = -1;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Runnable screenMover = new Runnable() {
        @Override
        public void run() {
            adjustCamera();
        }
    };
    private ScheduledFuture<?> screenMoverHandle;
    private final Viewport viewport;
    private final Camera camera;
    private final int worldWidth;
    private final int worldHeight;
    private int currentPointer = NO_POINTER;
    // Raw mouse coordinate on screen (including gutters)
    private int currentX;
    private int currentY;

    CameraController(Viewport viewport, Camera camera, int worldWidth, int worldHeight) {
        this.viewport = viewport;
        this.camera = camera;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenMoverHandle != null) screenMoverHandle.cancel(false);
        long delay = (currentPointer == NO_POINTER) ? STARTING_DELAY : NO_DELAY;
        screenMoverHandle =
                scheduler.scheduleAtFixedRate(screenMover, delay, REPEAT_DELAY, TimeUnit.MILLISECONDS);
        currentPointer = pointer;
        currentX = screenX;
        currentY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentPointer == pointer) {
            if (screenMoverHandle != null) screenMoverHandle.cancel(false);
            screenMoverHandle = null;
            currentPointer = NO_POINTER;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        currentX = screenX;
        currentY = screenY;
        return false;
    }

    private void adjustCamera() {
        // Height and width of the screen (excluding gutters)
        int screenWidth = viewport.getScreenWidth();
        int screenHeight = viewport.getScreenHeight();
        int leftGutterWidth = viewport.getLeftGutterWidth();
        int topGutterHeight = viewport.getTopGutterHeight();
        float xEdgeWidth = HORIZONTAL_EDGE * (float) screenWidth;
        float yEdgeHeight = VERTICAL_EDGE * (float) screenHeight;
        float xDelta = 0f;
        float yDelta = 0f;

        if (currentX > leftGutterWidth && currentX < xEdgeWidth + leftGutterWidth) {
            xDelta = -MOVEMENT_DELTA;
        } else if (currentX < screenWidth + leftGutterWidth
                && currentX > screenWidth + leftGutterWidth - xEdgeWidth ) {
            xDelta = MOVEMENT_DELTA;
        }

        if (currentY > topGutterHeight && currentY < yEdgeHeight + topGutterHeight) {
            yDelta = MOVEMENT_DELTA;
        } else if (currentY < screenHeight + topGutterHeight
                && currentY > screenHeight + topGutterHeight - yEdgeHeight) {
            yDelta = -MOVEMENT_DELTA;
        }

        camera.translate(xDelta, yDelta, 0);

        // Need to prevent moving camera outside of world
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        float viewportHalfWorldWidth = viewport.getWorldWidth() / 2f;
        float viewportHalfWorldHeight = viewport.getWorldHeight() / 2f;

        if (cameraX + viewportHalfWorldWidth > worldWidth) {
            camera.position.x = worldWidth - viewportHalfWorldWidth;
        } else if (cameraX - viewportHalfWorldWidth < 0) {
            camera.position.x = viewportHalfWorldWidth;
        }

        if (cameraY + viewportHalfWorldHeight > worldHeight) {
            camera.position.y = worldHeight - viewportHalfWorldHeight;
        } else if (cameraY - viewportHalfWorldHeight < 0) {
            camera.position.y = viewportHalfWorldHeight;
        }

        camera.update();
    }
}
