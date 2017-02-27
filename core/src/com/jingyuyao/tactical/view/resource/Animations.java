package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jingyuyao.tactical.view.resource.ResourceModule.BackingAnimationMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final Map<String, MyAnimation> animationMap;
  private final TextureAtlas textureAtlas;
  private final MyAnimationFactory myAnimationFactory;

  @Inject
  Animations(
      @BackingAnimationMap Map<String, MyAnimation> animationMap,
      TextureAtlas textureAtlas,
      MyAnimationFactory myAnimationFactory) {
    this.animationMap = animationMap;
    this.textureAtlas = textureAtlas;
    this.myAnimationFactory = myAnimationFactory;
  }

  public MyAnimation get(String name) {
    if (animationMap.containsKey(name)) {
      return animationMap.get(name);
    } else {
      MyAnimation animation = createAnimation(name);
      animationMap.put(name, animation);
      return animation;
    }
  }

  private MyAnimation createAnimation(String name) {
    return myAnimationFactory.create(textureAtlas.findRegions(name));
  }
}
