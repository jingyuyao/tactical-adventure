package com.jingyuyao.tactical.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.marking.MarkingSubscriber;
import com.jingyuyao.tactical.view.marking.WorldMarkings;
import com.jingyuyao.tactical.view.ui.UISubscriber;
import com.jingyuyao.tactical.view.ui.WorldUI;
import com.jingyuyao.tactical.view.world.World;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldScreen extends ScreenAdapter {

  private final Batch batch;
  private final World world;
  private final WorldMarkings worldMarkings;
  private final WorldUI worldUI;
  private final WorldController worldController;
  private final MarkingSubscriber markingSubscriber;
  private final UISubscriber uiSubscriber;

  @Inject
  WorldScreen(
      Batch batch,
      World world,
      WorldMarkings worldMarkings,
      WorldUI worldUI,
      WorldController worldController,
      MarkingSubscriber markingSubscriber,
      UISubscriber uiSubscriber) {
    this.batch = batch;
    this.world = world;
    this.worldMarkings = worldMarkings;
    this.worldUI = worldUI;
    this.worldController = worldController;
    this.markingSubscriber = markingSubscriber;
    this.uiSubscriber = uiSubscriber;
  }

  public void registerListeners(EventBus eventBus) {
    eventBus.register(world);
    eventBus.register(markingSubscriber);
    eventBus.register(uiSubscriber);
  }

  @Override
  public void show() {
    worldController.receiveInput();
  }

  @Override
  public void hide() {
    worldController.stopReceivingInput();
  }

  @Override
  public void render(float delta) {
    worldUI.act(delta);
    worldMarkings.act(delta);
    world.act(delta);

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    world.draw();
    worldMarkings.draw();
    worldUI.draw();
  }

  @Override
  public void resize(int width, int height) {
    // This is very important...
    world.resize(width, height);
    worldUI.resize(width, height);
  }

  @Override
  public void dispose() {
    world.dispose();
    worldUI.dispose();
    batch.dispose();
  }
}
