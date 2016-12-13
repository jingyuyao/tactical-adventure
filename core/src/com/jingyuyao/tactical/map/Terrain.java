package com.jingyuyao.tactical.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * A terrain tile.
 */
class Terrain extends Actor {
    private final Map map;
    private final TiledMapTileLayer.Cell cell;
    private final ShapeRenderer shapeRenderer;
    private Type type;
    private boolean highlighted = false;

    Terrain(Map map, TiledMapTileLayer.Cell cell, Type type, ShapeRenderer shapeRenderer,
                float x, float y, float width, float height) {
        this.map = map;
        this.cell = cell;
        this.type = type;
        this.shapeRenderer = shapeRenderer;
        setBounds(x, y, width, height);
        addListener(this.new TerrainListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (highlighted) {
            batch.end();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
            shapeRenderer.end();
            batch.begin();
        }
    }

    public enum Type {
        NORMAL,
        OBSTRUCTED,
        WATER,
        MOUNTAIN
    }

    private class TerrainListener extends InputListener {
        // Prevents disabling highlight from clicking the actor
        private boolean exitFromTouch = false;

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            highlighted = true;
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (exitFromTouch) {
                exitFromTouch = false;
            } else {
                highlighted = false;
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            exitFromTouch = true;
            return false;
        }
    }
}
