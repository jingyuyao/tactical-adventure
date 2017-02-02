package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UseConsumableActionTest {

  @Mock
  private BasePlayerState playerState;
  @Mock
  private Consumable consumable;
  @Mock
  private Player player;

  private UseConsumableAction useConsumableAction;

  @Test
  public void run() {
    useConsumableAction = new UseConsumableAction(playerState, player, consumable);

    useConsumableAction.run();

    verify(player).quickAccess(consumable);
    verify(consumable).apply(player);
    verify(player).useItem(consumable);
    verify(playerState).finish();
  }
}