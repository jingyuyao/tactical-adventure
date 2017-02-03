package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.SelectCharacter;
import com.jingyuyao.tactical.model.event.SelectTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
import com.jingyuyao.tactical.view.ui.UIFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class MapUI {

  private final Stage stage;
  private final Skin skin;
  private final Table root;
  private final Label characterLabel;
  private final Label terrainLabel;
  private final Label stateLabel;
  private final VerticalGroup actionButtons;
  private final UIFactory uiFactory;

  @Inject
  MapUI(@MapUIStage Stage stage, Skin skin, UIFactory uiFactory) {
    this.skin = skin;
    this.stage = stage;
    this.uiFactory = uiFactory;

    // Logical cell layout starts at top left corner
    root = new Table().pad(10);
    characterLabel = new Label(null, skin);
    terrainLabel = new Label(null, skin);
    stateLabel = new Label(null, skin);
    actionButtons = new VerticalGroup().space(7).columnRight();

    characterLabel.setAlignment(Align.right);

    root.setFillParent(true);
    root.setDebug(true);
    stage.addActor(root);

    // row 1
    root.row().top();
    root.add(terrainLabel).left();
    root.add(characterLabel).right();

    // row 2
    root.row().bottom();
    root.add(stateLabel).left();
    root.add(actionButtons).right().expand();
  }

  // TODO: need to refresh stats after attack
  @Subscribe
  public void highlightCharacter(SelectCharacter selectCharacter) {
    terrainLabel.setText(null);
    Character character = selectCharacter.getObject();
    characterLabel.setText(character.toString());
    characterLabel.setColor(Player.class.isInstance(character) ? Color.GREEN : Color.RED);
  }

  @Subscribe
  public void highlightTerrain(SelectTerrain selectTerrain) {
    characterLabel.setText(null);
    terrainLabel.setText(selectTerrain.getObject().toString());
  }

  @Subscribe
  public void stateChange(StateChanged stateChanged) {
    State newState = stateChanged.getObject();
    stateLabel.setText(newState.getName());
    actionButtons.clear();
    for (Action action : newState.getActions()) {
      actionButtons.addActor(uiFactory.createActionButton(action));
    }
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
