package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseCharacterTest {

  private static final String NAME = "yo";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private Items items;
  @Mock
  private Weapon weapon1;
  @Mock
  private Consumable consumable;

  private Character character;

  @Before
  public void setUp() {
    character = new BaseCharacter(NAME, MAX_HP, HP, MOVE_DISTANCE, items);
  }

  @Test
  public void damage_by() {
    character.damageBy(5);

    assertThat(character.getHp()).isEqualTo(HP - 5);

    character.damageBy(1000);

    assertThat(character.getHp()).isEqualTo(0);
  }

  @Test
  public void heal_by() {
    character.healBy(5);

    assertThat(character.getHp()).isEqualTo(HP + 5);

    character.healBy(1000);

    assertThat(character.getHp()).isEqualTo(MAX_HP);
  }

  @Test
  public void full_heal() {
    character.fullHeal();

    assertThat(character.getHp()).isEqualTo(MAX_HP);
  }

  @Test
  public void fluent_items() {
    when(items.getUnequippedItems()).thenReturn(ImmutableList.of(weapon1, consumable));

    assertThat(character.fluentItems()).containsExactly(weapon1, consumable);
  }

  @Test
  public void use_item() {
    character.useItem(weapon1);

    verify(items).useUnequippedItem(weapon1);
  }
}