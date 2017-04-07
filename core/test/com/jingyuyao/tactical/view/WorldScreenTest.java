package com.jingyuyao.tactical.view;

import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.GL20;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world.WorldView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldScreenTest {

  @Mock
  private GL20 gl;
  @Mock
  private WorldView worldView;
  @Mock
  private UI ui;
  @Mock
  private WorldController worldController;
  @Mock
  private WorldScreen worldScreen;
  @Mock
  private EventBus eventBus;

  @Before
  public void setUp() {
    worldScreen = new WorldScreen(gl, worldView, ui, worldController);
  }

  @Test
  public void register() {
    worldScreen.register(eventBus);

    verify(worldView).register(eventBus);
    verify(ui).register(eventBus);
  }

  @Test
  public void show() {
    worldScreen.show();

    verify(worldController).receiveInput();
  }

  @Test
  public void hide() {
    worldScreen.hide();

    verify(worldController).stopReceivingInput();
  }

  @Test
  public void render() {
    worldScreen.render(10f);

    InOrder inOrder = Mockito.inOrder(worldView, ui, gl);
    inOrder.verify(ui).act(10f);
    inOrder.verify(gl).glClear(GL20.GL_COLOR_BUFFER_BIT);
    inOrder.verify(worldView).update(10f);
    inOrder.verify(ui).draw();
  }

  @Test
  public void resize() {
    worldScreen.resize(20, 30);

    verify(worldView).resize(20, 30);
    verify(ui).resize(20, 30);
  }

  @Test
  public void dispose() {
    worldScreen.dispose();

    verify(ui).dispose();
  }
}