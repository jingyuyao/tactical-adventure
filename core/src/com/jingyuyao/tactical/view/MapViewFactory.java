package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jingyuyao.tactical.model.Character;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.Terrain;

import javax.inject.Inject;

/**
 * Not the cleanest approach but at least {@link MapView} can now receive injections via this class.
 */
public class MapViewFactory {
    private static final int TILE_SIZE = 32; // pixels
    private static final float RENDER_SCALE = 1f / TILE_SIZE;
    private static final int MAP_WIDTH = 25; // # tiles
    private static final int MAP_HEIGHT = 15; // # tiles

    private final MapActorFactory mapActorFactory;

    @Inject
    public MapViewFactory(MapActorFactory mapActorFactory) {
        this.mapActorFactory = mapActorFactory;
    }

    /**
     * Creates a {@link MapView} from the given map and adds all actors to the stage.
     */
    public MapView create(TiledMap tiledMap, Map map) {
        OrthographicCamera camera = new OrthographicCamera();
        FitViewport viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        Stage stage = new Stage(viewport);

        for (int y = 0; y < map.getWorldHeight(); y++) {
            for (int x = 0; x < map.getWorldWidth(); x++) {
                Terrain terrain = map.getTerrain(x, y);
                MapActor<Terrain> actor = mapActorFactory.createTerrain(terrain);
                stage.addActor(actor);
            }
        }

        for (Character character : map.getCharacters()) {
            MapActor<Character> actor = mapActorFactory.createCharacter(character);
            stage.addActor(actor);
        }

        return new MapView(stage, new OrthogonalTiledMapRenderer(tiledMap, RENDER_SCALE));
    }
}
