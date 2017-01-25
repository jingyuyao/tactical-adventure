package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealTest {

  private static final String NAME = "pot";
  private static final int INITIAL_USAGE = 2;

  @Mock
  private Character owner;
  @Mock
  private Player player;

  private Heal heal;

  @Before
  public void setUp() {
    heal = new Heal(owner, new ItemStats(NAME, INITIAL_USAGE));
  }

  @Test
  public void consume() {
    heal.consume(player);
    heal.consume(player);

    verify(player, times(2)).healBy(Heal.AMOUNT);
    verify(owner).removeItem(heal);
  }
}