package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
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
  private Batch batch;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private Vector3 vector3;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(worldEngine, mapRenderer, viewport, batch);
  }

  @Test
  public void update() {
    when(viewport.getCamera()).thenReturn(camera);

    worldView.update(5f);

    InOrder inOrder = Mockito.inOrder(viewport, mapRenderer, worldEngine, batch);
    inOrder.verify(viewport).apply();
    inOrder.verify(mapRenderer).setView(camera);
    inOrder.verify(mapRenderer).render();
    inOrder.verify(batch).begin();
    inOrder.verify(batch).setProjectionMatrix(camera.combined);
    inOrder.verify(worldEngine).update(5f);
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