package com.jingyuyao.tactical;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

/**
 * Contain all asset names and function to load them into an {@link AssetManager}
 */
public class AssetModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    AssetManager manager = new AssetManager();
    manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    return manager;
  }
}
