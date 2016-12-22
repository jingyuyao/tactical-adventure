package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jingyuyao.tactical.Assets;
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.HashMap;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
public class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 15; // # tiles
    private static final int VIEWPORT_HEIGHT = 10; // # tiles

    private final MapActorFactory mapActorFactory;
    private final Sprite highlightSprite;

    public MapViewFactory(AssetManager assetManager) {
        mapActorFactory = new MapActorFactory(assetManager);
        highlightSprite = new Sprite(assetManager.get(Assets.HIGHLIGHT, Texture.class));
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    public MapView create(TiledMap tiledMap, Map map, MapState mapState) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        Stage world = new Stage(viewport);
        java.util.Map<MapObject, MapActor> actorMap = new HashMap<MapObject, MapActor>();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Terrain terrain = map.terrains().get(x, y);
                MapActor actor = mapActorFactory.create(map, mapState, terrain);
                world.addActor(actor);
                actorMap.put(terrain, actor);
            }
        }

        // Characters must be added after terrain so they get hit by touch input
        for (Character character : map.characters()) {
            MapActor actor = mapActorFactory.create(map, mapState, character);
            world.addActor(actor);
            actorMap.put(character, actor);
        }

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);
        return new MapView(map, world, mapRenderer, actorMap, highlightSprite);
    }
}
