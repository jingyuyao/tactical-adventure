package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WaitingTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Characters characters;
  @Mock
  private Movements movements;
  @Mock
  private Player player;
  @Mock
  private Moving moving;
  @Mock
  private Retaliating retaliating;
  @Mock
  private Movement movement;

  private Waiting waiting;

  @Before
  public void setUp() {
    waiting = new Waiting(mapState, stateFactory, characters, movements);
  }

  @Test
  public void select_player_actionable() {
    when(player.isActionable()).thenReturn(true);
    when(movements.distanceFrom(player)).thenReturn(movement);
    when(stateFactory.createMoving(player, movement)).thenReturn(moving);

    waiting.select(player);

    verify(stateFactory).createMoving(player, movement);
    verify(mapState).goTo(moving);
  }

  @Test
  public void select_player_not_actionable() {
    when(player.isActionable()).thenReturn(false);

    waiting.select(player);

    verifyZeroInteractions(stateFactory);
    verifyZeroInteractions(mapState);
  }

  @Test
  public void end_turn() {
    when(characters.fluent()).thenReturn(FluentIterable.<Character>of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    waiting.endTurn();

    verify(player).setActionable(true);
    verify(mapState).goTo(retaliating);
  }

  @Test
  public void actions() {
    List<Action> actions = waiting.getActions();

    assertThat(actions).hasSize(1);
    assertThat(actions.get(0)).isInstanceOf(EndTurnAction.class);
  }
}
