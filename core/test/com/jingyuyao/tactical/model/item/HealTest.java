package com.jingyuyao.tactical.model.item;

import static org.mockito.Mockito.verify;

import com.jingyuyao.tactical.model.character.Character;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HealTest {

  private static final int AMOUNT = 11;

  @Mock
  private Character character;

  private Heal heal;

  @Before
  public void setUp() {
    heal = new Heal(AMOUNT);
  }

  @Test
  public void consume() {
    heal.apply(character);

    verify(character).healBy(AMOUNT);
  }
}