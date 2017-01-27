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

  @Mock
  private Character owner;

  private Heal heal;

  @Before
  public void setUp() {
    heal = new Heal(owner, new ItemData(NAME, INITIAL_USAGE));
  }

  @Test
  public void consume() {
    heal.consume();
    heal.consume();

    verify(owner, times(2)).healBy(Heal.AMOUNT);
    verify(owner).removeItem(heal);
  }
}