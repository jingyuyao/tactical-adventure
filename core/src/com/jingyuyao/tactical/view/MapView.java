package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.MapObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Contains and renders the stage.
 * The stage is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView implements Observer {
    private final Highlighter highlighter;
    private final Stage stage;
    private final java.util.Map<MapObject, MapActor> actorMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Sprite highlightSprite;

    /**
     * A map view contains a stage with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param stage Should already be set up with all the {@link MapActor}
     * @param mapRenderer The tiled map renderer
     * @param actorMap Contains all the game object to actor mapping
     * @param highlighter Highlighter used to draw the highlight
     * @param highlightSprite The sprite drawn for highlights
     */
    MapView(Stage stage, OrthogonalTiledMapRenderer mapRenderer, java.util.Map<MapObject, MapActor> actorMap, Highlighter highlighter,
            Sprite highlightSprite) {
        this.stage = stage;
        this.mapRenderer = mapRenderer;
        this.actorMap = actorMap;
        this.highlighter = highlighter;
        this.highlightSprite = highlightSprite;
        highlighter.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        updateHighlight();
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
        stage.getBatch().begin();
        highlightSprite.draw(stage.getBatch());
        stage.getBatch().end();
    }

    private void updateHighlight() {
        MapObject highlighted = highlighter.getHighlight();
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
    }
}
