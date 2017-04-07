package com.jingyuyao.tactical.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldControllerTest {

  @Mock
  private Viewport worldViewport;
  @Mock
  private WorldCamera worldCamera;
  @Mock
  private Model model;
  @Mock
  private World world;
  @Mock
  private Camera camera;
  @Mock
  private Cell cell;
  @Captor
  private ArgumentCaptor<Vector3> vector3Captor;

  private WorldController worldController;

  @Before
  public void setUp() {
    worldController = new WorldController(worldViewport, worldCamera, model, world);
  }

  @Test
  public void touch_down() {
    assertThat(worldController.touchDown(0, 0, 0, 0)).isTrue();
  }

  @Test
  public void touch_up_dragged() {
    when(worldCamera.isDragged()).thenReturn(true);

    worldController.touchUp(0, 0, 0, 0);

    verifyZeroInteractions(world);
    verifyZeroInteractions(model);
  }

  @Test
  public void touch_up_select() {
    when(worldCamera.isDragged()).thenReturn(false);
    when(worldViewport.getCamera()).thenReturn(camera);
    when(world.getCell(1, 2)).thenReturn(Optional.of(cell));

    worldController.touchUp(1, 2, 0, 0);

    verify(camera).unproject(vector3Captor.capture());
    Vector3 vector3 = vector3Captor.getValue();
    assertThat(vector3.x).isEqualTo(1.0f);
    assertThat(vector3.y).isEqualTo(2.0f);
    verify(model).select(cell);
  }
}