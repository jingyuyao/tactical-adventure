package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import javax.inject.Inject;

public class WorldActorController extends InputListener {

  private final WorldCamera worldCamera;
  private final SelectionHandler selectionHandler;
  private final MapObject object;

  @Inject
  WorldActorController(
      SelectionHandler selectionHandler,
      WorldCamera worldCamera,
      @Assisted MapObject object) {
    this.worldCamera = worldCamera;
    this.selectionHandler = selectionHandler;
    this.object = object;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    return true;
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    if (!worldCamera.isDragged()) {
      object.select(selectionHandler);
    }
  }
}
