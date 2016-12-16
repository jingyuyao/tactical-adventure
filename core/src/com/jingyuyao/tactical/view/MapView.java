package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Contains and renders the stage.
 * The stage is rendered in a grid scale (i.e. showing a 30x20 grid).
 * Controllers (input listeners) are registered to various actors on the stage.
 */
public class MapView {
    private final Stage stage;
    private final OrthogonalTiledMapRenderer mapRenderer;

    /**
     * A map view contains a stage with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     *
     * @param stage Should already be set up with all the {@link MapActor}
     * @param mapRenderer The tiled map renderer
     */
    MapView(Stage stage, OrthogonalTiledMapRenderer mapRenderer) {
        this.stage = stage;
        this.mapRenderer = mapRenderer;
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) stage.getCamera());
        mapRenderer.render();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public Stage getStage() {
        return stage;
    }
}
