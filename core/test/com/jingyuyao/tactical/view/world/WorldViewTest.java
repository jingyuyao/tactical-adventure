package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.world.system.Systems;
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
  private Batch batch;
  @Mock
  private Viewport viewport;
  @Mock
  private OrthogonalTiledMapRenderer mapRenderer;
  @Mock
  private Engine engine;
  @Mock
  private Systems systems;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private Vector3 vector3;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(batch, viewport, mapRenderer, engine, systems);
    verify(systems).addTo(engine);
  }

  @Test
  public void update() {
    when(viewport.getCamera()).thenReturn(camera);

    worldView.update(5f);

    InOrder inOrder = Mockito.inOrder(viewport, mapRenderer, engine, batch);
    inOrder.verify(viewport).apply();
    inOrder.verify(mapRenderer).setView(camera);
    inOrder.verify(mapRenderer).render();
    inOrder.verify(batch).begin();
    inOrder.verify(batch).setProjectionMatrix(camera.combined);
    inOrder.verify(engine).update(5f);
    inOrder.verify(batch).end();
  }

  @Test
  public void resize() {
    worldView.resize(11, 12);

    verify(viewport).update(11, 12);
  }

  @Test
  public void unproject() {
    when(viewport.getCamera()).thenReturn(camera);

    worldView.unproject(vector3);

    verify(camera).unproject(vector3);
  }
}