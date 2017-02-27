package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jingyuyao.tactical.view.resource.ResourceModule.BackingAnimationMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Animations {

  private final ResourceConfig resourceConfig;
  private final Map<String, Animation<TextureRegion>> animationMap;
  private final TextureAtlas textureAtlas;
  private float stateTime = 0f;

  @Inject
  Animations(
      ResourceConfig resourceConfig,
      @BackingAnimationMap Map<String, Animation<TextureRegion>> animationMap,
      TextureAtlas textureAtlas) {
    this.resourceConfig = resourceConfig;
    this.animationMap = animationMap;
    this.textureAtlas = textureAtlas;
  }

  public float getStateTime() {
    return stateTime;
  }

  public void advanceStateTime(float delta) {
    stateTime += delta;
  }

  public Animation<TextureRegion> get(String name) {
    if (animationMap.containsKey(name)) {
      return animationMap.get(name);
    } else {
      Animation<TextureRegion> animation = createAnimation(name);
      animationMap.put(name, animation);
      return animation;
    }
  }

  private Animation<TextureRegion> createAnimation(String name) {
    return new Animation<TextureRegion>(
        resourceConfig.getFrameDuration(), textureAtlas.findRegions(name), PlayMode.LOOP);
  }
}
