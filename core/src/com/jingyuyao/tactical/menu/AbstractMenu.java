package com.jingyuyao.tactical.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.menu.MenuModule.MenuStage;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisWindow;

abstract class AbstractMenu extends ScreenAdapter {

  private final GL20 gl;
  private final Input input;
  private final Stage stage;
  private final VisWindow root;

  AbstractMenu(GL20 gl, Input input, @MenuStage Stage stage) {
    this.gl = gl;
    this.input = input;
    this.stage = stage;
    this.root = new VisWindow(getTitle(), false);
    root.setFillParent(true);
    root.setMovable(false);
    root.getTitleTable().padLeft(20);
    TableUtils.setSpacingDefaults(root);
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
   * The root window for this menu.
   *
   * Initially constructed with no border, default spacing, filling parent and immovable.
   */
  VisWindow getRoot() {
    return root;
  }

  /**
   * The title for the root window.
   */
  abstract String getTitle();
}
