package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Contains and renders the world.
 * The world is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView implements Observer {
    private final Map map;
    private final Stage world;
    private final java.util.Map<MapObject, MapActor> actorMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Sprite highlightSprite;

    /**
     * A map view contains a world with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param map Highlighter used to draw the highlight
     * @param world Should already be set up with all the {@link MapActor}
     * @param mapRenderer The tiled map renderer
     * @param actorMap Contains all the game object to actor mapping
     * @param highlightSprite The sprite drawn for highlights
     */
    MapView(
            Map map,
            Stage world,
            OrthogonalTiledMapRenderer mapRenderer,
            java.util.Map<MapObject, MapActor> actorMap,
            Sprite highlightSprite
    ) {
        this.world = world;
        this.mapRenderer = mapRenderer;
        this.actorMap = actorMap;
        this.map = map;
        this.highlightSprite = highlightSprite;
        map.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        updateHighlight();
    }

    public Stage getWorld() {
        return world;
    }

    public void resize(int width, int height) {
        world.getViewport().update(width, height);
    }

    public void dispose() {
        world.dispose();
    }

    public void act(float delta) {
        world.act(delta);
    }

    public void draw() {
        mapRenderer.setView((OrthographicCamera) world.getCamera());
        mapRenderer.render();
        world.draw();
        world.getBatch().begin();
        highlightSprite.draw(world.getBatch());
        world.getBatch().end();
    }

    private void updateHighlight() {
        MapObject highlighted = map.getHighlight();
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
