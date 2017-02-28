package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.view.resource.ResourceModule.BackingAnimationMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final Map<String, MyAnimation> animationMap;
  private final TextureAtlas textureAtlas;
  private final MyAnimationFactory myAnimationFactory;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      @BackingAnimationMap Map<String, MyAnimation> animationMap,
      TextureAtlas textureAtlas,
      MyAnimationFactory myAnimationFactory) {
    this.resourceConfig = resourceConfig;
    this.animationMap = animationMap;
    this.textureAtlas = textureAtlas;
    this.myAnimationFactory = myAnimationFactory;
  }

  public MyAnimation getCharacter(String characterName) {
    return getFromAssetName(resourceConfig.getCharacterAssetPrefix() + characterName);
  }

  private MyAnimation getFromAssetName(String assetPath) {
    if (animationMap.containsKey(assetPath)) {
      return animationMap.get(assetPath);
    } else {
      MyAnimation animation = createAnimation(resourceConfig.getCharacterIdleFPS(), assetPath);
      animationMap.put(assetPath, animation);
      return animation;
    }
  }

  private MyAnimation createAnimation(int fps, String assetPath) {
    return myAnimationFactory.create(fps, textureAtlas.findRegions(assetPath));
  }
}
