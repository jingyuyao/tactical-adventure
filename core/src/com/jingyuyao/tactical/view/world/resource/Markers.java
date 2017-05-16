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
    return get("highlight");
  }

  public WorldTexture getActivated() {
    return get("activated");
  }

  public WorldTexture getMove() {
    return get("move");
  }

  public WorldTexture getTargetSelect() {
    return get("target_select");
  }

  public WorldTexture getAttack() {
    return get("attack");
  }

  private WorldTexture get(String markerName) {
    String resourceName = "texture/ui/marking/" + markerName;
    if (markerTextureCache.containsKey(resourceName)) {
      return markerTextureCache.get(resourceName);
    } else {
      WorldTexture texture = textureFactory.create(textureAtlas.findRegion(resourceName));
      markerTextureCache.put(resourceName, texture);
      return texture;
    }
  }
}
