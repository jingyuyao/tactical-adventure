package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.SelectCharacter;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.ActionGroup;
import com.jingyuyao.tactical.view.ui.PlayerBurst;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MapUI {

  private final Stage stage;
  private final ActionGroup actionGroup;
  private final PlayerBurst playerBurst;

  @Inject
  MapUI(
      @MapUIStage Stage stage,
      ActionGroup actionGroup,
      PlayerBurst playerBurst) {
    this.stage = stage;
    this.actionGroup = actionGroup;
    this.playerBurst = playerBurst;
  }

  // TODO: need to refresh stats after attack
  @Subscribe
  public void selectCharacter(SelectCharacter selectCharacter) {
    playerBurst.setVisible(true);
    playerBurst.display(selectCharacter.getObject());
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    playerBurst.setVisible(false);
  }

  @Subscribe
  public void stateChange(StateChanged stateChanged) {
    actionGroup.reloadActions(stateChanged.getObject().getActions());
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
