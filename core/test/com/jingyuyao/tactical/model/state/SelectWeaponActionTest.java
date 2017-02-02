package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectWeaponActionTest {

  private static final Coordinate PLAYER_COORDINATE = new Coordinate(101, 101);

  @Mock
  private BasePlayerState playerState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private ImmutableList<Target> targets;
  @Mock
  private SelectingTarget selectingTarget;

  private SelectWeaponAction selectWeaponAction;

  @Test
  public void run() {
    when(player.getCoordinate()).thenReturn(PLAYER_COORDINATE);
    when(weapon.createTargets(PLAYER_COORDINATE)).thenReturn(targets);
    when(stateFactory.createSelectingTarget(player, weapon, targets)).thenReturn(selectingTarget);

    selectWeaponAction = new SelectWeaponAction(playerState, stateFactory, player, weapon);

    selectWeaponAction.run();

    verify(player).quickAccess(weapon);
    verify(playerState).goTo(selectingTarget);
  }
}