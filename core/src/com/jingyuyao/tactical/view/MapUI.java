package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.ReviewingAttack;

import java.util.Locale;

public class MapUI {
    private final Skin skin;
    private final Stage ui;
    private final Table root;
    private final Label characterLabel;
    private final Label terrainLabel;
    private final Label stateLabel;
    private final Label attackPlan;
    private final VerticalGroup actionButtons;
    private ImmutableList<Action> currentActions;
    private boolean showButtons = true;

    // TODO: get rid of map state as well...
    MapUI(EventBus eventBus, MapState mapState, Skin skin) {
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        characterLabel = new Label(null, skin);
        terrainLabel = new Label(null, skin);
        stateLabel = new Label(null, skin);
        attackPlan = new Label(null, skin);
        actionButtons = new VerticalGroup().space(7);

        root.setFillParent(true);
        root.setDebug(true);
        ui.addActor(root);

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
        root.add(attackPlan).center().bottom();
        root.add(actionButtons).right().bottom();

        eventBus.register(this);

        // TODO: clean me up
        stateLabel.setText("Waiting");
        currentActions = mapState.getActions();
        populateButtons();
    }

    public Stage getUi() {
        return ui;
    }

    void act(float delta) {
        ui.act(delta);
    }

    void draw() {
        ui.getViewport().apply();
        ui.draw();
    }

    void resize(int width, int height) {
        ui.getViewport().update(width, height);
    }

    void dispose() {
        ui.dispose();
    }

    // TODO: need to refresh stats after attack
    @Subscribe
    public void highlightCharacter(Highlighter.CharacterAndTerrain characterAndTerrain) {
        characterLabel.setText(String.format(Locale.US, "HP: %d", characterAndTerrain.getCharacter().getHp()));
        updateTerrainLabel(characterAndTerrain.getTerrain());
    }

    @Subscribe
    public void highlightTerrain(Highlighter.JustTerrain justTerrain) {
        characterLabel.setText(null);
        updateTerrainLabel(justTerrain.getTerrain());
    }

    @Subscribe
    public void stateChange(MapState.StateChange stateChange) {
        stateLabel.setText(stateChange.getStateName());
        currentActions = stateChange.getActions();
        populateButtons();
    }

    @Subscribe
    public void waitChange(Waiter.WaitChange waitChange) {
        showButtons = !waitChange.isWaiting();
        populateButtons();
    }

    // TODO: create a nice looking widget to show this
    @Subscribe
    public void showAttackPlan(ReviewingAttack.ShowAttackPlan showAttackPlan) {
        attackPlan.setText(showAttackPlan.getAttackPlan().toString());
    }

    @Subscribe
    public void hideAttackPlan(ReviewingAttack.HideAttackPlan hideAttackPlan) {
        attackPlan.setText(null);
    }

    private void updateTerrainLabel(Terrain terrain) {
        terrainLabel.setText(String.format("Type: %s", terrain.getType().toString()));
    }

    private void populateButtons() {
        actionButtons.clear();
        if (showButtons) {
            for (Action action : currentActions) {
                actionButtons.addActor(createActionButton(action));
            }
        }
    }

    private TextButton createActionButton(final Action action) {
        TextButton button = new TextButton(action.getName(), skin);
        button.pad(7, 10, 7, 10);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }
}
