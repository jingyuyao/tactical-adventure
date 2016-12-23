package com.jingyuyao.tactical.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.model.Map;
import com.jingyuyao.tactical.model.state.Action;
import com.jingyuyao.tactical.model.state.MapState;

import java.util.Observable;
import java.util.Observer;

public class MapUI {
    private final Skin skin;
    private final Stage ui;
    private final Table root;
    private final Label highlight;
    private final Label state;
    private final VerticalGroup buttons;

    MapUI(Map map, MapState mapState, Skin skin) {
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
        mapState.addObserver(this.new MapStateObserver(mapState));
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
        private final MapState mapState;

        private MapStateObserver(MapState mapState) {
            this.mapState = mapState;
            update(null, null);
        }

        @Override
        public void update(Observable observable, Object o) {
            state.setText(mapState.getStateName());

            buttons.clear();
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
    }
}
