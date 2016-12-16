package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
public class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final float TILE_SCALE = 1f; // Relative to map size
    private static final int MAP_WIDTH = 25; // # tiles
    private static final int MAP_HEIGHT = 15;
    private static final String TERRAIN_LAYER = "terrain";
    private static final String TYPE_KEY = "type";

    private final Provider<Highlighter> highlighterProvider;

    @Inject
    public MapViewFactory(Provider<Highlighter> highlighterProvider) {
        this.highlighterProvider = highlighterProvider;
    }

    /**
     * Creates a {@link MapView} where (0,0) is on the bottom left. Each tile has a size of one.
     * Viewport's size is relative to tile size.
     */
    public MapView create(TiledMap tiledMap) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        Stage stage = new Stage(viewport);
        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);

        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(TERRAIN_LAYER);
        Preconditions.checkNotNull(terrainLayer, "MapView must contain a terrain layer.");
        int height = terrainLayer.getHeight();
        int width = terrainLayer.getWidth();
        Preconditions.checkArgument(height>0, "MapView height must be > 0");
        Preconditions.checkArgument(width>0, "MapView width must be > 0");

        ArrayList<ArrayList<MapActor<Terrain>>> terrainMap = new ArrayList<ArrayList<MapActor<Terrain>>>();
        MapView mapView = new MapView(tiledMap, stage, mapRenderer, terrainMap, width, height);

        for (int y = 0; y < height; y++) {
            ArrayList<MapActor<Terrain>> row = new ArrayList<MapActor<Terrain>>();
            terrainMap.add(row);
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(x, y);
                MapActor<Terrain> terrain = createTerrain(cell, x, y, TILE_SCALE);
                row.add(terrain);
                stage.addActor(terrain);
            }
        }

        // testing
        mapView.addCharacter(createCharacter(1, 1, TILE_SCALE));
        mapView.addCharacter(createCharacter(3, 3, TILE_SCALE));

        return mapView;
    }

    private MapActor<Character> createCharacter(int x, int y, float size) {
        return new MapActor<Character>(new Character(x, y), size, highlighterProvider.get());
    }

    private MapActor<Terrain> createTerrain(TiledMapTileLayer.Cell cell, int x, int y, float size) {
        MapProperties tileProperties = cell.getTile().getProperties();
        Terrain.Type type = Terrain.Type.NORMAL;
        if (tileProperties.containsKey(TYPE_KEY)) {
            String tileType = tileProperties.get(TYPE_KEY, String.class);
            try {
                type = Terrain.Type.valueOf(tileType);
            } catch (IllegalArgumentException e) {
                Gdx.app.log("Terrain", String.format("invalid type %s", tileType));
            }
        }

        return new MapActor<Terrain>(new Terrain(x, y, type), size, highlighterProvider.get());
    }
}
