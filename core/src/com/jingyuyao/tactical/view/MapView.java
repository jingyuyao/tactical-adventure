package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.util.Callable;

/**
 * Contains and renders the stage.
 * The stage is rendered in a grid scale (i.e. showing a 30x20 grid).
 * Controllers (input listeners) are registered to various actors on the stage.
 */
public class MapView {
    private final Map map;
    private final Stage stage;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Sprite reachableSprite;
    private final java.util.Map<MapObject, MapActor> actorMap;

    /**
     * A map view contains a stage with all the actors and a way to render them.
     * The background map is backed by a {@link OrthogonalTiledMapRenderer}.
     * @param map The map this view is for
     * @param stage Should already be set up with all the {@link MapActor}
     * @param actorMap Contains all the game object to actor mapping
     * @param mapRenderer The tiled map renderer
     * @param reachableSprite The sprite drawn for {@link Terrain.PotentialTarget#REACHABLE}
     */
    MapView(Map map, Stage stage, java.util.Map<MapObject, MapActor> actorMap, OrthogonalTiledMapRenderer mapRenderer,
            Sprite reachableSprite) {
        this.map = map;
        this.stage = stage;
        this.actorMap = actorMap;
        this.mapRenderer = mapRenderer;
        this.reachableSprite = reachableSprite;
        map.addCall(Map.Calls.CHARACTERS_UPDATE, new Callable() {
            @Override
            public void call() {
                updateCharacters();
            }
        });
        map.addCall(Map.Calls.TERRAINS_UPDATE, new Callable() {
            @Override
            public void call() {
                updateTerrains();
            }
        });
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

    private void updateTerrains() {
        for (Terrain terrain : map.getTerrains()) {
            MapActor actor = actorMap.get(terrain);
            switch (terrain.getPotentialTarget()) {
                case NONE:
                    actor.setSprite(null);
                    break;
                case REACHABLE:
                    actor.setSprite(reachableSprite);
                    break;
                case ATTACKABLE:
                    break;
            }
        }
    }

    private void updateCharacters() {
        for (Character character : map.getCharacters()) {
            MapActor actor = actorMap.get(character);
            if (actor.getX() != character.getX() || actor.getY() != character.getY()) {
                MoveToAction moveAction = new MoveToAction();
                moveAction.setPosition(character.getX(), character.getY());
                moveAction.setDuration(0.5f);
                actor.addAction(moveAction);
            }
        }
    }
}
