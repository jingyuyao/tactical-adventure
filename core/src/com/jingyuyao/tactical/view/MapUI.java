package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.Highlighter;
import com.jingyuyao.tactical.model.object.Character;
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
    private final Label highlight;
    private final Label state;
    private final Label attackPlan;
    private final VerticalGroup buttons;
    private ImmutableList<Action> currentActions;
    private boolean showButtons = true;

    MapUI(MapState mapState, Highlighter highlighter, AnimationCounter animationCounter, Skin skin) {
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        highlight = new Label("", skin);
        state = new Label("", skin);
        attackPlan = new Label("", skin);
        buttons = new VerticalGroup();

        root.setFillParent(true);
        root.setDebug(true);
        ui.addActor(root);

        // Logical cell layout starts at top left corner
        root.top().left().pad(10);

        root.add(highlight).expandX().left();
        root.row(); // Careful to not chain anything here since it sets default for all rows
        root.add(state).expandX().left();
        root.row();
        root.add(attackPlan).expandX().left();
        root.row();
        root.add().expand(); // A filler cell that pushes the buttons to the bottom
        root.row();
        root.add(buttons).expandX().right();

        buttons.space(7);

        highlighter.addObserver(this);
        mapState.addObserver(this);
        animationCounter.addObserver(this);

        // TODO: clean me up
        state.setText("Waiting");
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
        } else if (AnimationCounter.AnimationChange.class.isInstance(param)) {
            animationChange(AnimationCounter.AnimationChange.class.cast(param));
        } else if (MapState.ShowAttackPlan.class.isInstance(param)) {
            showAttackPlan(MapState.ShowAttackPlan.class.cast(param));
        } else if (MapState.HideAttackPlan.class.isInstance(param)) {
            hideAttackPlan(MapState.HideAttackPlan.class.cast(param));
        }
    }

    private void highlightCharacter(Highlighter.HighlightCharacter highlightCharacter) {
        Character character = highlightCharacter.getCharacter();
        String text = String.format(Locale.US, "HP: %d", character.getHp());
        highlight.setText(text);
    }

    private void highlightTerrain(Highlighter.HighlightTerrain highlightTerrain) {
        Terrain terrain = highlightTerrain.getTerrain();
        String text = String.format("Type: %s", terrain.getType().toString());
        highlight.setText(text);
    }

    private void stateChange(MapState.StateChange stateChange) {
        state.setText(stateChange.getStateName());
        currentActions = stateChange.getActions();
        populateButtons();
    }

    private void animationChange(AnimationCounter.AnimationChange animationChange) {
        showButtons = !animationChange.isAnimating();
        populateButtons();
    }

    // TODO: create a nice looking widget to show this
    private void showAttackPlan(MapState.ShowAttackPlan showAttackPlan) {
        attackPlan.setText(showAttackPlan.getAttackPlan().toString());
    }

    private void hideAttackPlan(MapState.HideAttackPlan hideAttackPlan) {
        attackPlan.setText("");
    }

    private void populateButtons() {
        buttons.clear();
        if (showButtons) {
            for (Action action : currentActions) {
                buttons.addActor(createActionButton(action));
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
