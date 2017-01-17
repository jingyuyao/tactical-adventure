package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.view.actor.ActorAnnotations.ActorWorldSize;
import javax.inject.Inject;

/**
 * Only dispatches clicked events if the click begins and ends over the actor.
 */
public class MapActorController extends ClickListener {

  private final MapState mapState;
  private final MapObject object;
  private final InputLock inputLock;

  @Inject
  MapActorController(@Assisted MapObject object, MapState mapState, @ActorWorldSize float size,
      InputLock inputLock) {
    this.object = object;
    this.mapState = mapState;
    this.inputLock = inputLock;
    setTapSquareSize(size / 2f);
  }

  @Override
  public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
    super.enter(event, x, y, pointer, fromActor);
    object.highlight(mapState);
  }

  @Override
  public void clicked(InputEvent event, float x, float y) {
    if (inputLock.isLocked()) {
      return;
    }
    object.select(mapState);
  }
}
