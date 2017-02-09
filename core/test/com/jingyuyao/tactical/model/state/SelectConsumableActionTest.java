package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectConsumableActionTest {

  @Mock
  private BasePlayerState playerState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Consumable consumable;
  @Mock
  private UsingConsumable usingConsumable;

  private SelectConsumableAction selectConsumableAction;

  @Before
  public void setUp() {
    selectConsumableAction =
        new SelectConsumableAction(playerState, stateFactory, player, consumable);
  }

  @Test
  public void run() {
    when(stateFactory.createUsingConsumable(player, consumable)).thenReturn(usingConsumable);

    selectConsumableAction.run();

    verify(player).quickAccess(consumable);
    verify(playerState).goTo(usingConsumable);
  }
}