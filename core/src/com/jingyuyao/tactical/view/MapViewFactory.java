package com.jingyuyao.tactical.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 15; // # tiles
    private static final int VIEWPORT_HEIGHT = 10; // # tiles

    private final ActorFactory actorFactory;

    MapViewFactory(EventBus eventBus, AssetManager assetManager) {
        actorFactory = new ActorFactory(eventBus, assetManager);
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    MapView create(TiledMap tiledMap, Level level) {
        Stage world = new Stage(new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));

        for (int x = 0; x < level.getMap().getWidth(); x++) {
            for (int y = 0; y < level.getMap().getHeight(); y++) {
                Terrain terrain = level.getMap().getTerrains().get(x, y);
                world.addActor(actorFactory.create(level, terrain));
            }
        }

        // Characters must be added after terrain so they get hit by touch input
        for (Player player : level.getMap().getPlayers()) {
            world.addActor(actorFactory.create(level, player));
        }
        for (Enemy enemy : level.getMap().getEnemies()) {
            world.addActor(actorFactory.create(level, enemy));
        }

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);
        return new MapView(world, mapRenderer);
    }
}
