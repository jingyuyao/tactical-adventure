package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jingyuyao.tactical.menu.MenuModule.MenuStage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

abstract class AbstractMenu extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final Stage stage;
  private final Table root;

  AbstractMenu(GL20 gl, Input input, @MenuStage Stage stage) {
    this.gl = gl;
    this.input = input;
    this.stage = stage;
    this.root = new VisTable(true);
    root.setFillParent(true);
    root.setBackground(VisUI.getSkin().getDrawable("window-bg"));
    this.stage.addActor(root);
  }

  @Override
  public void show() {
    input.setInputProcessor(stage);
  }

  @Override
  public void hide() {
    input.setInputProcessor(null);
  }

  @Override
  public void render(float delta) {
    stage.getViewport().apply();
    stage.act(delta);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  /**
   * The root table for this menu. Initially set to fill parent and a dark background.
   */
  Table getRoot() {
    return root;
  }
}
