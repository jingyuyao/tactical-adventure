package com.jingyuyao.tactical.view.world.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.view.world.component.LoopAnimation;
import com.jingyuyao.tactical.view.world.component.SingleAnimation;
import com.jingyuyao.tactical.view.world.resource.ResourceModule.AtlasRegionsCache;
import com.jingyuyao.tactical.view.world.resource.ResourceModule.LoopAnimationCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final TextureAtlas textureAtlas;
  private final TextureFactory textureFactory;
  private final Map<StringKey, LoopAnimation> loopAnimationCache;
  private final Map<StringKey, WorldTexture[]> atlasRegionsCache;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      TextureAtlas textureAtlas,
      TextureFactory textureFactory,
      @LoopAnimationCache Map<StringKey, LoopAnimation> loopAnimationCache,
      @AtlasRegionsCache Map<StringKey, WorldTexture[]> atlasRegionsCache) {
    this.resourceConfig = resourceConfig;
    this.textureFactory = textureFactory;
    this.loopAnimationCache = loopAnimationCache;
    this.textureAtlas = textureAtlas;
    this.atlasRegionsCache = atlasRegionsCache;
  }

  public LoopAnimation getLoop(StringKey key) {
    if (loopAnimationCache.containsKey(key)) {
      return loopAnimationCache.get(key);
    } else {
      LoopAnimation animation =
          new LoopAnimation(resourceConfig.getLoopFps(), getAtlasRegions(key));
      loopAnimationCache.put(key, animation);
      return animation;
    }
  }

  public SingleAnimation getSingle(StringKey key) {
    return new SingleAnimation(resourceConfig.getSingleFps(), getAtlasRegions(key));
  }

  private WorldTexture[] getAtlasRegions(StringKey key) {
    if (atlasRegionsCache.containsKey(key)) {
      return atlasRegionsCache.get(key);
    } else {
      WorldTexture[] worldTextures = createAtlasRegions(key);
      atlasRegionsCache.put(key, worldTextures);
      return worldTextures;
    }
  }

  private WorldTexture[] createAtlasRegions(StringKey key) {
    Array<AtlasRegion> regions = textureAtlas.findRegions(key.getPath());
    WorldTexture[] worldTextures = new WorldTexture[regions.size];
    for (int i = 0; i < regions.size; i++) {
      worldTextures[i] = textureFactory.create(regions.get(i));
    }
    return worldTextures;
  }
}
