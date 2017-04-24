package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
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
public class RetaliatingTest {

  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private ModelBus modelBus;
  @Mock
  private World world;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Enemy enemy;
  @Mock
  private Enemy enemy2;
  @Mock
  private Waiting waiting;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Retaliating retaliating;

  @Before
  public void setUp() {
    retaliating = new Retaliating(modelBus, worldState, stateFactory, world);
  }

  @Test
  public void select() {
    retaliating.select(cell);

    verifyZeroInteractions(worldState);
  }

  @Test
  public void actions() {
    assertThat(retaliating.getActions()).isEmpty();
  }

  @Test
  public void enter() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.hasEnemy()).thenReturn(true);
    when(cell2.hasEnemy()).thenReturn(true);
    when(cell.getEnemy()).thenReturn(enemy);
    when(cell2.getEnemy()).thenReturn(enemy2);
    when(enemy.retaliate(cell)).thenReturn(MyFuture.immediate());
    when(enemy2.retaliate(cell2)).thenReturn(MyFuture.immediate());
    when(stateFactory.createWaiting()).thenReturn(waiting);

    retaliating.enter();

    InOrder inOrder = Mockito.inOrder(enemy, enemy2, worldState, modelBus);
    inOrder.verify(modelBus, times(2)).post(argumentCaptor.capture());
    inOrder.verify(enemy).retaliate(cell);
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    inOrder.verify(enemy2).retaliate(cell2);
    inOrder.verify(worldState).branchTo(waiting);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(retaliating);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, enemy, ActivatedEnemy.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, enemy2, ActivatedEnemy.class);
  }

  @Test
  public void exit() {
    retaliating.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, retaliating, ExitState.class);
  }
}