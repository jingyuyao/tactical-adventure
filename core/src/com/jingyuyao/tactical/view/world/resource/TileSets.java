package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.view.world.WorldConfig;
import com.jingyuyao.tactical.view.world.resource.ResourceModule.TileTextureCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TileSets {

  private final AssetManager assetManager;
  private final WorldConfig worldConfig;
  private final TextureFactory textureFactory;
  private final Map<IntKey, WorldTexture> tileTextureCache;

  @Inject
  TileSets(
      AssetManager assetManager,
      WorldConfig worldConfig,
      TextureFactory textureFactory,
      @TileTextureCache Map<IntKey, WorldTexture> tileTextureCache) {
    this.assetManager = assetManager;
    this.worldConfig = worldConfig;
    this.textureFactory = textureFactory;
    this.tileTextureCache = tileTextureCache;
  }

  public WorldTexture get(IntKey key) {
    if (!tileTextureCache.containsKey(key)) {
      loadTiles(key.getBundle());
    }
    return tileTextureCache.get(key);
  }

  private void loadTiles(KeyBundle bundle) {
    String assetPath = bundle.getPathWithExtension();
    if (assetManager.isLoaded(assetPath)) {
      throw new RuntimeException(assetPath + " already loaded");
    }
    assetManager.load(assetPath, Texture.class);
    assetManager.finishLoadingAsset(assetPath);

    Texture tileSet = assetManager.get(assetPath);
    int tileSize = worldConfig.getTileSize();
    TextureRegion[][] textureRegions = TextureRegion.split(tileSet, tileSize, tileSize);

    for (int i = 0; i < textureRegions.length; i++) {
      int rowLength = textureRegions[i].length;
      for (int j = 0; j < rowLength; j++) {
        WorldTexture texture = textureFactory.create(textureRegions[i][j]);
        // tile id for tmx starts with 1
        IntKey key = bundle.get(i * rowLength + j + 1);
        tileTextureCache.put(key, texture);
      }
    }
  }
}
