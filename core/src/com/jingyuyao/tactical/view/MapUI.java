package com.jingyuyao.tactical.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HighlightCharacter;
import com.jingyuyao.tactical.model.event.HighlightTerrain;
import com.jingyuyao.tactical.model.event.StateChanged;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.view.ViewModule.MapUIStage;
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

  @Inject
  MapUI(@MapUIStage Stage stage, Skin skin) {
    this.skin = skin;
    this.stage = stage;

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
  public void highlightCharacter(HighlightCharacter highlightCharacter) {
    terrainLabel.setText(null);
    Character character = highlightCharacter.getObject();
    characterLabel.setText(character.toString());
    characterLabel.setColor(Player.class.isInstance(character) ? Color.GREEN : Color.RED);
  }

  @Subscribe
  public void highlightTerrain(HighlightTerrain highlightTerrain) {
    characterLabel.setText(null);
    terrainLabel.setText(highlightTerrain.getObject().toString());
  }

  @Subscribe
  public void stateChange(StateChanged stateChanged) {
    State newState = stateChanged.getObject();
    stateLabel.setText(newState.getName());
    actionButtons.clear();
    for (Action action : newState.getActions()) {
      actionButtons.addActor(createActionButton(action));
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

  private TextButton createActionButton(final Action action) {
    TextButton button = new TextButton(action.getText(), skin);
    button.getLabel().setAlignment(Align.right);
    button.getLabelCell().pad(10);
    button.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            action.run();
          }
        });
    return button;
  }
}
