package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EndTurnTest {

  @Mock
  private ModelBus modelBus;
  @Mock
  private WorldState worldState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private World world;
  @Mock
  private Cell cell;
  @Mock
  private Player player;
  @Mock
  private Retaliating retaliating;

  private EndTurn endTurn;

  @Before
  public void setUp() {
    endTurn = new EndTurn(modelBus, worldState, stateFactory, world);
  }

  @Test
  public void enter() {
    when(world.getCharacterSnapshot()).thenReturn(ImmutableList.of(cell));
    when(cell.player()).thenReturn(Optional.of(player));
    when(stateFactory.createRetaliating()).thenReturn(retaliating);

    endTurn.enter();

    verify(player).setActionable(true);
    verify(worldState).incrementTurn();
    verify(worldState).branchTo(retaliating);
  }
}