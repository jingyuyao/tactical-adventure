package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemsTest {

  @Mock
  private Consumable consumable1;
  @Mock
  private Weapon weapon1;
  @Mock
  private Armor armor1;
  @Mock
  private Armor armor2;

  private Items items;

  @Before
  public void setUp() {
    items = new Items(
        Lists.newArrayList(consumable1),
        Lists.newArrayList(weapon1),
        Lists.newArrayList(armor2),
        armor1
    );
  }

  @Test
  public void consumables() {
    assertThat(items.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void weapons() {
    assertThat(items.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void equipped_armors() {
    assertThat(items.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void unequipped_armors() {
    assertThat(items.getUnequippedArmors()).containsExactly(armor2);
  }

  @Test
  public void use_consumable() {
    when(consumable1.getUsageLeft()).thenReturn(1);

    items.useConsumable(consumable1);

    verify(consumable1).useOnce();
    assertThat(items.getConsumables()).containsExactly(consumable1);
  }

  @Test
  public void use_consumable_no_more() {
    when(consumable1.getUsageLeft()).thenReturn(0);

    items.useConsumable(consumable1);

    verify(consumable1).useOnce();
    assertThat(items.getConsumables()).isEmpty();
  }

  @Test
  public void use_weapon() {
    when(weapon1.getUsageLeft()).thenReturn(1);

    items.useWeapon(weapon1);

    verify(weapon1).useOnce();
    assertThat(items.getWeapons()).containsExactly(weapon1);
  }

  @Test
  public void use_weapon_broken() {
    when(weapon1.getUsageLeft()).thenReturn(0);

    items.useWeapon(weapon1);

    verify(weapon1).useOnce();
    assertThat(items.getWeapons()).isEmpty();
  }

  @Test
  public void use_armors() {
    when(armor1.getUsageLeft()).thenReturn(1);

    items.useEquippedArmors();

    verify(armor1).useOnce();
    assertThat(items.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void use_armors_broken() {
    when(armor1.getUsageLeft()).thenReturn(0);

    items.useEquippedArmors();

    verify(armor1).useOnce();
    assertThat(items.getEquippedArmors()).isEmpty();
  }

  @Test
  public void equip_body_armor() {
    items.equipBodyArmor(armor2);

    assertThat(items.getEquippedArmors()).containsExactly(armor2);
    assertThat(items.getUnequippedArmors()).containsExactly(armor1);
  }
}