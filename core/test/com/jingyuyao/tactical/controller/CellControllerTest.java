package com.jingyuyao.tactical.controller;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.map.Cell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CellControllerTest {

  @Mock
  private WorldCamera worldCamera;
  @Mock
  private Model model;
  @Mock
  private Cell cell;
  @Mock
  private InputEvent inputEvent;

  private CellController cellController;

  @Before
  public void setUp() {
    cellController = new CellController(worldCamera, model, cell);
  }

  @Test
  public void touch_down() {
    assertThat(cellController.touchDown(inputEvent, 0, 0, 0, 0)).isTrue();
  }

  @Test
  public void touch_up_dragged() {
    when(worldCamera.isDragged()).thenReturn(true);

    cellController.touchUp(inputEvent, 0, 0, 0, 0);

    verifyZeroInteractions(model);
  }

  @Test
  public void touch_up_not_dragged() {
    when(worldCamera.isDragged()).thenReturn(false);

    cellController.touchUp(inputEvent, 0, 0, 0, 0);

    verify(model).select(cell);
  }
}