package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.event.ActivatedPlayer;
import com.jingyuyao.tactical.model.event.DeactivatedPlayer;
import com.jingyuyao.tactical.model.event.SelectEnemy;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.ActionGroup;
import com.jingyuyao.tactical.view.ui.EnemyBurst;
import com.jingyuyao.tactical.view.ui.PlayerBurst;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: need to refresh stats after attack
@Singleton
class MapUI {

  private final Stage stage;
  private final ActionGroup actionGroup;
  private final EnemyBurst enemyBurst;
  private final PlayerBurst playerBurst;

  @Inject
  MapUI(
      @MapUIStage Stage stage,
      ActionGroup actionGroup,
      EnemyBurst enemyBurst,
      PlayerBurst playerBurst) {
    this.stage = stage;
    this.actionGroup = actionGroup;
    this.enemyBurst = enemyBurst;
    this.playerBurst = playerBurst;
  }

  @Subscribe
  public void activatedPlayer(ActivatedPlayer activatedPlayer) {
    playerBurst.setVisible(true);
    playerBurst.display(activatedPlayer.getObject());
  }

  @Subscribe
  public void deactivatedPlayer(DeactivatedPlayer deactivatedPlayer) {
    playerBurst.setVisible(false);
  }

  @Subscribe
  public void selectEnemy(SelectEnemy selectEnemy) {
    enemyBurst.setVisible(true);
    enemyBurst.display(selectEnemy.getObject());
  }

  @Subscribe
  public void selectTerrain(SelectTerrain selectTerrain) {
    enemyBurst.setVisible(false);
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
