package com.jingyuyao.tactical.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector3;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.state.WorldState;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.WorldCamera;
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
  private CameraController cameraController;
  @Mock
  private WorldCamera worldCamera;
  @Mock
  private World world;
  @Mock
  private WorldState worldState;
  @Mock
  private Cell cell;
  @Captor
  private ArgumentCaptor<Vector3> vector3Captor;

  private WorldController worldController;

  @Before
  public void setUp() {
    worldController = new WorldController(cameraController, worldCamera, world, worldState);
  }

  @Test
  public void touch_down() {
    assertThat(worldController.touchDown(0, 0, 0, 0)).isTrue();
  }

  @Test
  public void touch_up_dragged() {
    when(cameraController.isDragged()).thenReturn(true);

    worldController.touchUp(0, 0, 0, 0);

    verifyZeroInteractions(world);
    verifyZeroInteractions(worldState);
  }

  @Test
  public void touch_up_select() {
    when(cameraController.isDragged()).thenReturn(false);
    when(world.cell(1, 2)).thenReturn(Optional.of(cell));

    worldController.touchUp(1, 2, 0, 0);

    verify(worldCamera).unproject(vector3Captor.capture());
    Vector3 vector3 = vector3Captor.getValue();
    assertThat(vector3.x).isEqualTo(1.0f);
    assertThat(vector3.y).isEqualTo(2.0f);
    verify(worldState).select(cell);
  }
}