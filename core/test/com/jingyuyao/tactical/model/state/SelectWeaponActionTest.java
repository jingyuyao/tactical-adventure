package com.jingyuyao.tactical.model.state;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SelectWeaponActionTest {

  @Mock
  private BasePlayerState playerState;
  @Mock
  private Weapon weapon;

  @Test
  public void run() {
    SelectWeaponAction selectWeaponAction = new SelectWeaponAction(playerState, weapon);

    selectWeaponAction.run();

    verify(playerState).selectWeapon(weapon);
  }
}