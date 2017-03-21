package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.jingyuyao.tactical.view.resource.ResourceModule.LoopAnimationCache;
import com.jingyuyao.tactical.view.resource.ResourceModule.WorldTextureCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final TextureAtlas textureAtlas;
  private final AnimationFactory animationFactory;
  private final TextureFactory textureFactory;
  private final Map<String, LoopAnimation> loopAnimationCache;
  private final Map<String, WorldTexture[]> atlasRegionsCache;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      TextureAtlas textureAtlas,
      AnimationFactory animationFactory,
      TextureFactory textureFactory,
      @LoopAnimationCache Map<String, LoopAnimation> loopAnimationCache,
      @WorldTextureCache Map<String, WorldTexture[]> atlasRegionsCache) {
    this.resourceConfig = resourceConfig;
    this.textureFactory = textureFactory;
    this.loopAnimationCache = loopAnimationCache;
    this.textureAtlas = textureAtlas;
    this.animationFactory = animationFactory;
    this.atlasRegionsCache = atlasRegionsCache;
  }

  public LoopAnimation getCharacter(String characterName) {
    return getLoop(
        resourceConfig.getCharacterIdleFPS(),
        resourceConfig.getCharacterAssetPrefix() + characterName);
  }

  public SingleAnimation getWeapon(String weaponName) {
    return getSingle(
        resourceConfig.getWeaponFPS(),
        resourceConfig.getWeaponAssetPrefix() + weaponName);
  }

  private SingleAnimation getSingle(int fps, String assetPath) {
    return animationFactory.createSingle(fps, getAtlasRegions(assetPath));
  }

  private LoopAnimation getLoop(int fps, String assetPath) {
    if (loopAnimationCache.containsKey(assetPath)) {
      return loopAnimationCache.get(assetPath);
    } else {
      LoopAnimation animation = createLoop(fps, assetPath);
      loopAnimationCache.put(assetPath, animation);
      return animation;
    }
  }

  private LoopAnimation createLoop(int fps, String assetPath) {
    return animationFactory.createLoop(fps, getAtlasRegions(assetPath));
  }

  private WorldTexture[] getAtlasRegions(String assetPath) {
    if (atlasRegionsCache.containsKey(assetPath)) {
      return atlasRegionsCache.get(assetPath);
    } else {
      WorldTexture[] worldTextures = createAtlasRegions(assetPath);
      atlasRegionsCache.put(assetPath, worldTextures);
      return worldTextures;
    }
  }

  private WorldTexture[] createAtlasRegions(String assetPath) {
    Array<AtlasRegion> regions = textureAtlas.findRegions(assetPath);
    WorldTexture[] worldTextures = new WorldTexture[regions.size];
    for (int i = 0; i < regions.size; i++) {
      worldTextures[i] = textureFactory.create(regions.get(i));
    }
    return worldTextures;
  }
}
