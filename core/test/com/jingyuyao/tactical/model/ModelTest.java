package com.jingyuyao.tactical.model;

import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.state.MapState;
import com.jingyuyao.tactical.model.state.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

  @Mock
  private World world;
  @Mock
  private MapState mapState;
  @Mock
  private EventBus eventBus;
  @Mock
  private Cell cell;
  @Mock
  private State state;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Model model;

  @Before
  public void setUp() {
    model = new Model(world, mapState, eventBus);
  }

  @Test
  public void load() {
    Iterable<Cell> cells = ImmutableList.of();

    model.load(cells, state);

    verify(world).load(cells);
    verify(mapState).initialize(state);
  }

  @Test
  public void prep_for_save() {
    model.prepForSave();

    verify(mapState).prepForSave();
  }

  @Test
  public void reset() {
    model.reset();

    verify(world).reset();
    verify(mapState).reset();
  }

  @Test
  public void select() {
    model.select(cell);

    verify(mapState).select(cell);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, cell, SelectCell.class);
  }
}