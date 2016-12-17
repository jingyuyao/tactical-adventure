package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class Assets {
    public static String TEST_MAP = "maps/test_map.tmx";
    public static String JOHN = "sprites/john.png";
    public static String BILLY = "sprites/billy.png";
    public static String HIGHLIGHT = "sprites/highlight.png";
    public static String BLUE_OVERLAY = "sprites/blue_overlay.png";


    static AssetManager createAssetManager() {
        AssetManager manager = new AssetManager();

        // Loaders
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        // Assets
        manager.load(TEST_MAP, TiledMap.class);
        manager.load(JOHN, Texture.class);
        manager.load(BILLY, Texture.class);
        manager.load(HIGHLIGHT, Texture.class);
        manager.load(BLUE_OVERLAY, Texture.class);

        manager.finishLoading();
        return manager;
    }
}
