package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Map;

/**
 * Contains and renders the stage.
 * The stage is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView {
    private final Map map;
    private final Stage stage;
    private final java.util.Map<MapObject, MapActor> actorMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Sprite highlightSprite;

    /**
     * A map view contains a stage with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param map The map this view is for
     * @param stage Should already be set up with all the {@link MapActor}
     * @param actorMap Contains all the game object to actor mapping
     * @param mapRenderer The tiled map renderer
     * @param highlightSprite The sprite drawn for highlights
     */
    MapView(Map map, Stage stage, java.util.Map<MapObject, MapActor> actorMap, OrthogonalTiledMapRenderer mapRenderer,
            Sprite highlightSprite) {
        this.map = map;
        this.stage = stage;
        this.actorMap = actorMap;
        this.mapRenderer = mapRenderer;
        this.highlightSprite = highlightSprite;
    }

    public Stage getStage() {
        return stage;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void dispose() {
        stage.dispose();
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) stage.getCamera());
        mapRenderer.render();
        stage.draw();
        drawHighlight();
    }

    private void drawHighlight() {
        MapObject highlighted = map.getHighlighted();
        if (highlighted == null) {
            return;
        }

        MapActor highlightedActor = actorMap.get(highlighted);
        highlightSprite.setBounds(
                highlightedActor.getX(),
                highlightedActor.getY(),
                highlightedActor.getWidth(),
                highlightedActor.getHeight()
        );
        stage.getBatch().begin();
        highlightSprite.draw(stage.getBatch());
        stage.getBatch().end();
    }
}
