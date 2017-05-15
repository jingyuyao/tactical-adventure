package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.BattleSequence;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Retaliation;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.Promise;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
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
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Movements movements;
  @Mock
  private World world;
  @Mock
  private BattleSequence battleSequence;
  @Mock
  private Turn turn;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Enemy enemy;
  @Mock
  private Enemy enemy2;
  @Mock
  private Retaliation retaliation;
  @Mock
  private Retaliation retaliation2;
  @Mock
  private Path path;
  @Mock
  private Battle battle;
  @Mock
  private Cell origin;
  @Mock
  private StartTurn startTurn;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;
  @Captor
  private ArgumentCaptor<Runnable> runnableCaptor;

  private Retaliating retaliating;

  @Before
  public void setUp() {
    retaliating =
        new Retaliating(modelBus, worldState, stateFactory, movements, world, battleSequence);
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

  @Test(expected = IllegalStateException.class)
  public void enter_wrong_stage() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.PLAYER);

    retaliating.enter();
  }

  @Test
  public void enter() {
    when(worldState.getTurn()).thenReturn(turn);
    when(turn.getStage()).thenReturn(TurnStage.ENEMY);
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell, cell2));
    when(cell.enemy()).thenReturn(Optional.of(enemy));
    when(cell2.enemy()).thenReturn(Optional.of(enemy2));
    when(enemy.getRetaliation(movements, cell)).thenReturn(retaliation);
    when(enemy2.getRetaliation(movements, cell2)).thenReturn(retaliation2);
    when(retaliation.path()).thenReturn(Optional.of(path));
    when(retaliation.battle()).thenReturn(Optional.of(battle));
    when(retaliation2.path()).thenReturn(Optional.<Path>absent());
    when(retaliation2.battle()).thenReturn(Optional.<Battle>absent());
    when(path.getOrigin()).thenReturn(origin);
    when(origin.moveCharacter(path)).thenReturn(Promise.immediate());
    when(stateFactory.createStartTurn()).thenReturn(startTurn);

    retaliating.enter();

    InOrder inOrder =
        Mockito.inOrder(enemy, enemy2, worldState, modelBus, origin, turn, battleSequence);
    inOrder.verify(modelBus, times(2)).post(argumentCaptor.capture());
    inOrder.verify(enemy).getRetaliation(movements, cell);
    inOrder.verify(origin).moveCharacter(path);
    inOrder.verify(battleSequence).start(Mockito.eq(battle), runnableCaptor.capture());
    runnableCaptor.getValue().run();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    inOrder.verify(enemy2).getRetaliation(movements, cell2);
    inOrder.verify(turn).advance();
    inOrder.verify(modelBus).post(argumentCaptor.capture());
    inOrder.verify(worldState).branchTo(startTurn);
    assertThat(argumentCaptor.getAllValues().get(0)).isSameAs(retaliating);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, enemy, ActivatedEnemy.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 2, enemy2, ActivatedEnemy.class);
    assertThat(argumentCaptor.getAllValues().get(3)).isInstanceOf(Save.class);
  }

  @Test
  public void exit() {
    retaliating.exit();

    verify(modelBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, retaliating, ExitState.class);
  }
}