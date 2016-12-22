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
import com.jingyuyao.tactical.model.*;
import com.jingyuyao.tactical.model.state.MapState;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 15; // # tiles
    private static final int VIEWPORT_HEIGHT = 10; // # tiles

    private final ActorFactory actorFactory;
    private final Sprite highlightSprite;

    MapViewFactory(AssetManager assetManager) {
        actorFactory = new ActorFactory(assetManager);
        highlightSprite = new Sprite(assetManager.get(Assets.HIGHLIGHT, Texture.class));
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    MapView create(TiledMap tiledMap, Map map, MapState mapState) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        Stage world = new Stage(viewport);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Terrain terrain = map.getTerrains().get(x, y);
                world.addActor(actorFactory.create(map, mapState, terrain));
            }
        }

        // Characters must be added after terrain so they get hit by touch input
        for (Player player : map.getPlayers()) {
            world.addActor(actorFactory.create(map, mapState, player));
        }
        for (Enemy enemy : map.getEnemies()) {
            world.addActor(actorFactory.create(map, mapState, enemy));
        }

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);
        return new MapView(map, world, mapRenderer, highlightSprite);
    }
}
