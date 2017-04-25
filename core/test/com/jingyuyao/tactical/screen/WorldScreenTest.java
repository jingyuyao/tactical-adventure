package com.jingyuyao.tactical.screen;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.view.ui.WorldUI;
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
public class WorldScreenTest {

  @Mock
  private GL20 gl;
  @Mock
  private Input input;
  @Mock
  private WorldView worldView;
  @Mock
  private WorldUI worldUI;
  @Mock
  private ModelBus modelBus;
  @Mock
  private InputProcessor inputProcessor1;
  @Mock
  private InputProcessor inputProcessor2;
  @Captor
  private ArgumentCaptor<InputProcessor> argumentCaptor;

  private WorldScreen worldScreen;

  @Before
  public void setUp() {
    worldScreen = new WorldScreen(gl, input, worldView, worldUI);
  }

  @Test
  public void show() {
    when(worldUI.getInputProcessor()).thenReturn(inputProcessor1);
    when(worldView.getInputProcessor()).thenReturn(inputProcessor2);

    worldScreen.show();

    verify(worldView).center();
    verify(input).setInputProcessor(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isInstanceOf(InputMultiplexer.class);
    InputMultiplexer multiplexer = (InputMultiplexer) argumentCaptor.getValue();
    assertThat(multiplexer.getProcessors()).containsExactly(inputProcessor1, inputProcessor2);
  }

  @Test
  public void hide() {
    worldScreen.hide();

    verify(input).setInputProcessor(null);
  }

  @Test
  public void render() {
    worldScreen.render(10f);

    InOrder inOrder = Mockito.inOrder(worldView, worldUI, gl);
    inOrder.verify(worldUI).act(10f);
    inOrder.verify(gl).glClear(GL20.GL_COLOR_BUFFER_BIT);
    inOrder.verify(worldView).update(10f);
    inOrder.verify(worldUI).draw();
  }

  @Test
  public void resize() {
    worldScreen.resize(20, 30);

    verify(worldView).resize(20, 30);
    verify(worldUI).resize(20, 30);
  }

  @Test
  public void dispose() {
    worldScreen.dispose();

    verify(worldUI).dispose();
  }
}