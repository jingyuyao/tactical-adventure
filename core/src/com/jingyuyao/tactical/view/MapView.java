package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.actor.BaseActor;

/**
 * Contains and renders the world.
 * The world is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView {
    private final Stage world;
    private final OrthogonalTiledMapRenderer mapRenderer;

    /**
     * A map view contains a world with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param world Should already be set up with all the {@link BaseActor}
     * @param mapRenderer The tiled map renderer
     */
    MapView(Stage world, OrthogonalTiledMapRenderer mapRenderer) {
        this.world = world;
        this.mapRenderer = mapRenderer;
    }

    public Stage getWorld() {
        return world;
    }

    void act(float delta) {
        world.act(delta);
    }

    void draw() {
        world.getViewport().apply();
        mapRenderer.setView((OrthographicCamera) world.getCamera());
        mapRenderer.render();
        world.draw();
    }

    void resize(int width, int height) {
        // TODO: update camera so we don't show black bars
        world.getViewport().update(width, height);
    }

    void dispose() {
        world.dispose();
    }
}
