package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.map.Cell;
import javax.inject.Inject;

public class CellController extends InputListener {

  private final WorldCamera worldCamera;
  private final Model model;
  private final Cell cell;

  @Inject
  CellController(WorldCamera worldCamera, Model model, @Assisted Cell cell) {
    this.worldCamera = worldCamera;
    this.model = model;
    this.cell = cell;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    return true;
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    if (!worldCamera.isDragged()) {
      model.select(cell);
    }
  }
}
