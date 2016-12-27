package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class Assets {
    public static final String TEST_MAP = "maps/test_map.tmx";
    public static final String JOHN = "sprites/john.png";
    public static final String BILLY = "sprites/billy.png";
    public static final String HIGHLIGHT = "sprites/highlight.png";
    public static final String MOVE = "sprites/move.png";
    public static final String DANGER = "sprites/danger.png";
    public static final String ATTACK = "sprites/attack.png";
    public static final String CHOSEN_TARGET = "sprites/chosen_target.png";
    public static final String POTENTIAL_TARGET = "sprites/potential_target.png";
    public static final String SKIN = "ui/uiskin.json";

    static AssetManager createAssetManager() {
        AssetManager manager = new AssetManager();

        // Loaders
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        // Assets
        manager.load(TEST_MAP, TiledMap.class);
        manager.load(JOHN, Texture.class);
        manager.load(BILLY, Texture.class);
        manager.load(HIGHLIGHT, Texture.class);
        manager.load(MOVE, Texture.class);
        manager.load(DANGER, Texture.class);
        manager.load(ATTACK, Texture.class);
        manager.load(CHOSEN_TARGET, Texture.class);
        manager.load(POTENTIAL_TARGET, Texture.class);
        manager.load(SKIN, Skin.class);

        manager.finishLoading();
        return manager;
    }
}
