package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
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

  public static final String TEST_MAP = "maps/test_map";
  public static final String JOHN = "sprites/john.png";
  public static final String BILLY = "sprites/billy.png";
  public static final String HIGHLIGHT = "sprites/highlight.png";
  public static final String MOVE = "sprites/move.png";
  public static final String HIT = "sprites/hit.png";
  public static final String ATTACK = "sprites/attack.png";
  public static final String TARGET_SELECT = "sprites/target_select.png";
  public static final String POTENTIAL_TARGET = "sprites/potential_target.png";
  public static final String SKIN = "ui/uiskin.json";

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
    manager.load(JOHN, Texture.class);
    manager.load(BILLY, Texture.class);
    manager.load(HIGHLIGHT, Texture.class);
    manager.load(MOVE, Texture.class);
    manager.load(HIT, Texture.class);
    manager.load(ATTACK, Texture.class);
    manager.load(TARGET_SELECT, Texture.class);
    manager.load(POTENTIAL_TARGET, Texture.class);
    manager.load(SKIN, Skin.class);

    manager.finishLoading();
    return manager;
  }
}
