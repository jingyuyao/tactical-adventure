package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class Assets {
    public static final String TEST_MAP = "maps/test_map.tmx";
    public static final String JOHN = "sprites/john.png";
    public static final String BILLY = "sprites/billy.png";
    public static final String HIGHLIGHT = "sprites/highlight.png";
    public static final String BLUE_OVERLAY = "sprites/blue_overlay.png";
    public static final String RED_OVERLAY = "sprites/red_overlay.png";

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
        manager.load(RED_OVERLAY, Texture.class);

        manager.finishLoading();
        return manager;
    }
}
