package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.view.world.resource.ResourceModule.MarkerTextureCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markers {

  private final TextureAtlas textureAtlas;
  private final TextureFactory textureFactory;
  private final Map<String, WorldTexture> markerTextureCache;

  @Inject
  Markers(
      TextureAtlas textureAtlas,
      TextureFactory textureFactory,
      @MarkerTextureCache Map<String, WorldTexture> markerTextureCache) {
    this.textureAtlas = textureAtlas;
    this.textureFactory = textureFactory;
    this.markerTextureCache = markerTextureCache;
  }

  public WorldTexture getHighlight() {
    return get("marking/highlight");
  }

  public WorldTexture getActivated() {
    return get("marking/activated");
  }

  public WorldTexture getMove() {
    return get("marking/move");
  }

  public WorldTexture getTargetSelect() {
    return get("marking/target_select");
  }

  public WorldTexture getAttack() {
    return get("marking/attack");
  }

  private WorldTexture get(String fileName) {
    if (markerTextureCache.containsKey(fileName)) {
      return markerTextureCache.get(fileName);
    } else {
      WorldTexture texture = textureFactory.create(textureAtlas.findRegion(fileName));
      markerTextureCache.put(fileName, texture);
      return texture;
    }
  }
}
