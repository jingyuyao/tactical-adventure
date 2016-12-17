package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.MapObject;
import com.jingyuyao.tactical.model.Terrain;

import java.util.HashMap;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
public class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 25; // # tiles
    private static final int VIEWPORT_HEIGHT = 15; // # tiles

    private final MapActorFactory mapActorFactory;
    private final Sprite reachableSprite;

    public MapViewFactory(AssetManager assetManager) {
        mapActorFactory = new MapActorFactory(assetManager);
        reachableSprite = new Sprite(assetManager.get(Assets.BLUE_OVERLAY, Texture.class));
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    public MapView create(TiledMap tiledMap, Map map) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        Stage stage = new Stage(viewport);
        java.util.Map<MapObject, MapActor> actorMap = new HashMap<MapObject, MapActor>();

        for (int x = 0; x < map.getWorldWidth(); x++) {
            for (int y = 0; y < map.getWorldHeight(); y++) {
                Terrain terrain = map.getTerrain(x, y);
                MapActor actor = mapActorFactory.createTerrain(map, terrain);
                stage.addActor(actor);
                actorMap.put(terrain, actor);
            }
        }

        // Characters must be added after terrain so they get hit by touch input
        for (Character character : map.getCharacters()) {
            MapActor actor = mapActorFactory.createCharacter(map, character);
            stage.addActor(actor);
            actorMap.put(character, actor);
        }

        return new MapView(
                map, stage, actorMap, new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE),reachableSprite);
    }
}
