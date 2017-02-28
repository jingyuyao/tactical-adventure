package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.view.resource.ResourceModule.LoopAnimationCache;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final TextureAtlas textureAtlas;
  private final AnimationFactory animationFactory;
  private final Map<String, LoopAnimation> loopAnimationCache;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      TextureAtlas textureAtlas,
      AnimationFactory animationFactory,
      @LoopAnimationCache Map<String, LoopAnimation> loopAnimationCache) {
    this.resourceConfig = resourceConfig;
    this.loopAnimationCache = loopAnimationCache;
    this.textureAtlas = textureAtlas;
    this.animationFactory = animationFactory;
  }

  public LoopAnimation getCharacter(String characterName) {
    return getLoop(
        resourceConfig.getCharacterIdleFPS(),
        resourceConfig.getCharacterAssetPrefix() + characterName);
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
    return animationFactory.createLoop(fps, textureAtlas.findRegions(assetPath));
  }
}
