package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.controller.WorldCamera;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.model.ModelBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldViewTest {

  @Mock
  private WorldEngine worldEngine;
  @Mock
  private OrthogonalTiledMapRenderer mapRenderer;
  @Mock
  private Viewport viewport;
  @Mock
  private WorldCamera worldCamera;
  @Mock
  private WorldController worldController;
  @Mock
  private ModelBus modelBus;
  @Mock
  private OrthographicCamera camera;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(worldEngine, mapRenderer, viewport, worldCamera, worldController);
  }

  @Test
  public void input_processor() {
    InputProcessor processor = worldView.getInputProcessor();
    assertThat(processor).isInstanceOf(InputMultiplexer.class);
    InputMultiplexer multiplexer = (InputMultiplexer) processor;
    assertThat(multiplexer.getProcessors()).containsExactly(worldCamera, worldController).inOrder();
  }

  @Test
  public void center() {
    worldView.center();

    verify(worldCamera).center();
  }

  @Test
  public void update() {
    when(viewport.getCamera()).thenReturn(camera);

    worldView.update(5f);

    InOrder inOrder = Mockito.inOrder(viewport, mapRenderer, worldEngine);
    inOrder.verify(viewport).apply();
    inOrder.verify(mapRenderer).setView(camera);
    inOrder.verify(mapRenderer).render();
    inOrder.verify(worldEngine).update(5f);
  }

  @Test
  public void resize() {
    worldView.resize(11, 12);

    verify(viewport).update(11, 12);
  }
}