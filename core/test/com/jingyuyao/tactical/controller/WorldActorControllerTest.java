package com.jingyuyao.tactical.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldActorControllerTest {

  @Mock
  private SelectionHandler selectionHandler;
  @Mock
  private CameraController cameraController;
  @Mock
  private MapObject mapObject;
  @Mock
  private InputEvent inputEvent;

  private WorldActorController worldActorController;

  @Before
  public void setUp() {
    worldActorController = new WorldActorController(selectionHandler, cameraController, mapObject);
  }

  @Test
  public void touch_down() {
    assertThat(worldActorController.touchDown(inputEvent, 0, 0, 0, 0)).isTrue();
  }

  @Test
  public void touch_up_dragged() {
    when(cameraController.isDragged()).thenReturn(true);

    worldActorController.touchUp(inputEvent, 0, 0, 0, 0);

    verifyZeroInteractions(mapObject);
  }

  @Test
  public void touch_up_not_dragged() {
    when(cameraController.isDragged()).thenReturn(false);

    worldActorController.touchUp(inputEvent, 0, 0, 0, 0);

    verify(mapObject).select(selectionHandler);
  }
}