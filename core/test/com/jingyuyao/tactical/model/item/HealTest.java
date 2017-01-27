package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.character.Character;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealTest {

  private static final String NAME = "pot";
  private static final int INITIAL_USAGE = 2;
  private static final int AMOUNT = 11;

  @Mock
  private Character owner;

  private Heal heal;

  @Before
  public void setUp() {
    heal = new Heal(owner, new HealData(NAME, INITIAL_USAGE, AMOUNT));
  }

  @Test
  public void consume() {
    heal.consume();
    heal.consume();

    verify(owner, times(2)).healBy(AMOUNT);
    verify(owner).removeItem(heal);
  }
}