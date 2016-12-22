package com.jingyuyao.tactical.view;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MapUI {
    private final Skin skin;
    private final Stage ui;
    private final Table root;

    public MapUI(Skin skin) {
        this.skin = skin;
        ui = new Stage();
        root = new Table();
        root.setFillParent(true);
        root.setDebug(true);
        ui.addActor(root);

        root.add(new TextButton("HELLO", skin));
        root.add(new TextButton("WORLD", skin));
        root.row();
        root.add(new TextButton("NI", skin));
        root.add(new TextButton("HAO", skin));
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
}
