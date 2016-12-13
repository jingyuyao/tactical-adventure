package com.jingyuyao.tactical.map;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MapMovementProcessor extends InputAdapter {
    private static final float HORIZONTAL_EDGE = 0.12f;
    private static final float VERTICAL_EDGE = 0.13f;
    private static final float MOVEMENT_DELTA = 0.2f;
    private static final long STARTING_DELAY = 200L; // ms
    private static final long REPEAT_DELAY = 10L; // ms

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Runnable screenMover = new Runnable() {
        @Override
        public void run() {
            adjustViewPort();
        }
    };
    private ScheduledFuture<?> screenMoverHandle;
    private final Stage stage;
    // Raw mouse coordinate on screen (including gutters)
    private int currentX;
    private int currentY;

    MapMovementProcessor(Stage stage) {
        this.stage = stage;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Temp solution for preventing multi-touch for screwing things up
        // TODO: find a better solution for multi-touch
        if (screenMoverHandle == null) {
            screenMoverHandle =
                    scheduler.scheduleAtFixedRate(screenMover, STARTING_DELAY, REPEAT_DELAY, TimeUnit.MILLISECONDS);

            currentX = screenX;
            currentY = screenY;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenMoverHandle != null) screenMoverHandle.cancel(false);
        screenMoverHandle = null;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        currentX = screenX;
        currentY = screenY;
        return false;
    }

    private void adjustViewPort() {
        Viewport viewport = stage.getViewport();
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

        Camera camera = stage.getCamera();
        camera.translate(xDelta, yDelta, 0);
        camera.update();
    }
}
