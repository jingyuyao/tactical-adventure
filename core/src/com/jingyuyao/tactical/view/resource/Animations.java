package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.view.resource.ResourceModule.BackingAnimationMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final Map<String, LoopAnimation> animationMap;
  private final TextureAtlas textureAtlas;
  private final AnimationFactory animationFactory;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      @BackingAnimationMap Map<String, LoopAnimation> animationMap,
      TextureAtlas textureAtlas,
      AnimationFactory animationFactory) {
    this.resourceConfig = resourceConfig;
    this.animationMap = animationMap;
    this.textureAtlas = textureAtlas;
    this.animationFactory = animationFactory;
  }

  public LoopAnimation getCharacter(String characterName) {
    return getFromAssetName(resourceConfig.getCharacterAssetPrefix() + characterName);
  }

  private LoopAnimation getFromAssetName(String assetPath) {
    if (animationMap.containsKey(assetPath)) {
      return animationMap.get(assetPath);
    } else {
      LoopAnimation animation = createAnimation(resourceConfig.getCharacterIdleFPS(), assetPath);
      animationMap.put(assetPath, animation);
      return animation;
    }
  }

  private LoopAnimation createAnimation(int fps, String assetPath) {
    return animationFactory.create(fps, textureAtlas.findRegions(assetPath));
  }
}
