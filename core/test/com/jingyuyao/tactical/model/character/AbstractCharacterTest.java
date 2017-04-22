package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractCharacterTest {

  private static final String NAME = "yo";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Consumable consumable;
  @Mock
  private Item newItem;

  private List<Item> items;
  private Character character;

  @Before
  public void setUp() {
    items = Lists.newArrayList(weapon1, consumable, weapon2);
    character = new CharacterImpl(NAME, MAX_HP, HP, MOVE_DISTANCE, items);
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
  public void add_item() {
    character.addItem(newItem);

    assertThat(items).contains(newItem);
  }

  @Test
  public void remove_item() {
    character.removeItem(weapon1);

    assertThat(items).containsExactly(consumable, weapon2).inOrder();
  }

  @Test
  public void fluent_items() {
    assertThat(character.fluentItems()).containsExactly(weapon1, consumable, weapon2).inOrder();
  }

  @Test
  public void use_item() {
    when(weapon1.getUsageLeft()).thenReturn(1);

    character.useItem(weapon1);

    verify(weapon1).useOnce();
    assertThat(items).contains(weapon1);
  }

  @Test
  public void use_item_broken() {
    when(weapon1.getUsageLeft()).thenReturn(0);

    character.useItem(weapon1);

    verify(weapon1).useOnce();
    assertThat(items).doesNotContain(weapon1);
  }

  private static class CharacterImpl extends AbstractCharacter {

    CharacterImpl(String name, int maxHp, int hp, int moveDistance, List<Item> items) {
      super(name, maxHp, hp, moveDistance, items);
    }
  }
}