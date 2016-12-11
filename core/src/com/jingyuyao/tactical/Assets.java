package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class Assets {
    public static String TEST_MAP = "maps/test_map.tmx";

    static AssetManager load() {
        AssetManager manager = new AssetManager();

        // Loaders
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        // Assets
        manager.load(TEST_MAP, TiledMap.class);

        manager.finishLoading();
        return manager;
    }
}
