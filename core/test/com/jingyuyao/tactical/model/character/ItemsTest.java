package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemsTest {

  @Mock
  private Armor armor1;
  @Mock
  private Armor armor2;
  @Mock
  private Weapon weapon1;
  @Mock
  private Weapon weapon2;
  @Mock
  private Weapon weapon3;

  private Items items;

  @Before
  public void setUp() {
    items = new Items(Lists.newArrayList(armor2, weapon3), armor1, weapon1, weapon2);
  }

  @Test
  public void unequipped_items() {
    assertThat(items.getUnequippedItems()).containsExactly(armor2, weapon3);
  }

  @Test
  public void equipped_armors() {
    assertThat(items.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void equipped_weapons() {
    assertThat(items.getEquippedWeapons()).containsExactly(weapon1, weapon2);
  }

  @Test
  public void use_unequipped_item() {
    when(armor2.getUsageLeft()).thenReturn(1);

    items.useUnequippedItem(armor2);

    verify(armor2).useOnce();
    assertThat(items.getUnequippedItems()).containsExactly(armor2, weapon3);
  }

  @Test
  public void use_unequipped_item_broken() {
    when(armor2.getUsageLeft()).thenReturn(0);

    items.useUnequippedItem(armor2);

    verify(armor2).useOnce();
    assertThat(items.getUnequippedItems()).containsExactly(weapon3);
  }

  @Test
  public void use_armors() {
    when(armor1.getUsageLeft()).thenReturn(1);

    items.useArmors();

    verify(armor1).useOnce();
    assertThat(items.getEquippedArmors()).containsExactly(armor1);
  }

  @Test
  public void use_armors_broken() {
    when(armor1.getUsageLeft()).thenReturn(0);

    items.useArmors();

    verify(armor1).useOnce();
    assertThat(items.getEquippedArmors()).isEmpty();
  }

  @Test
  public void use_weapons() {
    when(weapon1.getUsageLeft()).thenReturn(1);
    when(weapon2.getUsageLeft()).thenReturn(0);

    items.useWeapons();

    verify(weapon1).useOnce();
    verify(weapon2).useOnce();
    assertThat(items.getEquippedWeapons()).containsExactly(weapon1);
  }

  @Test
  public void equip_body_armor() {
    items.equipBodyArmor(armor2);

    assertThat(items.getEquippedArmors()).containsExactly(armor2);
    assertThat(items.getUnequippedItems()).containsExactly(armor1, weapon3);
  }

  @Test
  public void equip_weapon_1() {
    items.equipWeapon1(weapon3);

    assertThat(items.getEquippedWeapons()).containsExactly(weapon2, weapon3);
    assertThat(items.getUnequippedItems()).containsExactly(armor2, weapon1);
  }

  @Test
  public void equip_weapon_2() {
    items.equipWeapon2(weapon3);

    assertThat(items.getEquippedWeapons()).containsExactly(weapon1, weapon3);
    assertThat(items.getUnequippedItems()).containsExactly(armor2, weapon2);
  }
}