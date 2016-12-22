package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Turn;

import java.util.Observable;
import java.util.Observer;

/**
 * Contains and renders the world.
 * The world is rendered in a grid scale (i.e. showing a 30x20 grid).
 */
public class MapView {
    private final Map map;
    private final Turn turn;
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
            Turn turn,
            Stage world,
            OrthogonalTiledMapRenderer mapRenderer,
            java.util.Map<MapObject, MapActor> actorMap,
            Sprite highlightSprite
    ) {
        this.world = world;
        this.turn = turn;
        this.mapRenderer = mapRenderer;
        this.actorMap = actorMap;
        this.map = map;
        this.highlightSprite = highlightSprite;
        map.addObserver(this.new MapObserver());
        turn.addObserver(this.new TurnObserver());
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

    private class MapObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
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

    private class TurnObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            for (Player player : map.getPlayers()) {
                CharacterActor actor = (CharacterActor) actorMap.get(player);
                // TODO: Let actor update its color? then we need to give character its turn info
                // but then we would be violating the single source of truth... hum...
                if (!turn.canAct(player)) {
                    actor.getSprite().setColor(Color.GRAY);
                } else {
                    actor.getSprite().setColor(Color.WHITE);
                }
            }
        }
    }
}
