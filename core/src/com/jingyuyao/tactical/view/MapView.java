package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.object.MapObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Contains and renders the world.
 * The world is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView implements Observer {
    private final Stage world;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Sprite highlightSprite;

    /**
     * A map view contains a world with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param map Highlighter used to draw the highlight
     * @param world Should already be set up with all the {@link MapActor}
     * @param mapRenderer The tiled map renderer
     * @param highlightSprite The sprite drawn for highlights
     */
    MapView(
            Map map,
            Stage world,
            OrthogonalTiledMapRenderer mapRenderer,
            Sprite highlightSprite
    ) {
        this.world = world;
        this.mapRenderer = mapRenderer;
        this.highlightSprite = highlightSprite;
        highlightSprite.setBounds(0, 0, ActorFactory.ACTOR_SIZE, ActorFactory.ACTOR_SIZE);

        map.addObserver(this);
    }

    public Stage getWorld() {
        return world;
    }

    void resize(int width, int height) {
        world.getViewport().update(width, height);
    }

    void dispose() {
        world.dispose();
    }

    void act(float delta) {
        world.act(delta);
    }

    void draw() {
        mapRenderer.setView((OrthographicCamera) world.getCamera());
        mapRenderer.render();
        world.draw();
        world.getBatch().begin();
        highlightSprite.draw(world.getBatch());
        world.getBatch().end();
    }

    @Override
    public void update(Observable observable, Object o) {
        if (Map.HighlightChange.class.isInstance(o)) {
            highlightChange((Map.HighlightChange) o);
        }
    }

    private void highlightChange(Map.HighlightChange highlightChange) {
        MapObject highlighted = highlightChange.getHighlight();
        if (highlighted == null) {
            return;
        }

        highlightSprite.setPosition(
                highlighted.getCoordinate().getX(),
                highlighted.getCoordinate().getY()
        );
    }
}
