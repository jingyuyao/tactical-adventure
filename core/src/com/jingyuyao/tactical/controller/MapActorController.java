package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.view.actor.ActorModule.ActorWorldSize;
import javax.inject.Inject;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
public class MapActorController extends ClickListener {

  private final MapState mapState;
  private final MapObject object;

  @Inject
  MapActorController(@Assisted MapObject object, MapState mapState, @ActorWorldSize float size) {
    this.object = object;
    this.mapState = mapState;
    setTapSquareSize(size / 2f);
  }

  @Override
  public void clicked(InputEvent event, float x, float y) {
    object.select(mapState);
  }
}
