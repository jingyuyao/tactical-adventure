package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.view.world.resource.ResourceModule.MarkerTextureCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markers {

  static final KeyBundle MARKER_BUNDLE = KeyBundle.texture("ui/marking");

  private final TextureAtlas textureAtlas;
  private final TextureFactory textureFactory;
  private final Map<StringKey, WorldTexture> markerTextureCache;

  @Inject
  Markers(
      TextureAtlas textureAtlas,
      TextureFactory textureFactory,
      @MarkerTextureCache Map<StringKey, WorldTexture> markerTextureCache) {
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
    StringKey key = MARKER_BUNDLE.get(markerName);
    if (markerTextureCache.containsKey(key)) {
      return markerTextureCache.get(key);
    } else {
      WorldTexture texture = textureFactory.create(textureAtlas.findRegion(key.getPath()));
      markerTextureCache.put(key, texture);
      return texture;
    }
  }
}
