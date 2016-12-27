package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.Waiter;
import com.jingyuyao.tactical.model.object.Terrain;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MapUI implements Observer {
    private final Skin skin;
    private final Stage ui;
    private final Table root;
    private final Label characterLabel;
    private final Label terrainLabel;
    private final Label stateLabel;
    private final VerticalGroup actionButtons;
    private ImmutableList<Action> currentActions;
    private boolean showButtons = true;

    MapUI(MapState mapState, Highlighter highlighter, Waiter waiter, Skin skin) {
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        characterLabel = new Label(null, skin);
        terrainLabel = new Label(null, skin);
        stateLabel = new Label(null, skin);
        actionButtons = new VerticalGroup().space(7);

        root.setFillParent(true);
        root.setDebug(true);
        ui.addActor(root);

        // Logical cell layout starts at top left corner
        root.top().left().pad(10);

        // row 1
        root.add(characterLabel).left().top();
        root.add(terrainLabel).right().top();

        // row 2
        root.row(); // Careful to not chain anything here since it sets default for all rows
        root.add().expand(); // A filler cell that pushes the buttons to the bottom

        // row 3
        root.row();
        root.add(stateLabel).left().bottom();
        root.add(actionButtons).right().bottom();

        highlighter.addObserver(this);
        mapState.addObserver(this);
        waiter.addObserver(this);

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

    @Override
    public void update(Observable observable, Object param) {
        // TODO: Hum... can we make this shorter? generics? visitor?
        if (Highlighter.HighlightTerrain.class.isInstance(param)) {
            highlightTerrain(Highlighter.HighlightTerrain.class.cast(param));
        } else if (Highlighter.HighlightCharacter.class.isInstance(param)) {
            highlightCharacter(Highlighter.HighlightCharacter.class.cast(param));
        } else if (MapState.StateChange.class.isInstance(param)) {
            stateChange(MapState.StateChange.class.cast(param));
        } else if (Waiter.Change.class.isInstance(param)) {
            animationChange(Waiter.Change.class.cast(param));
        } else if (MapState.ShowAttackPlan.class.isInstance(param)) {
            showAttackPlan(MapState.ShowAttackPlan.class.cast(param));
        } else if (MapState.HideAttackPlan.class.isInstance(param)) {
            hideAttackPlan(MapState.HideAttackPlan.class.cast(param));
        }
    }

    // TODO: need to refresh stats after attack
    private void highlightCharacter(Highlighter.HighlightCharacter highlightCharacter) {
        characterLabel.setText(String.format(Locale.US, "HP: %d", highlightCharacter.getCharacter().getHp()));
        updateTerrainLabel(highlightCharacter.getTerrain());
    }

    private void highlightTerrain(Highlighter.HighlightTerrain highlightTerrain) {
        characterLabel.setText(null);
        updateTerrainLabel(highlightTerrain.getTerrain());
    }

    private void stateChange(MapState.StateChange stateChange) {
        stateLabel.setText(stateChange.getStateName());
        currentActions = stateChange.getActions();
        populateButtons();
    }

    private void animationChange(Waiter.Change change) {
        showButtons = !change.isWaiting();
        populateButtons();
    }

    // TODO: create a nice looking widget to show this
    private void showAttackPlan(MapState.ShowAttackPlan showAttackPlan) {
        stateLabel.setText(showAttackPlan.getAttackPlan().toString());
    }

    private void hideAttackPlan(MapState.HideAttackPlan hideAttackPlan) {
        stateLabel.setText(null);
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
