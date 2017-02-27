package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class AssetModule extends AbstractModule {

  public static final String TEXTURE_ATLAS = "packed/texture.atlas";
  public static final String TEST_MAP = "maps/test_map";
  public static final String SKIN = "ui/uiskin.json";
  public static final String JINGYU = "sprites/jingyu.png";
  public static final String KEVIN = "sprites/kevin.png";
  public static final String ANDREW = "sprites/andrew.png";
  public static final String BEN = "sprites/ben.png";
  public static final String SOLDIER = "sprites/soldier.png";

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    AssetManager manager = new AssetManager();

    // Loaders
    manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

    // Assets
    manager.load(TEXTURE_ATLAS, TextureAtlas.class);
    manager.load(SKIN, Skin.class);
    manager.load(JINGYU, Texture.class);
    manager.load(KEVIN, Texture.class);
    manager.load(ANDREW, Texture.class);
    manager.load(BEN, Texture.class);
    manager.load(SOLDIER, Texture.class);

    manager.finishLoading();
    return manager;
  }

  @Provides
  @Singleton
  TextureAtlas provideTextureAtlas(AssetManager assetManager) {
    return assetManager.get(TEXTURE_ATLAS);
  }
}
