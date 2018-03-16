package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
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
  private PooledEngine engine;
  @Mock
  private WorldCamera worldCamera;
  @Mock
  private Systems systems;
  @Mock
  private Matrix4 matrix4;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(batch, engine, worldCamera, systems);
    verify(systems).addTo(engine);
  }

  @Test
  public void update() {
    when(worldCamera.getProjectionMatrix()).thenReturn(matrix4);

    worldView.update(5f);

    InOrder inOrder = Mockito.inOrder(worldCamera, engine, batch);
    inOrder.verify(worldCamera).apply();
    inOrder.verify(batch).begin();
    inOrder.verify(batch).setProjectionMatrix(matrix4);
    inOrder.verify(engine).update(5f);
    inOrder.verify(batch).end();
  }

  @Test
  public void resize() {
    worldView.resize(11, 12);

    verify(worldCamera).resize(11, 12);
  }
}
