package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;

abstract class AbstractAnimation implements Component {

  private final Animation<WorldTexture> animation;

  AbstractAnimation(int fps, WorldTexture[] keyFrames) {
    Preconditions.checkNotNull(keyFrames);
    Preconditions.checkArgument(keyFrames.length > 0, "did you forget to pack textures?");
    this.animation = new Animation<>(1f / fps, keyFrames);
    this.animation.setPlayMode(getPlayMode());
  }

  Animation<WorldTexture> getAnimation() {
    return animation;
  }

  abstract PlayMode getPlayMode();
}
