package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;

/**
 * Contains the stage and everything needed to render the stage.
 * The stage is rendered in a grid scale (i.e. showing a 30x20 grid).
 * Controllers (input listeners) are registered to various actors on the stage.
 */
public class MapView {
    private final Stage stage;
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private final int worldWidth;
    private final int worldHeight;
    /**
     * (0,0) starts at bottom left just like {@link TiledMapTileLayer}.
     */
    private final ArrayList<ArrayList<MapActor<Terrain>>> terrainMap;
    private final ArrayList<MapActor<Character>> characters = new ArrayList<MapActor<Character>>();

    MapView(TiledMap tiledMap, Stage stage, OrthogonalTiledMapRenderer mapRenderer,
            ArrayList<ArrayList<MapActor<Terrain>>> terrainMap, int worldWidth, int worldHeight) {
        this.stage = stage;
        this.tiledMap = tiledMap;
        this.terrainMap = terrainMap;
        this.mapRenderer = mapRenderer;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
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

    public MapActor<Terrain> getTerrain(int x, int y) {
        return terrainMap.get(y).get(x);
    }

    public void addCharacter(MapActor<Character> character) {
        characters.add(character);
        stage.addActor(character);
    }

    public void removeCharacter(MapActor<Character> character) {
        characters.remove(character);
        character.remove();
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Stage getStage() {
        return stage;
    }
}
