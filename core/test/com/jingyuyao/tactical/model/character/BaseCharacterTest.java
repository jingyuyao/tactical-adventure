package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Armor;
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
  private Armor armor1;
  @Mock
  private Weapon weapon1;
  @Mock
  private Consumable consumable1;

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
  public void get_consumables() {
    when(items.getConsumables()).thenReturn(ImmutableList.of(consumable1));

    assertThat(character.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void get_weapons() {
    when(items.getWeapons()).thenReturn(ImmutableList.of(weapon1));

    assertThat(character.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void get_equipped_armors() {
    when(items.getEquippedArmors()).thenReturn(ImmutableList.of(armor1));

    assertThat(character.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void get_unequipped_armors() {
    when(items.getUnequippedArmors()).thenReturn(ImmutableList.of(armor1));

    assertThat(character.getUnequippedArmors()).containsExactly(armor1);
  }

  @Test
  public void use_consumable() {
    character.useConsumable(consumable1);

    verify(items).useConsumable(consumable1);
  }

  @Test
  public void use_weapon() {
    character.useWeapon(weapon1);

    verify(items).useWeapon(weapon1);
  }

  @Test
  public void use_equipped_armors() {
    character.useEquippedArmors();

    verify(items).useEquippedArmors();
  }

  @Test
  public void equip_body_armor() {
    character.equipBodyArmor(armor1);

    verify(items).equipBodyArmor(armor1);
  }
}