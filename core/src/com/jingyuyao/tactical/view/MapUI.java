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

public class MapUI implements Observer {
    private final Map map;
    private final MapState mapState;
    private final Skin skin;
    private final Stage ui;
    private final Table root;
    private final Label highlight;
    private final VerticalGroup buttons;

    public MapUI(Map map, MapState mapState, Skin skin) {
        this.map = map;
        this.mapState = mapState;
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        highlight = new Label("test", skin);
        buttons = new VerticalGroup();

        root.setFillParent(true);
        root.setDebug(true);
        ui.addActor(root);

        // Logical cell layout starts at top left corner
        root.top().left().pad(10);

        root.add(highlight).expandX().left();
        root.row(); // Careful to not chain anything here since it sets default for all rows
        root.add().expand(); // A filler cell that pushes the buttons to the bottom
        root.row();
        root.add(buttons).expandX().right();

        buttons.space(5);
        buttons.addActor(new TextButton("Hello", skin));
        buttons.addActor(new TextButton("World", skin));

        // TODO: different observers?
        map.addObserver(this);
        mapState.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        updateHighlight();
        updateActions();
    }

    public Stage getUi() {
        return ui;
    }

    public void act(float delta) {
        ui.act(delta);
    }

    public void draw() {
        ui.draw();
    }

    public void resize(int width, int height) {
        ui.getViewport().update(width, height);
    }

    public void dispose() {
        ui.dispose();
    }

    private void updateHighlight() {
        highlight.setText(map.getHighlight().getCoordinate().toString());
    }

    private void updateActions() {
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
