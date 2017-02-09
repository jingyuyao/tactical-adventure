package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectPlayer;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.ActionGroup;
import com.jingyuyao.tactical.view.ui.Info;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: need to refresh stats after attack
@Singleton
class MapUI {

  private final Stage stage;
  private final ActionGroup actionGroup;
  private final Info info;
  private final Terrains terrains;

  @Inject
  MapUI(@MapUIStage Stage stage, ActionGroup actionGroup, Info info, Terrains terrains) {
    this.stage = stage;
    this.terrains = terrains;
    this.actionGroup = actionGroup;
    this.info = info;
  }

  @Subscribe
  public void selectPlayer(SelectPlayer selectPlayer) {
    info.display(selectPlayer.getObject(), terrains.get(selectPlayer.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    info.display(selectEnemy.getObject(), terrains.get(selectEnemy.getObject().getCoordinate()));
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    info.display(selectTerrain.getObject());
  }

  @Subscribe
  public void state(State state) {
    actionGroup.reloadActions(state.getActions());
  }

  void act(float delta) {
    stage.act(delta);
  }

  void draw() {
    stage.getViewport().apply();
    stage.draw();
  }

  void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  void dispose() {
    stage.dispose();
  }
}
