package com.jingyuyao.tactical.view.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ViewUtil {

  /**
   * Draw {@link Sprite} using {@link Batch} with dimension from {@link Actor}.
   */
  public static void draw(Batch batch, Sprite sprite, Actor actor) {
    sprite.setBounds(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
    sprite.draw(batch);
  }

  /**
   * Draw {@link TextureRegion} using {@link Batch} with dimension from {@link Actor}.
   */
  public static void draw(Batch batch, TextureRegion textureRegion, Actor actor) {
    batch.draw(textureRegion, actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
  }
}
