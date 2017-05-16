package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.ModelBundle;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseShipTest {

  private static final String NAME_KEY = "yo";
  private static final String RESOURCE_KEY = "yo_face";
  private static final int MAX_HP = 20;
  private static final int HP = 10;
  private static final int MOVE_DISTANCE = 3;

  @Mock
  private Items items;
  @Mock
  private Armor armor1;
  @Mock
  private Armor armor2;
  @Mock
  private Weapon weapon1;
  @Mock
  private Consumable consumable1;

  private Ship ship;

  @Before
  public void setUp() {
    ship = new BaseShip(NAME_KEY, RESOURCE_KEY, MAX_HP, HP, MOVE_DISTANCE, items);
  }

  @Test
  public void get_name() {
    Message name = ship.getName();
    assertThat(name.getBundle()).isSameAs(ModelBundle.SHIP_NAME);
    assertThat(name.getKey()).isEqualTo(NAME_KEY);
  }

  @Test
  public void get_defense() {
    when(items.getEquippedArmors()).thenReturn(ImmutableList.of(armor1, armor2));
    when(armor1.getDefense()).thenReturn(11);
    when(armor2.getDefense()).thenReturn(13);

    assertThat(ship.getDefense()).isEqualTo(24);
  }

  @Test
  public void damage_by() {
    ship.damageBy(5);

    assertThat(ship.getHp()).isEqualTo(HP - 5);

    ship.damageBy(1000);

    assertThat(ship.getHp()).isEqualTo(0);
  }

  @Test
  public void heal_by() {
    ship.healBy(5);

    assertThat(ship.getHp()).isEqualTo(HP + 5);

    ship.healBy(1000);

    assertThat(ship.getHp()).isEqualTo(MAX_HP);
  }

  @Test
  public void full_heal() {
    ship.fullHeal();

    assertThat(ship.getHp()).isEqualTo(MAX_HP);
  }

  @Test
  public void get_consumables() {
    when(items.getConsumables()).thenReturn(ImmutableList.of(consumable1));

    assertThat(ship.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void get_weapons() {
    when(items.getWeapons()).thenReturn(ImmutableList.of(weapon1));

    assertThat(ship.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void get_equipped_armors() {
    when(items.getEquippedArmors()).thenReturn(ImmutableList.of(armor1));

    assertThat(ship.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void get_stashed_armors() {
    when(items.getStashedArmors()).thenReturn(ImmutableList.of(armor1));

    assertThat(ship.getStashedArmors()).containsExactly(armor1);
  }

  @Test
  public void use_consumable() {
    ship.useConsumable(consumable1);

    verify(items).useConsumable(consumable1);
  }

  @Test
  public void use_weapon() {
    ship.useWeapon(weapon1);

    verify(items).useWeapon(weapon1);
  }

  @Test
  public void use_equipped_armors() {
    ship.useEquippedArmors();

    verify(items).useEquippedArmors();
  }

  @Test
  public void equip_armor() {
    ship.equipArmor(armor1);

    verify(items).equipArmor(armor1);
  }

  @Test
  public void unequip_armor() {
    ship.unequipArmor(armor1);

    verify(items).unequipArmor(armor1);
  }
}