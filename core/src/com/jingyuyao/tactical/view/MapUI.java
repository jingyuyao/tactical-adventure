package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.AnimationCounter;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;
import java.util.Observer;

public class MapUI {
    private final MapState mapState;
    private final Skin skin;
    private final Stage ui;
    private final Table root;
    private final Label highlight;
    private final Label state;
    private final VerticalGroup buttons;

    MapUI(Map map, MapState mapState, AnimationCounter animationCounter, Skin skin) {
        this.mapState = mapState;
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        highlight = new Label("", skin);
        state = new Label("", skin);
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
        root.add().expand(); // A filler cell that pushes the buttons to the bottom
        root.row();
        root.add(buttons).expandX().right();

        buttons.space(5);

        map.addObserver(this.new MapObserver(map));
        mapState.addObserver(this.new MapStateObserver());
        animationCounter.addObserver(this.new AnimationCounterObserver(animationCounter));
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

    private void populateButtons() {
        for (Action action : mapState.getActions()) {
            buttons.addActor(createActionButton(action));
        }
    }

    private TextButton createActionButton(final Action action) {
        TextButton button = new TextButton(action.getName(), skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }

    private class MapObserver implements Observer {
        private final Map map;

        private MapObserver(Map map) {
            this.map = map;
        }

        @Override
        public void update(Observable observable, Object o) {
            highlight.setText(map.getHighlight().toString());
        }
    }

    private class MapStateObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            state.setText(mapState.getStateName());

            buttons.clear();
            populateButtons();
        }
    }

    private class AnimationCounterObserver implements Observer {
        private final AnimationCounter animationCounter;

        private AnimationCounterObserver(AnimationCounter animationCounter) {
            this.animationCounter = animationCounter;
        }

        @Override
        public void update(Observable observable, Object o) {
            buttons.clear();
            if (!animationCounter.isAnimating()) {
                populateButtons();
            }
        }
    }
}
