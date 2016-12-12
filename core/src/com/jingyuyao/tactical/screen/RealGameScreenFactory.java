package com.jingyuyao.tactical.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.google.inject.Inject;
import com.jingyuyao.tactical.TacticalAdventure;
import com.jingyuyao.tactical.map.Map;
import com.jingyuyao.tactical.map.MapFactory;

public class RealGameScreenFactory implements GameScreenFactory {
    private final TacticalAdventure game;
    private final AssetManager assetManager;
    private final MapFactory mapFactory;

    @Inject
    public RealGameScreenFactory(TacticalAdventure game, AssetManager assetManager, MapFactory mapFactory) {
        this.game = game;
        this.assetManager = assetManager;
        this.mapFactory = mapFactory;
    }

    @Override
    public GameScreen create(String mapName) {
        TiledMap tiledMap = assetManager.get(mapName, TiledMap.class);
        Map map = mapFactory.create(tiledMap);
        return new GameScreen(game, map);
    }
}
