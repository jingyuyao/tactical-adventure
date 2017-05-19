package com.jingyuyao.tactical.view;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.controller.CameraController;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.ui.GameUI;
import com.jingyuyao.tactical.view.world.WorldView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameScreenTest {

  @Mock
  private GL20 gl;
  @Mock
  private Input input;
  @Mock
  private WorldView worldView;
  @Mock
  private GameUI gameUI;
  @Mock
  private CameraController cameraController;
  @Mock
  private WorldController worldController;
  @Mock
  private InputProcessor worldUIProcessor;
  @Captor
  private ArgumentCaptor<InputProcessor> argumentCaptor;

  private GameScreen gameScreen;

  @Before
  public void setUp() {
    when(gameUI.getInputProcessor()).thenReturn(worldUIProcessor);
    gameScreen = new GameScreen(gl, input, worldView, gameUI, cameraController, worldController);
  }

  @Test
  public void show() {
    gameScreen.show();

    verify(input).setInputProcessor(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(InputMultiplexer.class);
    InputMultiplexer multiplexer = (InputMultiplexer) argumentCaptor.getValue();
    assertThat(multiplexer.getProcessors())
        .containsExactly(worldUIProcessor, cameraController, worldController).inOrder();
  }

  @Test
  public void hide() {
    gameScreen.hide();

    verify(input).setInputProcessor(null);
  }

  @Test
  public void render() {
    gameScreen.render(10f);

    InOrder inOrder = Mockito.inOrder(worldView, gameUI, gl);
    inOrder.verify(gameUI).act(10f);
    inOrder.verify(gl).glClear(GL20.GL_COLOR_BUFFER_BIT);
    inOrder.verify(worldView).update(10f);
    inOrder.verify(gameUI).draw();
  }

  @Test
  public void resize() {
    gameScreen.resize(20, 30);

    verify(worldView).resize(20, 30);
    verify(gameUI).resize(20, 30);
  }

  @Test
  public void dispose() {
    gameScreen.dispose();

    verify(gameUI).dispose();
  }
}