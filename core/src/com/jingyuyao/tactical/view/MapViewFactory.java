package com.jingyuyao.tactical.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.jingyuyao.tactical.model.Level;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
@Singleton
class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int VIEWPORT_WIDTH = 15; // # tiles
    private static final int VIEWPORT_HEIGHT = 10; // # tiles

    private final ActorFactory actorFactory;

    @Inject
    MapViewFactory(ActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    MapView create(TiledMap tiledMap, Level level) {
        Stage world = new Stage(new ExtendViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT));

        for (Terrain terrain : level.getMap().getTerrains()) {
            world.addActor(actorFactory.create(terrain));
        }

        // Characters must be added after terrain so they get hit by touch input
        for (Player player : level.getMap().getPlayers()) {
            world.addActor(actorFactory.create(player));
        }
        for (Enemy enemy : level.getMap().getEnemies()) {
            world.addActor(actorFactory.create(enemy));
        }

        OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE);
        return new MapView(world, mapRenderer);
    }
}
