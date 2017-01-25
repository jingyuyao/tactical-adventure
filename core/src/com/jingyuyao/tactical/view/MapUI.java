package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.event.HighlightCharacter;
import com.jingyuyao.tactical.model.state.event.HighlightTerrain;
import com.jingyuyao.tactical.model.state.event.StateChanged;
import com.jingyuyao.tactical.view.ViewAnnotations.MapUiStage;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MapUI {

  private final Stage stage;
  private final Skin skin;
  private final Table root;
  private final Label characterLabel;
  private final Label terrainLabel;
  private final Label stateLabel;
  private final VerticalGroup actionButtons;
  private ImmutableList<Action> currentActions;

  @Inject
  MapUI(@MapUiStage Stage stage, Skin skin) {
    this.skin = skin;
    this.stage = stage;
    root = new Table();
    characterLabel = new Label(null, skin);
    terrainLabel = new Label(null, skin);
    stateLabel = new Label(null, skin);
    actionButtons = new VerticalGroup().space(7);

    root.setFillParent(true);
    root.setDebug(true);
    stage.addActor(root);

    // Logical cell layout starts at top left corner
    root.top().left().pad(10);

    // row 1
    root.add(characterLabel).left().top();
    root.add(); // filler since we want 3x3 table
    root.add(terrainLabel).right().top();

    // row 2
    root.row(); // Careful to not chain anything here since it sets default for all rows
    root.add().expand(); // A filler cell that pushes the buttons to the bottom

    // row 3
    root.row();
    root.add(stateLabel).left().bottom();
    root.add();
    root.add(actionButtons).right().bottom();
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

  // TODO: need to refresh stats after attack
  @Subscribe
  public void highlightCharacter(HighlightCharacter highlightCharacter) {
    characterLabel.setText(
        String.format(Locale.US, "HP: %d", highlightCharacter.getObject().getHp()));
  }

  @Subscribe
  public void highlightTerrain(HighlightTerrain highlightTerrain) {
    characterLabel.setText(null);
    updateTerrainLabel(highlightTerrain.getObject());
  }

  @Subscribe
  public void stateChange(StateChanged stateChanged) {
    State newState = stateChanged.getObject();
    stateLabel.setText(newState.getName());
    currentActions = newState.getActions();
    populateButtons();
  }

  private void updateTerrainLabel(Terrain terrain) {
    terrainLabel.setText(String.format("Type: %s", terrain.getType().toString()));
  }

  private void populateButtons() {
    actionButtons.clear();
    for (Action action : currentActions) {
      actionButtons.addActor(createActionButton(action));
    }
  }

  private TextButton createActionButton(final Action action) {
    TextButton button = new TextButton(action.getName(), skin);
    button.pad(7, 10, 7, 10);
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
